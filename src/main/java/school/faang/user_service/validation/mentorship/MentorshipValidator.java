package school.faang.user_service.validation.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.exception.DataValidationException;

@Component
@RequiredArgsConstructor
public class MentorshipValidator {

    public void validateMentorMenteeIds(long menteeId, long mentorId) {
        if (menteeId == mentorId) {
            throw new DataValidationException("Mentee's and mentor's IDs are the same");
        }
    }
}
