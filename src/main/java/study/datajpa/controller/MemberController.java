package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberRepository memberRepository;

  // 도메인 클래스 컨버터 사용전
  @GetMapping("/members/{id}")
  public String findMember(@PathVariable("id") Long id) {
    Member member = memberRepository.findById(id).get();
    return member.getUsername();
  }

  // 도메인 클래스 컨버터 사용후
  @GetMapping("/members2/{id}")
  public String findMember2(@PathVariable("id") Member member) {
    return member.getUsername();
  }

  @GetMapping("/members")
  public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    Page<MemberDto> dto = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
    return dto;
  }


  @PostConstruct
  public void init() {
    for (int i = 0; i < 100; i++) {
      memberRepository.save(new Member("user" + i, i));
    }

  }
}