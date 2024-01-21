package school.faang.user_service.service.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.skill.SkillMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillDto create (SkillDto skill) {

        getSkillFromDB(skill.getTitle());

        Skill skillEntity = skillMapper.toEntity(skill);
        List<User> users = userRepository.findAllById(skill.getUserIds());
        skillEntity.setUsers(users);

        skillEntity = skillRepository.save(skillEntity);

        return skillMapper.toDto(skillEntity);
    }

    public List<SkillDto> getUserSkills (long userId) {

        List<Skill> skills = skillRepository.findAllByUserId(userId);

        return skillMapper.listToDto(skills);
    }

    public void getSkillFromDB (String skillTitle) {
        if (skillRepository.existsByTitle(skillTitle)) {
            throw new DataValidationException("Skill with name " + skillTitle + " already exists in database.");
        }
    }

    public List<SkillCandidateDto> getOfferedSkills (long userId) {
        List<Skill> skills = skillRepository.findSkillsOfferedToUser(userId);
        List<SkillDto> skillDtos = skillMapper.listToDto(skills);

        Map<SkillDto, Long> skillDtosMap = skillDtos
                .stream()
                .collect(Collectors.groupingBy(skillDto -> skillDto, Collectors.counting()));

        List<SkillCandidateDto> skillCandidateDtos = skillDtosMap
                .entrySet().stream()
                .map(item -> new SkillCandidateDto(item.getKey(), item.getValue()))
                .toList();

        return skillCandidateDtos;
    }
}
