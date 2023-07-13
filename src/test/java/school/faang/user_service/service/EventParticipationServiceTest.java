package school.faang.user_service.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import school.faang.user_service.repository.event.EventParticipationRepository;
import school.faang.user_service.service.event.EventParticipationService;

import java.util.List;

public class EventParticipationServiceTest {
    @Mock
    private EventParticipationRepository eventParticipationRepository;

    @InjectMocks
    private EventParticipationService eventParticipationService;

    @Test
    public void registerParticipantTest() {
        Mockito.when(eventParticipationRepository.findAllParticipantsByEventId(1L)).thenReturn(List.of());
        eventParticipationService.registerParticipant(1L, 10L);
        Mockito.verify(eventParticipationRepository).register(1L, 10L);
    }
}
