package com.jeferson.jecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class JecommerceApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(JecommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// esse metodo gera a senha embaralhada pelo BCrypt para ser usado depois
		System.out.println("Encode = " + passwordEncoder.encode("123456"));
		
		boolean result = passwordEncoder.matches("123456", "$2a$10$3oWxei.vd8F8iA32BetcOedPSuxtUg2GPE1InPcvcWGm1mbqmYhVq");
		System.out.println("RESULTADO = " + result);
	}

}
