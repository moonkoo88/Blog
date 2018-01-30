package com.mkcheong.security;

import com.mkcheong.persistence.MemberRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Log
public class MkUsersService implements UserDetailsService {

    @Autowired
    MemberRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return
                repo.findById(username)
                        .filter(m -> m != null)
                        .map(m -> new MkSecurityUser(m)).get();

    }

}
