package school.faang.user_service.service.goal.filter;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalFilterDto;
import school.faang.user_service.entity.goal.Goal;

import java.util.List;

@Component
public class GoalDescriptionFilter implements GoalFilter {

    @Override
    public boolean isApplicable(GoalFilterDto filters) {
        return filters.getDescriptionPattern() != null;
    }

    @Override
    public void apply(List<Goal> goals, GoalFilterDto filters) {
        goals.removeIf(goal -> !goal.getDescription().contains(filters.getDescriptionPattern()));
    }
}
