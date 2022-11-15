package springjpa.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springjpa.order.domain.Member;
import springjpa.order.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId).get();
        if(member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }
    }
}