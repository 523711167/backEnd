package org.pindaodao.admin.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //org.pindaodao.admin.entity.User user = new org.pindaodao.admin.entity.User();
        //return  new User(user.getUsername(), user.getPassword(), user.getEnabled(),
        //        user.getAccountNonExpired(), user.getCredentialsNonExpired(),
        //        user.getAccountNonLocked(), null);
        return  new User("pxx", passwordEncoder.encode("1234"), true,
                true, true,
                true, Collections.singletonList(new SimpleGrantedAuthority("123")));
    }


}
