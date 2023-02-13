package com.smartgroup.smartmoney.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartgroup.smartmoney.model.User;
import com.smartgroup.smartmoney.repository.UserRepository;

@Service
@Primary
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByEmail(email);
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User and/or password incorrect!"));
		
		return new org.springframework.security.core.userdetails.User(email, user.getPassword(), getPermissions(user));
	}

	private Collection<? extends GrantedAuthority> getPermissions(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getPermissions()
			.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getDescription().toUpperCase())));
		return authorities;
	}

}
