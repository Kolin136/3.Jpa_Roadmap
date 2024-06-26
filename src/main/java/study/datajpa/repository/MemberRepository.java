package study.datajpa.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.MemberDtoRecord;
import study.datajpa.dto.UsernameOnlyDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>,MemberRepositoryCustom {

  List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username")String username,@Param("age") int age);

  @Query("select m.username from Member m")
  List<String> findUsernameList();


  @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
      "from Member m join m.team t")
  List<MemberDto> findMemberDto();

  @Query("select new study.datajpa.dto.MemberDtoRecord(m.id, m.username, t.name) " +
      "from Member m join m.team t")
  List<MemberDtoRecord> findMemberDto2();

  @Query("select m from Member m where m.username = :name")
  Member findMembers(@Param("name") String username);

  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") List<String> names);

  List<Member> findListByUsername(String name); //컬렉션
  Member findMemberByUsername(String name); //단건
  Optional<Member> findOptionalByUsername(String name); //단건 Optiona

  @Query(value = "select m from Member m left join m.team t")
  Page<Member> findByAge(int age, Pageable pageable);

  @Modifying
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();

  @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);


  @Query(value = "SELECT m.member_id as id, m.username, t.name as teamName " +
      "from member m left join team t",
      countQuery = "SELECT count(*) from member",
      nativeQuery = true)
  Page<MemberProjection> findByNativeProjection(Pageable pageable);


}