package hufs.ces.rcube.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto { //회원 생성 및 수정 요청에 사용
    private String email;
    private String name;
    private  String password; //회원 생성 시 필요한 필드


}
