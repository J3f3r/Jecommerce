package com.jeferson.jecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	
	// metodo que verifica se o eu estou logado e sou admin que vai ser chamado na User entity e no pedido em service
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}
}
