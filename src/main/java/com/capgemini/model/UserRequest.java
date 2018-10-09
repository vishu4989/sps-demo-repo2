package com.capgemini.model;

import java.util.List;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {
	
	   
	@NotNull
	@NotEmpty(message = "{friendArray.notempty}")
	@Size(max = 2, message = "{friendArraySize.size}")
	
	private List<UserEmail> friends;
	//private String emailFirst;
	//private String emailSecond;

	public List<UserEmail> getFriends() {
		return friends;
	}

	public void setFriends(List<UserEmail> friends) {
		this.friends = friends;
	}
	
	
	
	/*public String getEmailFirst() {
		return emailFirst;
	}
	public void setEmailFirst(String emailFirst) {
		this.emailFirst = emailFirst;
	}
	public String getEmailSecond() {
		return emailSecond;
	}
	public void setEmailSecond(String emailSecond) {
		this.emailSecond = emailSecond;
	}*/

	

	
	
	
	

}
