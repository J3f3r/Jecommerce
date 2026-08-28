package com.jeferson.jecommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jeferson.jecommerce.dto.OrderDTO;
import com.jeferson.jecommerce.entities.Order;
import com.jeferson.jecommerce.entities.OrderItem;
import com.jeferson.jecommerce.entities.Product;
import com.jeferson.jecommerce.entities.User;
import com.jeferson.jecommerce.repositories.OrderItemRepository;
import com.jeferson.jecommerce.repositories.OrderRepository;
import com.jeferson.jecommerce.repositories.ProductRepository;
import com.jeferson.jecommerce.services.exceptions.ForbiddenException;
import com.jeferson.jecommerce.services.exceptions.ResourceNotFoundException;
import com.jeferson.jecommerce.tests.OrderFactory;
import com.jeferson.jecommerce.tests.ProductFactory;
import com.jeferson.jecommerce.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

	@InjectMocks
	private OrderService service;
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;
	
	private Long existingOrderId, nonExistingOrderId;
	private Long existingProductId, nonExistingProductId;
	private Order order;
	private OrderDTO orderDTO;
	private User admin, client;
	private Product product;
	
	@BeforeEach
	void SetUp() throws Exception{
		
		existingOrderId = 1L;
		nonExistingOrderId = 2L;
		
		existingProductId = 1L;
		nonExistingProductId = 2L;
		
		admin = UserFactory.createCustomAdminUser(1L, "Jef");
		client = UserFactory.createCustomClientUser(2L, "Bob");
		
		order = OrderFactory.createOrder(client);
		orderDTO = new OrderDTO(order);
		
		product = ProductFactory.createProduct();
		
		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.save(any())).thenReturn(order);
		
		Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
	}
	
	@Test// sucesso
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
		// mock feito dentro do teste quando o metodo tem outros cenarios
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		OrderDTO result = service.findById(existingOrderId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
		
		// verifica se os metodos foram chamados pelo menos 1 vez
		Mockito.verify(repository, Mockito.times(1)).findById(existingOrderId);
		Mockito.verify(authService, Mockito.times(1)).validateSelfOrAdmin(any());
	}
	
	@Test// sucesso
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		OrderDTO result = service.findById(existingOrderId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
		
		Mockito.verify(repository, Mockito.times(1)).findById(existingOrderId);
		Mockito.verify(authService, Mockito.times(1)).validateSelfOrAdmin(any());
	}
	
	@Test// falha
	public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
		
		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ForbiddenException.class, () -> {
			
			@SuppressWarnings("unused")
			OrderDTO result = service.findById(existingOrderId);
		});
	}
	
	@Test// falha
	public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Mockito.doThrow(ResourceNotFoundException.class).when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			
			@SuppressWarnings("unused")
			OrderDTO result = service.findById(nonExistingOrderId);
		});
	}
	
	@Test// sucesso admin logado
	public void insertShouldReturnOrderDTOWhenAdminLogged() {
		// aqui dentro simulamos o comportamento do authenticated()
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		OrderDTO result = service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
	}
	
	@Test// sucesso client logado
	public void insertShouldReturnOrderDTOWhenClientLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		OrderDTO result = service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
		
		// verificacoes de persistencia
		Mockito.verify(userService, Mockito.times(1)).authenticated();
		Mockito.verify(repository, Mockito.times(1)).save(any());
		Mockito.verify(orderItemRepository, Mockito.times(1)).saveAll(any());
	}
	
	@Test// falha usuario invalid
	public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {
		
		Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
		
		// vamos instanciar um usuario que nao esta cadastrado no BD
		order.setClient(new User());
		
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			
			@SuppressWarnings("unused")
			OrderDTO result = service.insert(orderDTO);
		});
	}
	
	@Test// falha id de produto inexistente no bd
	public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {
		
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		// agora simulamos o comportamento onde adicionamos itens ao produto com id inexistente
		product.setId(nonExistingProductId);// muda o id para um inexistente
		
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		// instanciamos novos itens e adicionamos ao id inexistente que foi modificado acima
		
		// agora vamos instanciar orderDTO que eh o objeto a ser testado
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			
			@SuppressWarnings("unused")
			OrderDTO result = service.insert(orderDTO);
		});		
	}
}
