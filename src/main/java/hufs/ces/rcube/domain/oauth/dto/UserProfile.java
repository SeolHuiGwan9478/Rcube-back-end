package hufs.ces.rcube.domain.oauth.dto;

import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class UserProfile { //Oauth서버별로가져올 수 있는 유저 정보 다름
    private final String oauthId;
    private final String email;
    private final String name;
    private final String imageUrl;

    public Member toMember() {
        return Member.builder()
                .oauthId(oauthId)
                .email(email)
                .name(name)
                .imageUrl(imageUrl)
                .role(Role.GUEST)
                .build();
    }


}
