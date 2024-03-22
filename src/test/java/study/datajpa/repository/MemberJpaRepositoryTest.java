package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

  @Autowired
  MemberJpaRepository memberJpaRepository;

  @Test
  public void testMember() throws Exception {
    //given
    Member member = new Member("memberA");

    //when
    Member saveMember = memberJpaRepository.save(member);
    Member findMember = memberJpaRepository.find(saveMember.getId());
    //then
    assertEquals(findMember.getId(),member.getId());
  }
  
  @Test
  public void basicCRUD() throws Exception {
    //given
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    //when
    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
    Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

    List<Member> all = memberJpaRepository.findAll();

    Long count = memberJpaRepository.count();
    //then
    // 단건 조회
    assertEquals(findMember1,member1);
    assertEquals(findMember2,member2);
    // 리스트 조회
    assertEquals(all.size(),2);
    // 카운트 검증
    assertEquals(count,2);
    // 삭제 검증
    memberJpaRepository.delete(member1);
    memberJpaRepository.delete(member2);
    Long deleteCount = memberJpaRepository.count();
    assertEquals(deleteCount,0);


  }

  @Test
  public void findByUsernameAndAgeGreaterThan() {
    Member m1 = new Member("AAA", 10);
    Member m2 = new Member("AAA", 20);
    memberJpaRepository.save(m1);
    memberJpaRepository.save(m2);
    List<Member> result =
        memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
    assertEquals(result.get(0).getUsername(),"AAA");
    assertEquals(result.get(0).getAge(),20);
    assertEquals(result.size(),1);
  }

  @Test
  public void paging() throws Exception {
    //given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 10));
    memberJpaRepository.save(new Member("member3", 10));
    memberJpaRepository.save(new Member("member4", 10));
    memberJpaRepository.save(new Member("member5", 10));
    int age = 10;
    int offset = 0;
    int limit = 3;
    //when
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
    long totalCount = memberJpaRepository.totalCount(age);
    //페이지 계산 공식 적용...
    // totalPage = totalCount / size ...
    // 마지막 페이지 ...
    // 최초 페이지 ..
    //then
    assertEquals(members.size(),3);
    assertEquals(totalCount,5);
  }

  @Test
  public void bulkUpdate() throws Exception {
    //given
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 19));
    memberJpaRepository.save(new Member("member3", 20));
    memberJpaRepository.save(new Member("member4", 21));
    memberJpaRepository.save(new Member("member5", 40));
    //when
    int resultCount = memberJpaRepository.bulkAgePlus(20);
    //then
    assertEquals(resultCount,3);
  }
}