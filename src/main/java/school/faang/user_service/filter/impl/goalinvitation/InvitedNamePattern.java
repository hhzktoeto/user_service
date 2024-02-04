package school.faang.user_service.filter.impl.goalinvitation;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.InvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;
import school.faang.user_service.filter.Filter;

import java.util.List;

/**
 * @author Alexander Bulgakov
 */
@Component
public class InvitedNamePattern implements Filter<InvitationFilterDto, GoalInvitation> {
    @Override
    public boolean isApplicable(InvitationFilterDto filterDto) {
        return filterDto.getInvitedNamePattern() != null;
    }

    @Override
    public void apply(List<GoalInvitation> goalInvitations, InvitationFilterDto filter) {
        goalInvitations.removeIf(goalInvitation ->
                !goalInvitation.getInvited().getUsername()
                        .contains(filter.getInvitedNamePattern()));
    }
}