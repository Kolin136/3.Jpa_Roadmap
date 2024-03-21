package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.MemberDtoRecord;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @Test
  public void testMember() {
    Member member = new Member("memberA");
    Member savedMember = memberRepository.save(member);
    Member findMember =
        memberRepository.findById(savedMember.getId()).get();
    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());

    Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
  }
  @Test
  public void basicCRUD() {
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberRepository.save(member1);
    memberRepository.save(member2);
    //단건 조회 검증
    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();
    assertEquals(findMember1,member1);
    assertEquals(findMember2,member2);

    //리스트 조회 검증
    List<Member> all = memberRepository.findAll();
    assertEquals(all.size(),2);
    //카운트 검증
    long count = memberRepository.count();
    assertEquals(count,2);
    //삭제 검증
    memberRepository.delete(member1);
    memberRepository.delete(member2);
    long deletedCount = memberRepository.count();
    assertEquals(deletedCount,0);
  }


  @Test
  public void testQuery() throws Exception {
    //given
    Member m1 = new Member("AAA", 10);
    Member m2 = new Member("BBB", 20);
    memberRepository.save(m1);
    memberRepository.save(m2);
    //when
    List<Member> result = memberRepository.findUser("AAA", 10);
    assertEquals(result.get(0),m1);
    //then
  }

  @Test
  public void findUsernameList() throws Exception {
    //given
    Member m1 = new Member("AAA", 10);
    Member m2 = new Member("BBB", 20);
    memberRepository.save(m1);
    memberRepository.save(m2);

    List<String> usernameList = memberRepository.findUsernameList();
    for (String s : usernameList) {
      System.out.println("s = " + s);
    }
  }

  @Test
  public void findMemberDto() throws Exception {
    //given
    Team team = new Team("teamA");
    teamRepository.save(team);

    Member m1 = new Member("AAA", 10);
    m1.setTeam(team);
    memberRepository.save(m1);

    List<MemberDtoRecord> memberDto = memberRepository.findMemberDto2();
    for (MemberDtoRecord dto : memberDto) {
      System.out.println("dto = " + dto);
    }
  }



}