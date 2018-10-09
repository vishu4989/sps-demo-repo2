package com.capgemini.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.model.ResponseUser;
import com.capgemini.model.Subscriber;
import com.capgemini.model.User;

@RestController
public class FriendController {
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value="/add/{friend}", method = RequestMethod.POST)
	public boolean addFriend(@PathVariable("friend") String friend, @RequestBody User user) {
		System.out.println(user.getUser_email());
		return true;
	}
	
	@RequestMapping(value="/add/{id}")
	public ResponseUser findFriend(@PathVariable("id") String id) {
		 return jdbcTemplate.queryForObject("select * from friendmanagement where id=?", new Object[] {
		            id
		        },
		        new BeanPropertyRowMapper < ResponseUser > (ResponseUser.class));
	}
	
	@RequestMapping(value="/subscribe", method = RequestMethod.PUT)
	public int subscribeFriend(@RequestBody Subscriber subscriber) {
		 System.out.print(subscriber.getRequestor());
		 System.out.print(subscriber.getTarget());
		 
		 return jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
			        new Object[] {
			        		subscriber.getTarget(), subscriber.getRequestor()
		 });
		 
	}

}
