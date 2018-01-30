package com.mkcheong.security;

import com.mkcheong.domain.Member;
import com.mkcheong.domain.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MkSecurityUser extends User {

    private static final String ROLE_PREFIX = "ROLE_";

    private Member member;

    private String memberStr;

    public MkSecurityUser(Member member) {

        super(member.getUid(), member.getUpw(), makeGrantedAuthority(member.getRoles()));

        this.member = member;

        this.memberStr = member.getUid();
    }

    private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles) {

        List<GrantedAuthority> list = new ArrayList<>();

        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())));

        return list;
    }

}
