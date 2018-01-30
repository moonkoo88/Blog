package com.mkcheong.persistence;

import com.mkcheong.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, String> {
}
