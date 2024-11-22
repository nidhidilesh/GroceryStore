package com.grocery.GroceryStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.grocery.GroceryStore.entity.User;
import com.grocery.GroceryStore.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		if(userRepository.count()==0) {
			createAdminUser();
			createRegularUser(2);
		}
		
	}

	/*This method will create users*/
	private void createRegularUser(int numberOfUsers) {
		for(int i = 0; i < numberOfUsers; i++) {
			User user = new User();
			user.setUsername("user" + i);
			user.setPassword(new BCryptPasswordEncoder(4).encode("userpassword" + i));
			user.getRoles().add("USER");
			userRepository.save(user);
		}
		
	}

	/*This method will create admin*/
	private void createAdminUser() {
		User adminUser = new User();
		adminUser.setUsername("admin1");
		adminUser.setPassword(new BCryptPasswordEncoder(4).encode("adminpassword"));
		adminUser.getRoles().add("ADMIN");
		userRepository.save(adminUser);
		
	}

}
