package hr.fer.tel.pipp.admin.security;

public class AuthenticationResponse {
	
	private final String jwt;
	private String role;
	
	public AuthenticationResponse(String jwt, String role) {
		this.jwt = jwt;
		this.role = role;
	}

	public String getJwt() {
		return jwt;
	}

	public String getRole() {
		return role;
	}

}
