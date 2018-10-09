package com.capgemini.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserEmail {
	
	@NotNull 
	@NotEmpty(message = "{createFriendEmail.notempty}") 
	@Email(message = "{createFriendEmail.valid}")
    @Size(max = 30, message = "{createFriendEmail.size}")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
