package school.faang.user_service.mapper.goal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import school.faang.user_service.dto.goal.CreateGoalDto;
import school.faang.user_service.dto.goal.ResponseGoalDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.goal.Goal;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {
    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    @Mapping(source = "skillsToAchieve", target = "skillsToAchieve", qualifiedByName = "skillDtosToSkills")
    Goal toGoalFromCreateGoalDto(CreateGoalDto createGoalDto);

    @Mapping(source = "skillsToAchieve", target = "skillsToAchieve", qualifiedByName = "skillsToSkillDtos")
    ResponseGoalDto toResponseGoalDtoFromGoal(Goal goal);

    @Named("skillDtosToSkills")
    default List<Skill> skillDtosToSkills(List<SkillDto> skillDtos) {
        return skillDtos.stream()
                .map(skillDto -> Skill.builder()
                        .title(skillDto.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("skillsToSkillDtos")
    default List<SkillDto> skillsToSkillDtos(List<Skill> skills) {
        return skills.stream()
                .map(skillDto -> SkillDto.builder()
                        .title(skillDto.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}
