package com.capgemini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.UserFriandsListResponse;
import com.capgemini.repository.FriendMangmtRepo;
import com.capgemini.validation.FriendManagementValidation;

@Service
public class FrientMangmtService {

	@Autowired
	FriendMangmtRepo friendMangmtRepo;

	public FriendManagementValidation addNewFriendConnection(com.capgemini.model.UserRequest userReq) {
		System.out.println("-------333333-------------");
		return friendMangmtRepo.addNewFriendConnection(userReq);
	}

	public FriendManagementValidation subscribeTargetFriend(com.capgemini.model.Subscriber subscriber)throws ResourceNotFoundException {

		//System.out.println("-------2222-------------");
		return friendMangmtRepo.subscribeTargetFriend(subscriber);

	}
	
	
	public UserFriandsListResponse retrieveFriendsEmails(String email) throws ResourceNotFoundException {
		
		return friendMangmtRepo.retrieveFriendsEmails(email);
	}

	
	
	



}
