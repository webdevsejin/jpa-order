package springjpa.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springjpa.order.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository  // 등록해놓으면 component scan에 의해 스프링빈으로 자동 등록됨
@RequiredArgsConstructor
public class MemberRepository {
//    jpa를 사용하는 경우에는 Entity manager가 필요함\
//    작성 후 Control + shift + T 를 누르면 자동으로 테스트 코드 생성됨.
//    entity manager를 통한 명령은 항상 transaction 안에서 이루어져야 한다.
    //@PersistenceContext // spring이 entity manger를 만들어서 injection 해준다.
    //@requiredArgsConstructor를 사용하면 PersistenceContext 또는 autowired를 사용한 것과 동일한 작업을 자동으로 해준다.
    private final EntityManager em;

//    public MemberRepository(EntityManager em){
//        this.em = em;
//    }

    public Long save(Member member){ // void 로 해도 관계 없음
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id){ // Member 로 리턴하면 ?? 못 찾는 경우 nullpointexception 발생 가능성 있음.
        // member를 찾아서 반환해줌, optional 이 리스트구조이므로 한건의 데이터를 가져오려면 get메소드 이용
        return Optional.ofNullable(em.find(Member.class ,id)).get();
    }

    public List<Member> findAll(){  //JPQL과 sql은 약간의 차이는 있지만 기능적으로는 거의 동일함.
                                    // sql은 테이블을 대상으로 쿼리를 진행하는데 반해 ,JPQL은 엔티티 객체에 대해 쿼리를 진행한다.
        return em.createQuery("select m from Member m", Member.class) // sql과 반환타입을 args로 전달
                .getResultList();
    }

    public Optional<Member> findByLoginId(String loginId){ // :param 으로 바인딩해서 특정 로그인아이디를 가지는 회원 정보만 조회
         return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList()
                .stream()
                .filter(m->m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findByName(String username){ // :param 으로 바인딩해서 특정 이름을 가지는 회원 정보만 조회
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByEmail(String email){ // :param 으로 바인딩해서 특정 email을 가지는 회원 정보만 조회
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }
}

