package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Member;

@SpringBootTest
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
  
  

}