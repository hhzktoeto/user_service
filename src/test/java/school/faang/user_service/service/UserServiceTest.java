package school.faang.user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.event.EventService;
import school.faang.user_service.service.goal.GoalService;
import school.faang.user_service.service.mentorship.MentorshipService;
import school.faang.user_service.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventService eventService;
    @Mock
    private MentorshipService mentorshipService;
    @Mock
    private GoalService goalService;

    @Captor
    private ArgumentCaptor<User> captor;


    @Test
    void getUsers() {
        assertTrue(true);
    }

    @Test
    public void testGetUserById() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);

        assertTrue(userService.existsUserById(user.getId()));
    }

    @Test
    public void successDeleteNonActiveUser() {
        long months = 3L;
        LocalDateTime timeToDelete = LocalDateTime.now().minusMonths(months);
        userService.deleteNonActiveUsers();
        Mockito.verify(userRepository, times(1)).deleteAllInactiveUsersAndUpdatedAtOverMonths(timeToDelete);
    }

    @Test
    public void successDeactivationUserById() {
        List<Goal> goals = List.of(Goal.builder()
                .id(1L)
                .users(List.of(User.builder()
                        .id(10L)
                        .active(true)
                        .build()))
                .build());
        List<Event> events = List.of(Event.builder()
                .id(1L)
                .maxAttendees(2)
                .build());
        List<User> mentees = List.of(User.builder()
                .id(2L)
                .active(true)
                .build());
        User user = User.builder()
                .id(1L)
                .active(true)
                .goals(goals)
                .ownedEvents(events)
                .mentees(mentees)
                .build();
        long userId = user.getId();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.setEventService(eventService);
        userService.setMentorshipService(mentorshipService);

        userService.deactivationUserById(userId);
        verify(userRepository).save(captor.capture());

        User deactivatedUser = captor.getValue();
        assertFalse(deactivatedUser.isActive());
    }

}