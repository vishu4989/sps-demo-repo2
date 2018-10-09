package com.capgemini.model;

import java.util.ArrayList;
import java.util.List;

public class UserFriandsListResponse {
	
	public String status;
	public int count;
	public List<String> friends = new ArrayList<String>();
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	
	
	
	

}
