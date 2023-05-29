package com.project.oag.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.oag.repository.UserRepository;

@Service("userDetailsService")
@Transactional
public class UserDetailService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginAttemptService loginAttemptService;

	public UserDetailService() {
		super();
	}
	
//
//	// @Override
//	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//		  	boolean enabled = true;
//		    boolean accountNonExpired = true;
//		    boolean credentialsNonExpired = true;
//		    boolean accountNonLocked = true;
//		if (loginAttemptService.isBlocked()) {
//			throw new RuntimeException("blocked");
//		}
//
//		try {
//			final Optional<User> user = userRepository.findByEmail(email);
//			if (user == null) {
//				throw new UsernameNotFoundException("No user found with username: " + email);
//			}
//
//			  return new org.springframework.security.core.userdetails.User(
//			          user.getEmail(), 
//			          user.getPassword().toLowerCase(), 
//			          user.isEnabled(), 
//			          accountNonExpired, 
//			          credentialsNonExpired, 
//			          accountNonLocked,null);
//		} catch (final Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	// UTIL
/*
	private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(final Collection<Role> roles) {
		final List<String> privileges = new ArrayList<>();
		final List<Privilege> collection = new ArrayList<>();
		for (final Role role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		for (final Privilege item : collection) {
			privileges.add(item.getName());
		}

		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
		final List<GrantedAuthority> authorities = new ArrayList<>();
		for (final String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}*/

}
