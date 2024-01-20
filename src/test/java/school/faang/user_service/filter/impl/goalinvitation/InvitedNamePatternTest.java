package school.faang.user_service.filter.impl.goalinvitation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.goal.InvitationFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.GoalInvitation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Bulgakov
 */
@ExtendWith(MockitoExtension.class)
public class InvitedNamePatternTest {

    private InvitedNamePattern invitedNamePattern;
    private List<GoalInvitation> goalInvitations;

    @BeforeEach
    public void setUp() {
        invitedNamePattern = new InvitedNamePattern();
        goalInvitations = new ArrayList<>();
    }

    @Test
    public void testIsApplicable_WithInvitedNamePattern_ReturnsTrue() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        when(filterDto.getInvitedNamePattern()).thenReturn("John");
        boolean result = invitedNamePattern.isApplicable(filterDto);

        assertTrue(result);
    }

    @Test
    public void testIsApplicable_WithoutInvitedNamePattern_ReturnsFalse() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        when(filterDto.getInvitedNamePattern()).thenReturn(null);
        boolean result = invitedNamePattern.isApplicable(filterDto);

        assertFalse(result);
    }

    @Test
    public void testApply_WithMatchingInvitedNamePattern_ReturnsFilteredGoalInvitations() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        filterDto.setInvitedNamePattern("John");

        User invited = new User();
        invited.setId(1L);
        invited.setUsername("John Doe");

        User invited2 = new User();
        invited2.setId(2L);
        invited2.setUsername("Jane Smith");

        GoalInvitation invitation1 = new GoalInvitation();
        invitation1.setId(1L);
        invitation1.setInvited(invited);

        GoalInvitation invitation2 = new GoalInvitation();
        invitation2.setId(2L);
        invitation2.setInvited(invited2);

        goalInvitations.add(invitation1);
        goalInvitations.add(invitation2);

        List<GoalInvitation> result = invitedNamePattern.apply(goalInvitations, filterDto);

        assertEquals(1, result.size());
        assertEquals(invitation1.getInvited().getUsername(), result.get(0).getInvited().getUsername());
    }
}