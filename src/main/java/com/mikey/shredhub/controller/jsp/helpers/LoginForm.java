package com.mikey.shredhub.controller.jsp.helpers;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginForm {
	
	@NotEmpty
	@Size(min = 1, max = 50)
	private String username;
	@NotEmpty
	@Size(min = 1, max = 20)
	private String password;

	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}

}
