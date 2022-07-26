package com.innovation.myblog.service;

import com.innovation.myblog.models.Comment;
import com.innovation.myblog.models.Member;
import com.innovation.myblog.models.Myblog;
import com.innovation.myblog.models.TargetType;
import com.innovation.myblog.repository.CommentRepository;
import com.innovation.myblog.repository.MemberRepository;
import com.innovation.myblog.repository.MyblogRepository;
import com.innovation.myblog.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpSessionRequiredException;

@Slf4j
@RequiredArgsConstructor
@Service
// 좋아요 등록, 취소를 수행하는 서비스
public class LikeService {
    private final MemberService memberService;
    private final MyblogRepository myblogRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Object like(TargetType type, Long typeId, String param) throws IllegalArgumentException, HttpSessionRequiredException {
        String likeParam = param.toUpperCase();
        boolean like = false;

        switch (likeParam) {
            case "LIKE":
                like = true;
                break;
            case "DISLIKE":
                break;
            default:
                throw new IllegalArgumentException(
                        "like param은 'like', 'dislike' 중 하나여야 합니다.");
        }

        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new HttpSessionRequiredException("로그인이 필요합니다"));

        // 게시글(MyBlog) 혹은 댓글(Comment)에 좋아요 추가 후 db에 저장.
        // 좋아요를 누른 사용자에도 대상 게시물, 혹은 댓글의 id 저장.
        switch (type) {
            case COMMENT:
                Comment comment =
                        commentRepository.findById(typeId).orElseThrow(
                                () -> new IllegalArgumentException("해당 id로 " +
                                        "댓글을 찾을 수 없습니다."));
                if (like) {
                    try {
                        comment.addLikedMember(memberId,
                                memberService.getMyInfo().getNickname());
                    } catch (IllegalArgumentException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                    member.addLikedComments(typeId);
                } else {
                    try {
                        comment.removeLikedMember(memberId);
                    } catch (IllegalArgumentException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                    member.removeLikedComments(typeId);
                }
                memberRepository.save(member);
                return commentRepository.save(comment);

            case POST:
                Myblog myblog = myblogRepository.findById(typeId).orElseThrow(
                        () -> new IllegalArgumentException("해당 id로 " +
                                "게시글을 찾을 수 없습니다."));
                if (like) {
                    try {
                        myblog.addLikedMember(memberId,
                                memberService.getMyInfo().getNickname());
                    } catch (IllegalArgumentException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                    member.addLikedMyBlogs(typeId);
                } else {
                    try {
                        myblog.removeLikedMember(memberId);
                    } catch (IllegalArgumentException e) {
                        log.error(e.getMessage());
                        return null;
                    }
                    member.removeLikedMyBlogs(typeId);
                }
                memberRepository.save(member);
                return myblogRepository.save(myblog);
        }
        return null;
    }
}
