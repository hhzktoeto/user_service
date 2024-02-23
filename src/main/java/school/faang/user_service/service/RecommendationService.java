package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.RecommendationEvent;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.mapper.RecommendationMapper;
import school.faang.user_service.publisher.RecommendationEventPublisher;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepository;
    private final RecommendationMapper recommendationMapper;
    private final RecommendationEventPublisher recommendationEventPublisher;
    private final RecommendationRepository recommendationRepository;

    public RecommendationDto create(RecommendationDto recommendationDto) {
        validationToCreateRecommendation(recommendationDto);
        Recommendation entity = recommendationMapper.toEntity(recommendationDto);
        entity = recommendationRepository.save(entity);
        long entityId = recommendationRepository.create(recommendationDto.getAuthorId(), recommendationDto.getReceiverId(), recommendationDto.getContent());

        RecommendationEvent recommendationEvent = new RecommendationEvent(entityId, recommendationDto.getAuthorId(), recommendationDto.getReceiverId(), LocalDateTime.now());
        recommendationEventPublisher.publish(recommendationEvent);

        return recommendationMapper.toDto(entity);
    }

    private void validationToCreateRecommendation(RecommendationDto recommendationDto) {
        if (!userRepository.existsById(recommendationDto.getAuthorId())) {
            throw new IllegalArgumentException("There are no author id in data base");
        }

        if (!userRepository.existsById(recommendationDto.getReceiverId())) {
            throw new IllegalArgumentException("There are no receiver id in data base");
        }

        if (recommendationDto.getContent().isBlank() || recommendationDto.getContent() == null) {
            throw new IllegalArgumentException("There are no content");
        }

        if (recommendationDto.getAuthorId() == recommendationDto.getReceiverId()) {
            throw new IllegalArgumentException("You can not write a recommendation to yourself");
        }
    }

}
