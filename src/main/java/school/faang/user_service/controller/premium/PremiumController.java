package school.faang.user_service.controller.premium;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.premium.PremiumDto;
import school.faang.user_service.entity.premium.PremiumPeriod;
import school.faang.user_service.service.premium.PremiumService;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Premium", description = "Endpoints for managing premium access")
public class PremiumController {

    private final PremiumService premiumService;
    private final UserContext userContext;

    @Operation(summary = "Buy premium for user with days")
    @PostMapping("/user/premium/{days}")
    public PremiumDto buyPremium(@PathVariable @Positive(message = "Days quantity can't be less than 1") Integer days) {
        return premiumService.buyPremium(userContext.getUserId(), PremiumPeriod.fromDays(days));
    }
}
