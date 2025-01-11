package com.shop.demo.repository;

import com.shop.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

}
