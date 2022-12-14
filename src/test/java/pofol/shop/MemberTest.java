package pofol.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pofol.shop.domain.Member;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.business.MemberService;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;


    @Test
    public void joinTest()throws Exception{
        Member member1 = new Member("member1", "1234");
        memberService.signUp(member1);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findById(member1.getId());
        assertThat(findMember.get()).isEqualTo(member1);
    }

    @Test()
    public void duplicateTest()throws Exception{
        Member member1 = new Member("member1","1234");
        Member member2 = new Member("member2","1234");
        memberService.signUp(member1);
        em.flush();
        em.clear();
        assertThatThrownBy(() -> memberService.signUp(member2)).isInstanceOf(IllegalStateException.class);
    }
}
