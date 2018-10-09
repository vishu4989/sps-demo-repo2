package com.capgemini.repository;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.capgemini.validation.FriendManagementValidation;
import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.ResponseUser;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserEmail;
import com.capgemini.model.UserFriandsListResponse;

@Repository
public class FriendMangmtRepo {

	@Autowired
	FriendManagementValidation fmError;

	@Autowired
	JdbcTemplate jdbcTemplate;



	//public FriendManagementValidation addNewRequest(String requestor , String target){

	public FriendManagementValidation addNewFriendConnection(com.capgemini.model.UserRequest userReq){
		List<UserEmail> friends = userReq.getFriends();

		String requestor = friends.get(0).getEmail();
		String target = friends.get(1).getEmail();

		String query = "SELECT email FROM friendmanagement";

		List<String> emails =jdbcTemplate.queryForList(query,String.class);

		if(emails.contains(requestor) && emails.contains(target)){

				if(getFriendList(requestor)==null && getFriendList(target)==null){
					if(connectFriends(requestor, target)) {
						fmError.setStatus("Success");
						fmError.setErrorDescription("");
					}else {
						fmError.setStatus("Failed");
						fmError.setErrorDescription("");
					}

				}else {
					addRequestorListToTarget(requestor, target);
					addRequestorListToTarget(target, requestor);	
				}

		}else if(emails.contains(requestor)){
			fmError.setStatus("Failed");
			fmError.setErrorDescription("Target Email is not available");

		}else{
			fmError.setStatus("Failed");
			fmError.setErrorDescription("Requestor Email is not available");
		}

		return fmError;

	}

	
	
	public FriendManagementValidation subscribeTargetFriend(com.capgemini.model.Subscriber subscriber)throws ResourceNotFoundException {

		String requestor = subscriber.getRequestor();
		String target = subscriber.getTarget();


		String query = "SELECT email FROM friendmanagement";

		List<String> emails =jdbcTemplate.queryForList(query,String.class);

		if(emails.contains(target) && emails.contains(requestor)) {
			String sql = "SELECT subscription FROM friendmanagement WHERE email=?";

			String subscribers = (String) jdbcTemplate.queryForObject(
					sql, new Object[] { requestor }, String.class);



			int result=0;
			if(subscribers.isEmpty()) {
				result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
						new Object[] {
								target, requestor
				});
			}else {

				String[] subs = subscribers.split(",");
				ArrayList al = new ArrayList(Arrays.asList(subs));
				System.out.println("al "+al);
				if(!al.contains(target)) {
					target= subscribers +", "+ target;
					result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
							new Object[] {
									target, requestor
					});
				}else {
					fmError.setStatus("Failed");
					fmError.setErrorDescription("Target already subscribed");    
				}
			}
			//		String[] subscriberList = subscribers.split(",");






			if(result==1) {
				fmError.setStatus("Success");
				fmError.setErrorDescription("Subscribed successfully");
			}else {
				fmError.setStatus("Failed");
				fmError.setErrorDescription("");
			}
		}else {
			fmError.setStatus("Failed");
			fmError.setErrorDescription("Check Target or Requestor email id");

		}

		return fmError;

	}






	public UserFriandsListResponse retrieveFriendsEmails(String email) throws ResourceNotFoundException {	

		UserFriandsListResponse emailListresponse = new UserFriandsListResponse();
		emailListresponse.setStatus("success");
		emailListresponse.setCount(new Integer(2));
		emailListresponse.getFriends().add("som1@gmail.com");
		emailListresponse.getFriends().add("som2@gmail.com");
		System.out.println("########## " +emailListresponse.getStatus());
		System.out.println("########## " +emailListresponse.getCount());
		System.out.println("########## " +emailListresponse.getFriends().get(0));
		System.out.println("########## " +emailListresponse.getFriends().get(1));
		return emailListresponse;

	}

	private boolean connectFriends(String requestor, String target){
		boolean status = false;

		String sql = "SELECT id FROM friendmanagement WHERE email=?";
		String requestorId = (String) jdbcTemplate.queryForObject(
				sql, new Object[] { requestor }, String.class);


		String targetId = (String) jdbcTemplate.queryForObject(
				sql, new Object[] { target }, String.class);


		int result;
		result = jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] {
						targetId, requestor
		});
		if(result==1){
			result = jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
					new Object[] {
							requestorId, target
			});
			if(result==1) {
				status = true;
			}
		}
		return status;
	}
	
	
	private void addRequestorListToTarget(String firstEmail, String secondEmail){
		String sql = "SELECT id FROM friendmanagement WHERE email=?";
		String requestorId = (String) jdbcTemplate.queryForObject(
				sql, new Object[] { firstEmail }, String.class);
		
		String sqlrFriendList = "SELECT friend_list FROM friendmanagement WHERE email=?";
		String friendList = (String) jdbcTemplate.queryForObject(
				sqlrFriendList, new Object[] { secondEmail }, String.class);
		
		friendList = friendList.isEmpty()?requestorId:friendList+", "+requestorId;
		
		jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] {
						friendList, secondEmail
		}); 
	}

	
	private List<String> getFriendList(String email) {
		List<String> reqFriendsAL = null;
		String requestorfriendListSQL = "SELECT friend_list FROM friendmanagement where email=?";
		String requestorFriends = (String) jdbcTemplate.queryForObject(
				requestorfriendListSQL, new Object[] { email }, String.class);
		if(!requestorFriends.isEmpty()) {
			String[] reqFriends = requestorFriends.split(",");
			reqFriendsAL = (List<String>) Arrays.asList(reqFriends);
		}
		return reqFriendsAL;
	}


}
