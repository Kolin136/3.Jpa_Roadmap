package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl{

  private final EntityManager em;


  public List<Member> findMemberCustom() {
    System.out.println("하하");
    return em.createQuery("select m from Member m where age > :age")
        .setParameter("age", 3)
        .getResultList();
  }
}



