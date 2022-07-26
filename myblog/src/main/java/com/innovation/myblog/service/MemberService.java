package com.innovation.myblog.service;

import com.innovation.myblog.dto.MemberResponseDto;
import com.innovation.myblog.repository.MemberRepository;
import com.innovation.myblog.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(MemberResponseDto::of)
                .orElseThrow(
                        () -> new RuntimeException("유저 정보가 없습니다")
                );
    }

    //현재 SecurityContext에 있는 유저 정보 가져오기기
   @Transactional
    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
