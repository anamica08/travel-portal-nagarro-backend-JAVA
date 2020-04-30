package com.nagarro.travelportal.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nagarro.travelportal.model.Employee;

@Service
public class JwtUserDetailService implements UserDetailsService {

	@Autowired
	EmployeeService empService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if ("admin@nagarro.com".equals(username)) {//1234
			return new User("admin@nagarro.com", "$2y$12$ofMQ1e3QEo/1sunlirdJXOZMim1rT8rIdMZhnrDxy9xuAOKosnhee",
					new ArrayList<>());
			
		}
		
		//for user other than admin.
		Employee user = empService.getEmployeeByUsername(username);
		
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
		

	}
}
