package com.jeferson.jecommerce.contollers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeferson.jecommerce.dto.OrderDTO;
import com.jeferson.jecommerce.entities.Order;
import com.jeferson.jecommerce.entities.OrderItem;
import com.jeferson.jecommerce.entities.OrderStatus;
import com.jeferson.jecommerce.entities.Product;
import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.tests.ProductFactory;
import com.jeferson.jecommerce.tests.TokenUtil;
import com.jeferson.jecommerce.tests.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	private String adminToken, clientToken, invalidToken;
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private Long existingOrderId, nonExistingOrderId;
	
	private User user;
	
	private Product product;
	
	private Order order;
	private OrderDTO orderDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		
		existingOrderId = 1L;
		nonExistingOrderId = 100L;

		
		
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		invalidToken = adminToken + "xpto"; // simula wrong passwor
		
		user = UserFactory.createClientUser();
		
		order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);
		
		product = ProductFactory.createProduct();
		
		//agora vou incluir o produto com item do pedido
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
	}
	
	// testar os 6 cenarios para o findById
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.items[1].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void findByIdShouldReturnForbiddenIdExistsAndClientLoggedAndOrderDoesNotBelongueUser() throws Exception{// pedido nao pertence ao user logado com client
		
		Long otherOrderId = 2L;
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", otherOrderId)
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingOrderId)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundIdDoesNotExistAndClientLogged() throws Exception{// pedido nao pertence ao user logado com client
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingOrderId)
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnunauthorizedWhenIdExistsAndInvalidToken() throws Exception{// pedido nao pertence ao user logado com client
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingOrderId)
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
