package com.shop.demo.service;

import com.shop.demo.dto.MemberFormDto;
import com.shop.demo.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원가입 테스트")
  void saveMemberTest(){
    //given
    Member member = createMember();

    //when
    Member savedMember = memberService.saveMember(member);

    //then
    assertThat(member).isEqualTo(savedMember);
  }

  @Test
  @DisplayName("중복 회원 가입 테스트")
  void saveDuplicateMemberTest(){
    //given
    Member member1 = createMember();
    Member member2 = createMember();

    //when
    memberService.saveMember(member1);

    //then
    assertThatThrownBy(() -> memberService.saveMember(member2))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("이미 가입된 회원입니다.");
  }

  private Member createMember(){
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail("test@email.com");
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 마포구 합정동");
    memberFormDto.setPassword("1234");

    return Member.createMember(memberFormDto, passwordEncoder);
  }

}