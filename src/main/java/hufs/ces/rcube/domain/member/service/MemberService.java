package hufs.ces.rcube.domain.member.service;

import hufs.ces.rcube.domain.member.dto.MemberRequestDto;
import hufs.ces.rcube.domain.member.dto.MemberResponseDto;
import hufs.ces.rcube.domain.member.entity.Member;
import hufs.ces.rcube.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    //회원 생성
    public MemberResponseDto createMember(MemberRequestDto memberRequestDTO){
        Member member = Member.builder()
                .email(memberRequestDTO.getEmail())
                .name(memberRequestDTO.getName())
                .password(memberRequestDTO.getPassword())
                .build();

        Member savedMember = memberRepository.save(member);
        return new MemberResponseDto((savedMember.getId()), savedMember.getEmail(), savedMember.getName());
    }

    //회원 조회 (단건)
    public MemberResponseDto getMemberById(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("회원이 존재하지 않습니다."));
                return new MemberResponseDto(member.getId(), member.getEmail(), member.getName());
    }

    //회원 목록 조회
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(member.getId(), member.getEmail(), member.getName()))
                .collect(Collectors.toList());
    }



}
