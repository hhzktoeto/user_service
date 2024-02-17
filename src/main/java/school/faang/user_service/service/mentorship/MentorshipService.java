package school.faang.user_service.service.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.UserMapper;
import school.faang.user_service.repository.mentorship.MentorshipRepository;
import school.faang.user_service.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipService {
    private final UserService userService;
    private final MentorshipRepository mentorshipRepository;
    private final UserMapper userMapper;

    public List<UserDto> getMentors(long id) {
        User user = userService.getUserById(id);
        return userMapper.toDtoList(user.getMentors());
    }

    public List<UserDto> getMentees(long id) {
        User user = userService.getUserById(id);
        return userMapper.toDtoList(user.getMentees());
    }

    public void deleteMentor(long menteeId, long mentorId) {
        User mentee = userService.getUserById(menteeId);
        User mentor = userService.getUserById(mentorId);
        if (mentee.getMentors().remove(mentor)) {
            mentorshipRepository.save(mentee);
        }
    }

    public void deleteMentee(long mentorId, long menteeId) {
        User mentor = userService.getUserById(mentorId);
        User mentee = userService.getUserById(menteeId);
        if (mentor.getMentees().remove(mentee)) {
            mentorshipRepository.save(mentor);
        }
    }
}