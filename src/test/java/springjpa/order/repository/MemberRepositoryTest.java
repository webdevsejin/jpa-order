package springjpa.order.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository repoitory;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Transactional  // @springframework에 있는 transaction을 선택해야 함.
                    // 이 내용을 빼면 No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call 에러 발생함
                    // test case 에 transactional 이 있으면 테스트 완료 후 rollback 하기 때문에 데이터가 db에 저장되지는 않음
    @Rollback(false) // 이 어노테이션을 주게 되면 롤백하지 않고 db에 저장된다.
    @Test
    public void testMember() {
        //given
        Member member = new Member();
        member.setUsername("spring");
        //when
        Long saveId = repoitory.save(member);
        Member findMember = repoitory.findById(saveId);
        //then
        // 같은 transaction에서 같은 id 로 저장하고 찾은 것은
        // 영속성 context에 의해서 같은 엔티티로 식별하므로 member와 findMember는 동일하다.
        // 이런경우 select 문으로 db에서 가져오는게 아니라, 캐시에서 꺼내온다.
        System.out.println("member == findMember ==>" + (member == findMember));
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void findById() {
    }
}