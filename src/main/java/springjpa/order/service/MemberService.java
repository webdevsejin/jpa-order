package springjpa.order.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.Member;
import springjpa.order.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service  // 등록해놓으면 component scan에 의해 스프링빈으로 자동 등록됨
@Transactional(readOnly = true) // spring에서 제공하는 어노테이션 사용 - jpa의 모든 데이터 변경이나 로직이 가급적이면 transaction 안에서 이루어지는 것이 좋다.
                                // transaction 에 조회가 많으면 true 로 놓고 수정이 필요한 곳에는 readOnly=false로 별도 지정하면 됨.
@RequiredArgsConstructor // final이 있는 필드를 가지고 생성자를 자동으로 만들어주는 Lombok 어노테이션
public class MemberService {
    //@Autowired // 해당 어노테이션에 의해 injection 된다. setter 인젝션은 맵 동작중에 변경되는 것을 방지하기 위해 잘 권장하지 않는 방식임
    private final MemberRepository repository ;

//==> requiredArgsConstructor 를 사용하면 이 부분 자동 생성해줌
//    @Autowired // 생성자를 이용한 인젝션 방식을 더 권장한다. 또한 생성자가 하나만 있을 때는 @Autowired 생략해도 자동으로 인젝션해준다.
//    public MemberService(MemberRepository repository){
//        this.repository = repository;
//    }
    /**
    * 핵심기능 1 - 회원가입
    */
    @Transactional  // readOnly=false 임. 쓰기에는 readOnly = true 옵션을 주면 데이터 변경이 안된다.
    public Long join(Member member){
        validateDuplicateLoginId(member); // 중복회원이 있는지 검증하기는 하나, 동시생성시에는 못걸러줄수도 있으므로 db의 필드 속성에 unique를 걸어주는 것이 좋다.
        validateDuplicateMember(member); // 중복회원이 있는지 검증하기는 하나, 동시생성시에는 못걸러줄수도 있으므로 db의 필드 속성에 unique를 걸어주는 것이 좋다.
        return repository.save(member);
    }

    public void validateDuplicateMember(Member member){
        List<Member> findMembers = repository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()){ // 동일한 이메일이 존재한다면 exception 처리
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public void validateDuplicateLoginId(Member member){
        Optional<Member> findMember = repository.findByLoginId(member.getLoginId());
        if (!findMember.isEmpty()){ // 동일한 로그인아이디가 존재한다면 exception 처리
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    /**
     * 핵심기능 2 - 회원 조회(전체/한건)
     */
    //@Transactional(readOnly = true) // 조회하는 곳에서는 이렇게 설정하면 성능이 최적화된다.
    public List<Member> findMembers(){
        return repository.findAll();
    }

    //@Transactional(readOnly = true) // 조회하는 곳에서는 이렇게 설정하면 성능이 최적화된다.
    public Member findOne(Long id){
        return repository.findById(id);
    }
}
