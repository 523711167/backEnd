package org.pindaodao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private ClientDetailsService clientDetailsService;

    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 令牌端点de安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //oauth/token_key是公开
                .tokenKeyAccess("permitAll()")
                //oauth/check_token公开
                .checkTokenAccess("permitAll()")
                //表单认证（申请令牌）
                .allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    /**
     * ClientDetailsServiceConfigurer是Spring Security中的一个配置类，用于配置客户端详细信息服务。
     * ClientDetailsServiceConfigurer提供了一些配置方法，以便我们在应用程序中配置客户端详细信息服务，
     * 包括客户端ID、客户端密钥、访问令牌有效期等等。
     * 通过这些方法，我们可以实现不同客户端的差别化配置，以满足不同客户端的授权需求。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("pxx")
                .secret(passwordEncoder().encode("12345"))
                //可以访问de资源客户端
                .resourceIds("oauth-resource")
                // 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
                .authorizedGrantTypes(
                        "authorization_code",
                        "password",
                        "client_credentials",
                        "implicit",
                        "refresh_token")
                // 允许的授权范围
                .scopes("all")
                //配置客户端de权限,基于spring-securityde权限
                .authorities("USER")
                //false跳转到授权页面
                .autoApprove(true)
                //加上验证回调地址
                .redirectUris("http://www.baidu.com");
        //自定义加载客户端管理
        //clients.withClientDetails()
    }


    //@Autowired
    //private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * //token存储的方式
     * //InMemoryTokenStore
     * //RedisTokenStore
     * //JbdcTokenStore
     * //JwtTokenStore
     *
     * @Bean public TokenStore inMemoryTokenStore()
     * {
     * return new InMemoryTokenStore();
     * }
     */


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConvert());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConvert() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }


    //配置令牌服务 令牌储存方式
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        //配置对应de客户端
        service.setClientDetailsService(clientDetailsService);
        //Token刷新
        service.setSupportRefreshToken(true);
        //Token存储方式
        service.setTokenStore(tokenStore());
        service.setTokenEnhancer(accessTokenConvert());
        // 令牌默认有效期
        service.setAccessTokenValiditySeconds(7200);
        //刷新令牌有效期
        service.setRefreshTokenValiditySeconds(259200);
        // 刷新令牌默认有效期3天
        return service;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //设置授权码模式的授权码如何存取，暂时采用内存方式
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置令牌de地址  令牌de服务
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.authenticationManager(authenticationManager)//认证管理器
                .userDetailsService(userDetailsService)//password模式使用
                .authorizationCodeServices(authorizationCodeServices())//授权码模式
                .tokenServices(tokenService())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }
}
