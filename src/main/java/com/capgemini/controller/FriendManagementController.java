package com.capgemini.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.BaseResponse;
import com.capgemini.model.UserFriandsListResponse;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;

/**
 * @author vishwman
 *
 */
@RestController
@RequestMapping(value = "/test")
public class FriendManagementController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final String sharedKey = "SHARED_KEY";
	private static final String SUCCESS_STATUS = "success";
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;

	@Autowired
	public FrientMangmtService frndMngtServc;

	@Autowired
	FriendManagementValidation fmError;


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> newFriendConnection(@Valid @RequestBody com.capgemini.model.UserRequest userReq, BindingResult result)throws ResourceNotFoundException {
		LOG.info("====logger test 1234=======");
		LOG.debug("====logger test =======");
		BaseResponse response = new BaseResponse();
//		ResponseEntity<BaseResponse> re = null;
		ResponseEntity<FriendManagementValidation> responseEntity = null;
		try{
			
			FriendManagementValidation fmv =frndMngtServc.addNewFriendConnection(userReq);
			if(fmv.getStatus().equalsIgnoreCase("success")) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			}else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		 
		}catch(Exception e) {
			
//			re =  new ResponseEntity<BaseResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);
			
		} 
		
		return responseEntity;


	}

	
	/**
	 * @param subscriber
	 * @param result
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/subscribe", method = RequestMethod.PUT)
	public ResponseEntity<FriendManagementValidation> subscribeFriend(@Valid @RequestBody com.capgemini.model.Subscriber subscriber, BindingResult result)throws ResourceNotFoundException {
      System.out.print("In Subscribe");
      LOG.info("====logger test 1234=======");
		LOG.debug("====logger test =======");
		//Validation
		if(result.hasErrors()) {
			return handleValidation(result);
		}

		ResponseEntity<FriendManagementValidation> responseEntity = null;

		try {
			FriendManagementValidation fmv =frndMngtServc.subscribeTargetFriend(subscriber);
			if(fmv.getStatus().equalsIgnoreCase("success")) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			}else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {

		}

		return responseEntity;

	}


	/**
	 * This method is used for client validation
	 * @param result
	 * @return
	 */
	private ResponseEntity<FriendManagementValidation> handleValidation(BindingResult result) {
		fmError.setStatus("Failed");
		if(result.getFieldError("requestor") != null && result.getFieldError("target") != null) {
			fmError.setErrorDescription(result.getFieldError("requestor").getDefaultMessage()+" "+result.getFieldError("target").getDefaultMessage());
		}else if(result.getFieldError("target") != null) {
			fmError.setErrorDescription(result.getFieldError("target").getDefaultMessage());
		}else{
			fmError.setErrorDescription(result.getFieldError("requestor").getDefaultMessage());

		}
		return new ResponseEntity<FriendManagementValidation>(fmError, HttpStatus.BAD_REQUEST);

	}
	
	
	/*@RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.GET)
	  public Shipwreck get(@PathVariable Long id){
	    return ShipwreckStub.get(id);
	  }
	  @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.PUT)
	  public Shipwreck update(@PathVariable Long id, @RequestBody Shipwreck shipwreck){
	    return ShipwreckStub.update(id, shipwreck);
	  }
	  @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.DELETE)
	  public Shipwreck delete(@PathVariable Long id){
	    return ShipwreckStub.delete(id);
	  }*/
	
	  
	//public ResponseEntity<UserFriandsListResponse> RetrieveFriendList(@Email @Valid @PathVariable String emailid)throws ResourceNotFoundException {
	@RequestMapping(value = "/friends/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserFriandsListResponse> RetrieveFriendList(@Email @Valid @PathVariable String id)throws ResourceNotFoundException {	
		//public ResponseEntity<UserFriandsListResponse> RetrieveFriendList(@PathVariable String id )throws ResourceNotFoundException {
		System.out.println("---------------" +id);
		UserFriandsListResponse response = frndMngtServc.retrieveFriendsEmails(id );
		ResponseEntity<UserFriandsListResponse> responseEntity = null;
		System.out.println("---------------");
		if(response.getStatus() == SUCCESS_STATUS){
			response.setStatus(SUCCESS_STATUS);
			responseEntity = new ResponseEntity<UserFriandsListResponse>(response, HttpStatus.OK);
		} else {
			response.setStatus(ERROR_STATUS);
			responseEntity = new ResponseEntity<UserFriandsListResponse>(response, HttpStatus.BAD_REQUEST);
			//response.setCode(AUTH_FAILURE);
		}
		return responseEntity;
		


	}
	
	
	

}
