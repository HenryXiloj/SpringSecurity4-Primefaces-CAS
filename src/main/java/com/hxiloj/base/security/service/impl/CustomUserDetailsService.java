package com.hxiloj.base.security.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hxiloj.base.security.model.AppUserDetails;


public class CustomUserDetailsService implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

	
	@Override
	public UserDetails loadUserDetails(CasAssertionAuthenticationToken token){
		
		String login = token.getPrincipal().toString();
		String lowercaseLogin = login.toLowerCase();
	
		if (lowercaseLogin != null) {
			List<GrantedAuthority> authorities = buildUserAuthority();
			
			return new AppUserDetails(lowercaseLogin, authorities);
		}
		else
		{
			 throw new UsernameNotFoundException("USUARIO NO EXISTE");
		}
		
	}
	
	
	private List<GrantedAuthority> buildUserAuthority() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
				
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		
		return new ArrayList<GrantedAuthority>(authorities);
	}

}
