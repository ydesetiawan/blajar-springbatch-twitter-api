package com.yd.persistence.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yd.persistence.repository.UserRepository;
import com.yd.persistence.repository.model.UserRole;
import com.yd.security.AppsUserDetails;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		com.yd.persistence.repository.model.User user = userRepository
				.findByUsername(username);
		Set<GrantedAuthority> setAuths = new HashSet<>();
		for (UserRole userRole : user.getUserRole()) {
			setAuths.add(new SimpleGrantedAuthority("ROLE_"
					+ userRole.getRole().getName()));
		}
		Set<GrantedAuthority> authorities = new HashSet<>(setAuths);
		AppsUserDetails userDetails;
		userDetails = new AppsUserDetails(username, user.getPassword(),
				authorities);
		userDetails.setUser(user);
		return userDetails;
	}

}
