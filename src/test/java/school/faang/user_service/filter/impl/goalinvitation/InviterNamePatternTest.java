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
public class InviterNamePatternTest {

    private InviterNamePattern inviterNamePattern;
    private List<GoalInvitation> goalInvitations;

    @BeforeEach
    public void setUp() {
        inviterNamePattern = new InviterNamePattern();
        goalInvitations = new ArrayList<>();
    }

    @Test
    public void testIsApplicable_WithInviterNamePattern_ReturnsTrue() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        when(filterDto.getInviterNamePattern()).thenReturn("John");
        boolean result = inviterNamePattern.isApplicable(filterDto);

        assertTrue(result);
    }

    @Test
    public void testIsApplicable_WithoutInviterNamePattern_ReturnsFalse() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        when(filterDto.getInviterNamePattern()).thenReturn(null);
        boolean result = inviterNamePattern.isApplicable(filterDto);

        assertFalse(result);
    }

    @Test
    public void testApply_WithMatchingInviterNamePattern_ReturnsFilteredGoalInvitations() {
        InvitationFilterDto filterDto = new InvitationFilterDto();
        filterDto.setInviterNamePattern("John");

        User inviter = new User();
        inviter.setId(1L);
        inviter.setUsername("John Doe");

        User inviter2 = new User();
        inviter2.setId(2L);
        inviter2.setUsername("Jane Smith");

        GoalInvitation invitation1 = new GoalInvitation();
        invitation1.setId(1L);
        invitation1.setInviter(inviter);

        GoalInvitation invitation2 = new GoalInvitation();
        invitation2.setId(2L);
        invitation2.setInviter(inviter2);

        goalInvitations.add(invitation1);
        goalInvitations.add(invitation2);

        List<GoalInvitation> result = inviterNamePattern.apply(goalInvitations, filterDto);

        assertEquals(1, result.size());
        assertEquals(invitation1.getInviter().getUsername(), result.get(0).getInviter().getUsername());
    }
}
