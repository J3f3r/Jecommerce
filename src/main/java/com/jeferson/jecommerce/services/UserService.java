package com.jeferson.jecommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeferson.jecommerce.dto.UserDTO;
import com.jeferson.jecommerce.entities.Role;
import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.projections.UserDetailsProjection;
import com.jeferson.jecommerce.repositories.UserRepository;
import com.jeferson.jecommerce.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService{

//	//@Autowired
//	private UserRepository repository;	
//	private CustomUserUtil customUserUtil;
	
	private final UserRepository repository;
	private final CustomUserUtil customUserUtil;

	// Construtor explícito para Injeção de Dependências
	public UserService(UserRepository repository, CustomUserUtil customUserUtil) {
		this.repository = repository;
		this.customUserUtil = customUserUtil;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}// essa implementacao vai evitar o problema do Eazy Load
	
	protected User authenticated() {// aqui foi simplificado usando uma classe auxiliar para ser testado facilmente
		try {
			String username = customUserUtil.getLoggedUsername();
			
			return repository.findByEmail(username).get();
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}

}
