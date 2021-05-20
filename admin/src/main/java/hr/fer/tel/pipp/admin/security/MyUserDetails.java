package hr.fer.tel.pipp.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hr.fer.tel.pipp.admin.model.Role;
import hr.fer.tel.pipp.admin.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private Role role;
	
	public MyUserDetails(User user) {
		email = user.getEmail();
		password = user.getPassword();
		role = user.getRole();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		System.out.println(role.toString());
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { return true; }

	@Override
	public boolean isEnabled() { return true; }

}
