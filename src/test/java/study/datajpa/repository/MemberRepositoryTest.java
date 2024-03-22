package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  @PersistenceContext
  EntityManager em;

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

  @Test
  public void returnType() throws Exception {
    //given
    Member m1 = new Member("AAA", 10);
    Member m2 = new Member("BBB", 20);
    memberRepository.save(m1);
    memberRepository.save(m2);

    List<Member> aaa = memberRepository.findListByUsername("AAA");

  }

  @Test
  public void paging() throws Exception {
    //given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
    //when
    Page<Member> page = memberRepository.findByAge(age, pageRequest);

    Page<MemberDto> toMap = page.map(
        member -> new MemberDto(member.getId(), member.getUsername(), null));

    //then
    List<Member> content = page.getContent();

    long totalElements = page.getTotalElements();

    assertEquals(content.size(),3);  // 페이징하고 가져온 데이터 개수
    assertEquals(page.getTotalElements(),5);  // 페이징 필터전 전체 개수
    assertEquals(page.getNumber(),0);  // 현재 몇번째 페이지
    assertEquals(page.getTotalPages(),2); // 총 페이지 개수
    assertTrue(page.isFirst()); // 현재 첫번째 페이지인가
    assertTrue(page.hasNext());  // 다음 페이지가 있는가
  }

  @Test
  public void bulkUpdate() throws Exception {
    //given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 19));
    memberRepository.save(new Member("member3", 20));
    memberRepository.save(new Member("member4", 21));
    memberRepository.save(new Member("member5", 40));
    //when
    int resultCount = memberRepository.bulkAgePlus(20);
    //then
    assertEquals(resultCount,3);
  }

  @Test
  public void findMemberLazy() throws Exception {
    //given
    //member1 -> teamA
    //member2 -> teamB
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    teamRepository.save(teamA);
    teamRepository.save(teamB);
    memberRepository.save(new Member("member1", 10, teamA));
    memberRepository.save(new Member("member2", 20, teamB));
    em.flush();
    em.clear();
    //when

    List<Member> members = memberRepository.findAll();


  }
  
  
  


}