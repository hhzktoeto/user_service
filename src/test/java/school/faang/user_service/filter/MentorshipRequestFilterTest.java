package school.faang.user_service.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.mentorship.filter.RequestFilterDto;
import school.faang.user_service.entity.MentorshipRequest;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.filter.mentorship.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestFilterTest {

    private final List<MentorshipRequestFilter> mentorshipRequestFilters = new ArrayList<>();

    private MentorshipRequestFilter mentorshipRequestFilter = new MentorshipDescriptionFilter();

    private MentorshipRequest mentorshipRequest = new MentorshipRequest();

    private RequestFilterDto requestFilterDto = new RequestFilterDto();

    private List<MentorshipRequest> mentorshipRequests;

    private User receiver = new User();
    private User requester = new User();


    @BeforeEach
    public void init() {
        receiver.setId(2L);
        receiver.setUsername("IVAN");
        requester.setId(3L);
        requester.setUsername("Mark");
        mentorshipRequestFilters.add(new MentorshipDescriptionFilter());
        mentorshipRequestFilters.add(new MentorshipReceiverFilter());
        mentorshipRequestFilters.add(new MentorshipRequesterFilter());
        mentorshipRequestFilters.add(new MentorshipStatusFilter());
        mentorshipRequests = new ArrayList<>(List.of(mentorshipRequest));
        mentorshipRequest.setReceiver(receiver);
        mentorshipRequest.setRequester(requester);
        mentorshipRequest.setDescription("One TWO THREE FOUR");
        mentorshipRequest.setStatus(RequestStatus.PENDING);
    }

    @Test
    void whenIncorrectDescription() {
        requestFilterDto.setDescriptionFilter("TW45O");
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        Assertions.assertEquals(0, mentorshipRequests.size());
    }

    @Test
    void whenCorrectMultipleParameters() {
        requestFilterDto.setDescriptionFilter("TWO");
        requestFilterDto.setStatusFilter(RequestStatus.PENDING);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        Assertions.assertEquals(1, mentorshipRequests.size());
    }

    @Test
    void whenCorrectMultipleParametersTwo() {
        requestFilterDto.setRequesterFilter(3L);
        requestFilterDto.setReceiverFilter(2L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        Assertions.assertEquals(1, mentorshipRequests.size());
    }

    @Test
    void whenIncorrectMultipleParameters() {
        requestFilterDto.setDescriptionFilter("Incorrect");
        requestFilterDto.setStatusFilter(RequestStatus.ACCEPTED);
        requestFilterDto.setRequesterFilter(4L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        Assertions.assertEquals(0, mentorshipRequests.size());
    }

    @Test
    void whenCorrectDescription() {
        requestFilterDto.setDescriptionFilter("TWO");
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        Assertions.assertEquals(1, mentorshipRequests.size());
    }

    @Test
    void whenIncorrectReceiver() {
        requestFilterDto.setReceiverFilter(3L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(0, mentorshipRequests.size());
    }

    @Test
    void whenCorrectReceiver() {
        requestFilterDto.setReceiverFilter(2L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(1, mentorshipRequests.size());
    }

    @Test
    void whenIncorrectRequester() {
        requestFilterDto.setRequesterFilter(4L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(0, mentorshipRequests.size());
    }

    @Test
    void whenCorrectRequester() {
        requestFilterDto.setRequesterFilter(3L);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(1, mentorshipRequests.size());
    }

    @Test
    void whenIncorrectStatus() {
        requestFilterDto.setStatusFilter(RequestStatus.ACCEPTED);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(0, mentorshipRequests.size());
    }

    @Test
    void whenCorrectStatus() {
        requestFilterDto.setStatusFilter(RequestStatus.PENDING);
        mentorshipRequestFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(requestFilterDto))
                .forEach(userFilter -> userFilter.apply(mentorshipRequests, requestFilterDto));
        assertEquals(1, mentorshipRequests.size());
    }

}