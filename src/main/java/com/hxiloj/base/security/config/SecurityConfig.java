package com.hxiloj.base.security.config;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.hxiloj.base.security.service.impl.CustomUserDetailsService;
import com.hxiloj.base.security.util.CsrfCookieGeneratorFilter;
import com.hxiloj.base.security.util.UtilClass;

/**
 * @author hxiloj
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Bean
	public ServiceProperties serviceProperties()
	{
		ServiceProperties sp = new ServiceProperties();
		sp.setService(UtilClass.getPropertieValue("serviceProperties", SecurityConfig.class));
		sp.setSendRenew(false);
		return sp;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() 
	{
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey(UtilClass.getPropertieValue("casAuthenticationProvider", SecurityConfig.class));
		return casAuthenticationProvider;
	}

	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() 
	{
		return new CustomUserDetailsService();
	}

	@Bean
	public SessionAuthenticationStrategy sessionStrategy() 
	{
		SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
		return sessionStrategy;
	}

	@Bean
	public Saml11TicketValidator casSamlServiceTicketValidator() 
	{
		return new Saml11TicketValidator(UtilClass.getPropertieValue("casSamlServiceTicketValidator", SecurityConfig.class));
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() 
	{
		return new Cas20ServiceTicketValidator(UtilClass.getPropertieValue("casSamlServiceTicketValidator", SecurityConfig.class));
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception 
	{
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
		return casAuthenticationFilter;
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() 
	{
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(UtilClass.getPropertieValue("casAuthenticationEntryPoint", SecurityConfig.class));
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() 
	{
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(UtilClass.getPropertieValue("singleSignOutFilter", SecurityConfig.class));
		return singleSignOutFilter;
	}

	@Bean
	public LogoutFilter requestCasGlobalLogoutFilter() 
	{
		LogoutFilter logoutFilter = new LogoutFilter(UtilClass.getPropertieValue("requestCasGlobalLogoutFilter", SecurityConfig.class), new SecurityContextLogoutHandler());
		 logoutFilter.setFilterProcessesUrl("/logout");
	        return logoutFilter;
	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.authenticationProvider(casAuthenticationProvider());
	}
	
	 @Override
    protected void configure(HttpSecurity http) throws Exception {
		 
		 http.addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
	    	.exceptionHandling()
			.authenticationEntryPoint(casAuthenticationEntryPoint())
			.and().addFilter(casAuthenticationFilter())
			.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
			.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);

		http
			 .authorizeRequests()
			 .antMatchers("/**")
			 .fullyAuthenticated()
		     .and().formLogin()
		    .loginPage("/login")
		    .and().exceptionHandling().accessDeniedPage("/403")
		    .and().logout()
		    .logoutUrl("/logout")
		    .logoutSuccessUrl("/")
		    .invalidateHttpSession(true).permitAll()
		    .deleteCookies("remove")
		    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		    .and().sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		    .and().sessionManagement()
		    .maximumSessions(30)
			.expiredUrl("/login")
			.maxSessionsPreventsLogin(true)
			.sessionRegistry(sessionRegistry());
        

    }
	 
	 
	 @Bean
	    public HttpSessionEventPublisher httpSessionEventPublisher() {
	        return new HttpSessionEventPublisher();
	    }
	    
	    // Work around https://jira.spring.io/browse/SEC-2855
	    @Bean
	    public SessionRegistry sessionRegistry() {
	        SessionRegistry sessionRegistry = new SessionRegistryImpl();
	        return sessionRegistry;
	    }
}

