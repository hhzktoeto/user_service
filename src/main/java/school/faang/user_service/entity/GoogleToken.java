package school.faang.user_service.entity;

import com.google.api.client.auth.oauth2.StoredCredential;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_token")
public class GoogleToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "oauth_client_id")
    private String oauthClientId;

    @Column(name = "oauth_client_secret")
    private String oauthClientSecret;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expiration_time_milliseconds")
    private Long expirationTimeMilliseconds;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public StoredCredential toStoredCredential() {
        StoredCredential credential = new StoredCredential();
        credential.setAccessToken(this.accessToken);
        credential.setRefreshToken(this.refreshToken);
        credential.setExpirationTimeMilliseconds(this.expirationTimeMilliseconds);
        return credential;
    }
}
