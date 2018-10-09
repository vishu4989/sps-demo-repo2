package com.capgemini.model;

import java.util.Date;

public class ResponseUser {

	int Id;
	String email,friend_list,subscription,text_message;
	Date updated_timestamp;



	public ResponseUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseUser(int id, String email, String friend_list, String subscription, String text_message,
			Date updated_timestamp) {
		super();
		Id = id;
		this.email = email;
		this.friend_list = friend_list;
		this.subscription = subscription;
		this.text_message = text_message;
		this.updated_timestamp = updated_timestamp;
	}

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFriend_list() {
		return friend_list;
	}
	public void setFriend_list(String friend_list) {
		this.friend_list = friend_list;
	}
	public String getSubscription() {
		return subscription;
	}
	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}
	public String getText_message() {
		return text_message;
	}
	public void setText_message(String text_message) {
		this.text_message = text_message;
	}
	public Date getUpdated_timestamp() {
		return updated_timestamp;
	}
	public void setUpdated_timestamp(Date updated_timestamp) {
		this.updated_timestamp = updated_timestamp;
	} 



}