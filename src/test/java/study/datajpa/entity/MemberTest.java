package study.datajpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {
  @PersistenceContext
  EntityManager em;

  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("멤버 테스트")
  public void testEntity() throws Exception {
    //given
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);
    Member member3 = new Member("member3", 30, teamB);
    Member member4 = new Member("member4", 40, teamB);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);

    em.flush();
    em.clear();

    List<Member> members = em.createQuery("select m from Member m", Member.class)
        .getResultList();

    for (Member member : members) {
      System.out.println("member = " + member);
      System.out.println("-> member.team = " + member.getTeam());
    }
    //when

    //then
  }
  
  @Test
  @DisplayName("")
  public void JpaEventBaseEntity() throws Exception {
    //given
    Member member = new Member("member1");
    memberRepository.save(member);

    Thread.sleep(100);
    member.setUsername("member2");

    em.flush();
    em.clear();
    //when
    Member findMember = memberRepository.findById(member.getId()).get();
    //then
    System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
    System.out.println("findMember.getUpdatedDate() = " + findMember.getUpdatedDate());
  }
  
  


}