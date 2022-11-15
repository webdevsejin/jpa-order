package springjpa.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.Member;
import springjpa.order.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // rollback 처리를 위한 부분 , @Rollback(false)로 주면 rollback 하지 않고 db에 직접 저장할 수 있다.
// MemberService 에서 control + shift + T 를 누르면 자동으로 테스트코드를 생성할 수 있다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository repository;
    @Autowired EntityManager em;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    // 중요한 주요 기능 2가지를 테스트해보려고 한다.
    // 라이브템플릿(ex.tdd) 만들어서 적용해보기
    // 기능1 . 회원가입
    @Test
    public void join() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("spring1");
        member.setEmail("a@mail.com");
        // when
        Long id = memberService.join(member);
        // then
        assertThat(member.getId()).isEqualTo(id);
    }

    // 기능2. 중복회원예외
    @Test
    public void validateDuplicateMember() {
        // findOne()
        // given
        Member member = new Member();
        member.setUsername("spring1");
        member.setEmail("a@mail.com");

        Member member1 = new Member();
        member1.setUsername("spring2");
        member1.setEmail("b@mail.com");
        // when
        Long id = memberService.join(member);
        //try { } catch () { } 대신에 아래와 같이 예외처리해주는 것이 좋다.
        assertThrows(IllegalStateException.class, () -> {
            Long id1 = memberService.join(member1); // 예외가 발생될 것으로 예상되는 명령어
        });
        // then
        Assertions.fail("예외가 발생해야 합니다."); // 위에서 에러가 발생하고 이쪽으로 흐르면 안될때 체크할 목적으로 사용가능
    }
}