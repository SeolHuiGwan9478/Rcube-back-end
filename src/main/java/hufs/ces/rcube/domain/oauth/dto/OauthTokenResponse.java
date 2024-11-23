package hufs.ces.rcube.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class OauthTokenResponse { //OAuth 서버와의 통신을 통해 엑세스 토큰을 받아올 dto설정
    @JsonProperty("access_token")
    private String accessToken;

    private String scope;

    @JsonProperty("token_type")
    private String tokenType;


}