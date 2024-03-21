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
  
  

}