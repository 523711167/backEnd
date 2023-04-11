package org.pindaodao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

/**
    //这个两个配置是为了在AuthorizationServerEndpointsConfigurer配置认证器
    //@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(
                User.withUsername("pxxx")
                    .password(passwordEncoder.encode("123"))
                    .authorities("USER").build()
        );
        return inMemoryUserDetailsManager;
    }

    //认证管理器
    //@Bean
    //public AuthenticationManager authenticationManagerBean() throws Exception {
    //    return super.authenticationManagerBean();
    //}
*/
    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/r/r1").hasAnyAuthority("p1")
                .antMatchers("/login*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.inMemoryAuthentication().withUser("pdd").password(passwordEncoder.encode("1234")).roles("123");
    //}

}
