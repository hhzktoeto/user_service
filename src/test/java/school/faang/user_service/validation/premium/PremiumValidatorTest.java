package school.faang.user_service.validation.premium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.payment.PaymentResponse;
import school.faang.user_service.dto.payment.PaymentStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.premium.Premium;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.exception.ServiceInteractionException;
import school.faang.user_service.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PremiumValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PremiumValidator premiumValidator;

    @Test
    void validatePaymentStatus_InvalidResponse_ThrowsException() {
        assertThrows(ServiceInteractionException.class, ()
                -> premiumValidator.validatePaymentResponse(getFailedResponse()));
    }

    @Test
    void validatePaymentStatus_ValidResponse_DoesNotThrowException() {
        assertDoesNotThrow(() -> premiumValidator.validatePaymentResponse(getValidResponse()));
    }

    @Test
    void validateBuyPremium_UserHavePremium_ThrowsException() {
        User user = getUser();
        long userId = user.getId();
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getUser()));

        assertThrows(DataValidationException.class, ()
                -> premiumValidator.validateUserPremiumStatus(userId));
    }

    @Test
    void validateBuyPremium_UserDontHavePremium_DoesNotThrowException() {
        long userId = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getUserWithoutPremium()));

        assertDoesNotThrow(() -> premiumValidator.validateUserPremiumStatus(userId));
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .premium(Premium.builder().build())
                .build();
    }

    private User getUserWithoutPremium() {
        return User.builder()
                .id(1L)
                .build();
    }

    private PaymentResponse getFailedResponse() {
        return PaymentResponse.builder().build();
    }

    private PaymentResponse getValidResponse() {
        return PaymentResponse.builder()
                .status(PaymentStatus.SUCCESS)
                .build();
    }
}
