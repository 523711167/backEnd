package org.pindaodao.config;

import org.pindaodao.plugin.CustomDefaultUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class    ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String resource;

    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resource).tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**")
                .access("#oauth2.hasScope('all')")
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //配置access_token远程验证策略。
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:2778/oauth/check_token");
        services.setClientId("pxx");
        services.setClientSecret("12345");
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new CustomDefaultUserAuthenticationConverter());
        services.setAccessTokenConverter(accessTokenConverter);
        return services;
    }

}


