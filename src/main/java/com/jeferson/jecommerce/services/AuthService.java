package com.jeferson.jecommerce.services;

import org.springframework.stereotype.Service;

import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	private final UserService userService;

	// Construtor explícito para Injeção de Dependência pelo Spring
	public AuthService(UserService userService) {
		this.userService = userService;
	}
	
	// metodo que verifica se o eu estou logado e sou admin que vai ser chamado na User entity e no pedido em service
	public void validateSelfOrAdmin(Long userId) {
		User me = userService.authenticated();
		
//		// quebrar em 2 ifs para fins de testes
//		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
//			throw new ForbiddenException("Access denied");
//		}
		
		if (me.hasRole("ROLE_ADMIN")) {
			return;
		}
		
		if (!me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied. Should be self or Admin.");
		}
	}
}
