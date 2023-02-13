package com.smartgroup.smartmoney.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	    clients.inMemory()
	            .withClient("angular")
	            .secret("$2a$10$KmslsTeK6AmPXIgzbt81bukSi2Uip8qUSM0fF.cdBp4ZY4UZi0uWm") // @ngul@r0
	            .scopes("read", "write")
	            .authorizedGrantTypes("password", "refresh_token")
	            .accessTokenValiditySeconds(20)
	            .refreshTokenValiditySeconds(3600 * 24)
	        .and()
	            .withClient("mobile")
	            .secret("$2a$10$5y.4./JdTMPoipXm9VNLbOE.xnXsd0AM7V3xr5byrL0/G0DRERJrq") // m0b1l30
	            .scopes("read")
	            .authorizedGrantTypes("password", "refresh_token")
	            .accessTokenValiditySeconds(20)
	            .refreshTokenValiditySeconds(3600 * 24);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.accessTokenConverter(accessTokenConverter())
			.tokenStore(tokenStore())
			.userDetailsService(userDetailsService)
			.reuseRefreshTokens(false);
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("3032885ba9cd6621bcc4e7d6b6c35c2b");
		return accessTokenConverter;
	}

	// method for opaque token
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//	    security.checkTokenAccess("permitAll()");
//	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
}
