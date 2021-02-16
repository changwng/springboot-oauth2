package com.example.oauth2.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final DataSource dataSource; // (1) 토큰에 대한 영속화를 진행하기 위해서 Datasoruce 의존성을 주입받습니다.

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource) // (5) clinet inMemory() 방식에서 jdbc() 방식으로 변경합니다. 의존성은 dataSource 주입해줍니다.
        ;
//                .inMemory() //(1): 간단한 설정을 위해 메모리에 저장시키겠습니다.
//                .withClient("client") //(2): 클라이언트 이름을 작성합니다.
//                .secret("{bcrypt}$2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  // password//(3): 시크릿을 작성해야합니다. 스프링 시큐리티 5.0 부터는 암호화 방식이 조 변경되서 반드시 암호화해서 저장하는 것을 권장합니다. 해당 암호는 password입니다. (현재 프로젝트는 스프링부트 2.1 이기 때문에 스프링 시큐리티 5.1 의존성을 주입받습니다.)
//                .redirectUris("http://localhost:9000/callback") //(4): 리다이렉트 URI을 설정합니다.
//                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials", "refresh_token")//
//                .accessTokenValiditySeconds(120)
//                .refreshTokenValiditySeconds(240)
//                .scopes("read_profile");
    }


    /* 1) Authorization Code Grant Type 방식
      @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory() // (1)
                .withClient("client") //(2)
                .secret("{bcrypt} $2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  //(3) password
                .redirectUris("http://localhost:9000/callback") // (4)
                .authorizedGrantTypes("authorization_code") // (5)(5): Authorization Code Grant 타입을 설정합니다.
                .scopes("read_profile"); // (6)
    }
    */
    /* 2) Implicit Grant 방식
        @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("{bcrypt}$2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  // password
                .redirectUris("http://localhost:9000/callback")
                .authorizedGrantTypes("authorization_code", "implicit") // (1) implicit 추가
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240)
                .scopes("read_profile");
    }
    응답 타입이 token 일 경우 암시적 승인 타입에 해당합니다
    */
    /* 3) Resource Owner Password Credentials Grant 방식
    public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("{bcrypt}$2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  // password
                .redirectUris("http://localhost:9000/callback")
                .authorizedGrantTypes("authorization_code", "implicit", "password") // (1) password 타입 추가
                .accessTokenValiditySeconds(120) //  access token 만료시간
                .refreshTokenValiditySeconds(240) // refresh token 만료시간
                .scopes("read_profile");
    }


    //(2)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
                //@formatter:off
                endpoints
                        .authenticationManager(authenticationManager) // Bean 등록은 SecurityConfig 에서 등록합니다.
                ;
                //@formatter:on
            }
        }
    
      4) Client Credentials Grant Type 방식
         @Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
            .inMemory()
            .withClient("client")
            .secret("{bcrypt}$2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  // password
            .redirectUris("http://localhost:9000/callback")
            .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials") // client_credentials 추가
            .accessTokenValiditySeconds(120)
            .refreshTokenValiditySeconds(240)
            .scopes("read_profile");
}
    // 코드의 변경사항은 client_credentials 변경 뿐입니다.
    리소스 주인이 어떤 권한인증을 하지 않기 때문에 Refresh Token을 넘겨주지 않는것이 바람직합니다.
    5) Refresh Token Grant Type 방식

      @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("client")
                .secret("{bcrypt}$2a$10$iP9ejueOGXO29.Yio7rqeuW9.yOC4YaV8fJp3eIWbP45eZSHFEwMG")  // password
                .redirectUris("http://localhost:9000/callback")
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials", "refresh_token") // refresh_token 추가
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240)
                .scopes("read_profile");
    }
        */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //@formatter:off
        endpoints  //(4) 2,3 번에서 생성한 객체을 AuthorizationServerEndpointsConfigurer 객체에 넣어줍니다.
                .approvalStore(approvalStore())
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
        ;
        //@formatter:on
        /* endpoints
            .accessTokenConverter(jwtAccessTokenConverter())
            .userDetailsService(userDetailsService);
            */
    }
    @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
        {
        oauthServer.checkTokenAccess("isAuthenticated()");    
        }

    @Bean
    public TokenStore tokenStore() { //(2) 주입 받은 Datasoruce 의존성을 기반으로 JdbcTokenStore을 생성합니다.
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() { //(3) 2번과 마찬가지로 Datasoruce을 주입시켜 JdbcApprovalStore을 생성합니다
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
