package com.jeferson.jecommerce.tests;

import java.time.LocalDate;

import com.jeferson.jecommerce.entities.Role;
import com.jeferson.jecommerce.entities.User;

public class UserFactory {

	public static User createClientUser() {

		User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"),
				"$2a$10$3oWxei.vd8F8iA32BetcOedPSuxtUg2GPE1InPcvcWGm1mbqmYhVq");

		user.addRole(new Role(1L, "ROLE_CLIENT"));

		return user;
	}

	public static User createAdminUser() {

		User user = new User(2L, "Alex Green", "alex@gmail.com", "977777777", LocalDate.parse("1987-12-13"),
				"$2a$10$3oWxei.vd8F8iA32BetcOedPSuxtUg2GPE1InPcvcWGm1mbqmYhVq");

		user.addRole(new Role(2L, "ROLE_ADMIN"));

		return user;
	}// ambos construtores para o mock do setUp

	public static User createCustomClientUser(Long id, String username) {

		User user = new User(id, "Maria", username, "988888888", LocalDate.parse("2001-07-25"),
				"$2a$10$3oWxei.vd8F8iA32BetcOedPSuxtUg2GPE1InPcvcWGm1mbqmYhVq");

		user.addRole(new Role(1L, "ROLE_CLIENT"));

		return user;
	}
	
	public static User createCustomAdminUser(Long id, String username) {
		
		User user = new User(id, "Alex", username, "988888888", LocalDate.parse("2001-07-25"),
				"$2a$10$3oWxei.vd8F8iA32BetcOedPSuxtUg2GPE1InPcvcWGm1mbqmYhVq");

		user.addRole(new Role(1L, "ROLE_CLIENT"));

		return user;
	}
}
