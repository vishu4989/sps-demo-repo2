package com.capgemini.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.UserEmail;
import com.capgemini.model.UserFriandsListResponse;
import com.capgemini.validation.FriendManagementValidation;


@Repository
public class FriendMangmtRepo {

	@Autowired
	FriendManagementValidation fmError;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	public FriendManagementValidation addNewFriendConnection(com.capgemini.model.UserRequest userReq) {
		try {
			List<UserEmail> friends = userReq.getFriends();

			String requestor = friends.get(0).getEmail();
			String target = friends.get(1).getEmail();

			String query = "SELECT email FROM friendmanagement";

			List<String> emails = jdbcTemplate.queryForList(query, String.class);

			if (emails.contains(requestor) && emails.contains(target)) {

				boolean isBlocked = isBlocked(requestor, target);
				if (!isBlocked) {
					if (isAlreadyFriend(requestor, target)) {
						fmError.setStatus("Failed");
						fmError.setErrorDescription("Already friends");
					} else {
						connectFriend(requestor, target);
						connectFriend(target, requestor);
						fmError.setStatus("Success");
						fmError.setErrorDescription("");
					}
				} else {
					fmError.setStatus("Failed");
					fmError.setErrorDescription("target blocked");
				}
			} else if (emails.contains(requestor)) {
				fmError.setStatus("Failed");
				fmError.setErrorDescription("Target Email is not available");

			} else {
				fmError.setStatus("Failed");
				fmError.setErrorDescription("Requestor Email is not available");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fmError;

	}

	public FriendManagementValidation subscribeTargetFriend(com.capgemini.model.Subscriber subscriber)
			throws ResourceNotFoundException {

		String requestor = subscriber.getRequestor();
		String target = subscriber.getTarget();

		String query = "SELECT email FROM friendmanagement";

		List<String> emails = jdbcTemplate.queryForList(query, String.class);

		if (emails.contains(target) && emails.contains(requestor)) {
			String sql = "SELECT subscription FROM friendmanagement WHERE email=?";

			String subscribers = (String) jdbcTemplate.queryForObject(sql, new Object[] { requestor }, String.class);

			int result = 0;
			if (subscribers.isEmpty()) {
				result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
						new Object[] { target, requestor });
			} else {

				String[] subs = subscribers.split(",");
				ArrayList al = new ArrayList(Arrays.asList(subs));
				System.out.println("al " + al);
				if (!al.contains(target)) {
					target = subscribers + ", " + target;
					result = jdbcTemplate.update(
							"update friendmanagement " + " set subscription = ? " + " where email = ?",
							new Object[] { target, requestor });
				} else {
					fmError.setStatus("Failed");
					fmError.setErrorDescription("Target already subscribed");
				}
			}

			if (result == 1) {
				fmError.setStatus("Success");
				fmError.setErrorDescription("Subscribed successfully");
			} else {
				fmError.setStatus("Failed");
				fmError.setErrorDescription("");
			}
		} else {
			fmError.setStatus("Failed");
			fmError.setErrorDescription("Check Target or Requestor email id");

		}

		return fmError;

	}

	public UserFriandsListResponse retrieveFriendsEmails(String email) throws ResourceNotFoundException {

		UserFriandsListResponse emailListresponse = new UserFriandsListResponse();

		String friendList = getFriendList(email);
		if ("".equals(friendList)) {
			emailListresponse.setStatus("success");
			emailListresponse.setCount(0);
		} else {
			System.out.println(friendList);
			String[] friendListQueryParam = friendList.split(",");

			StringJoiner joiner = new StringJoiner(",", "SELECT email FROM friendmanagement WHERE id in (", ")");

			for (String ignored : friendListQueryParam) {
				joiner.add(ignored);
			}
			String query = joiner.toString();

			List<String> friends = (List<String>) jdbcTemplate.queryForList(query, new Object[] {}, String.class);

			emailListresponse.setStatus("success");
			emailListresponse.setCount(friends.size());
			for (String friend : friends) {
				emailListresponse.getFriends().add(friend);
			}
		}
		return emailListresponse;

	}

	/**
	 * This method is invoked to connect friend
	 * 
	 * @param firstEmail
	 * @param secondEmail
	 */
	private void connectFriend(String firstEmail, String secondEmail) {
		String requestorId = getId(firstEmail);
		String friendList = getFriendList(secondEmail);

		friendList = friendList.isEmpty() ? requestorId : friendList + "," + requestorId;

		jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] { friendList, secondEmail });
	}

	/**
	 * This method is invoked to check whether the friend is already connected
	 * 
	 * @param requestor
	 * @param target
	 * @return
	 */
	private boolean isAlreadyFriend(String requestor, String target) {
		boolean alreadyFriend = false;

		String requestorId = getId(requestor);
		String targetId = getId(target);

		String requestorFriendList = getFriendList(requestor);
		String[] requestorFriends = requestorFriendList.split(",");

		String targetFirendList = getFriendList(target);
		String[] targetFriends = targetFirendList.split(",");

		if (Arrays.asList(requestorFriends).contains(targetId) && Arrays.asList(targetFriends).contains(requestorId)) {
			alreadyFriend = true;
		}
		System.out.println("alreadyFriend " + alreadyFriend);
		return alreadyFriend;

	}

	/**
	 * this method is invoked to get Id of particular email
	 * 
	 * @param email
	 * @return
	 */
	private String getId(String email) {
		String sql = "SELECT id FROM friendmanagement WHERE email=?";
		String requestorId = (String) jdbcTemplate.queryForObject(sql, new Object[] { email }, String.class);
		return requestorId;
	}

	/**
	 * This method is invoked to get the list of friends
	 * 
	 * @param email
	 * @return
	 */
	private String getFriendList(String email) {
		String sqlrFriendList = "SELECT friend_list FROM friendmanagement WHERE email=?";
		String friendList = (String) jdbcTemplate.queryForObject(sqlrFriendList, new Object[] { email }, String.class);

		System.out.println("++++++++" + friendList);
		return friendList;

	}

	/**
	 * This method is invoked to check whether the target is blocked or not
	 * 
	 * @param requestor_email
	 * @param target_email
	 * @return
	 */
	private boolean isBlocked(String requestor_email, String target_email) {
		boolean status = false;
		try {
			String sqlrFriendList = "SELECT Subscription_Status FROM unsubscribe WHERE Requestor_email=? AND Target_email=?";
			String Subscription_Status = (String) jdbcTemplate.queryForObject(sqlrFriendList,
					new Object[] { requestor_email, target_email }, String.class);
			System.out.println("Subscription_Status " + Subscription_Status);
			if (Subscription_Status.equalsIgnoreCase("Blocked")) {
				status = true;
			}
		} catch (Exception e) {

		}
		return status;
	}
}
