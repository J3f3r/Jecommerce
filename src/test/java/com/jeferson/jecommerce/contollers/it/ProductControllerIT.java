package com.jeferson.jecommerce.contollers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.jeferson.jecommerce.dto.ProductDTO;
import com.jeferson.jecommerce.entities.Category;
import com.jeferson.jecommerce.entities.Product;
import com.jeferson.jecommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	private String adminToken, clientToken, invalidToken;
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String productName;
	
	private Product product;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		
		productName = "MacBook";
		
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		invalidToken = adminToken + "xpto"; // simula wrong passwor
		
		Category category = new Category(2L, "Eletro");
		
		product = new Product(null, "Console PlayStation 5", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. ", 3999.90, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
		
		product.getCategories().add(category);
		
		productDTO = new ProductDTO(product);
	}
	
	@Test
	public void fidAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/products?name={productName}", productName)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());// .ok
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}
	
	@Test
	public void fidAllShouldReturnPageWhenNameParamIsEmpty() throws Exception{
		
		ResultActions result = mockMvc
				.perform(get("/products")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());// .ok
		result.andExpect(jsonPath("$.content[0].id").value(1));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
	}
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLogged() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		// comaparacoes
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L)); // proxima produto inserido
		result.andExpect(jsonPath("$.name").value("Console PlayStation 5"));
		result.andExpect(jsonPath("$.description").value("Lorem ipsum, dolor sit amet consectetur adipisicing elit. "));
		result.andExpect(jsonPath("$.price").value(3999.90));
		result.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		
		result.andExpect(jsonPath("$.categories[0].id").value(2L));
	}
	
	// cenarios para 422 UnprocessableEntity
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLoggedAndInvalidName() throws Exception{
		
		product.setName("ab");
		
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLoggedAndInvalidDescription() throws Exception{
		
		product.setDescription("ab");
		
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLoggedAndPriceIsNegative() throws Exception{
		
		product.setPrice(-50.0);
		
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLoggedAndPriceIsZero() throws Exception{
		
		product.setPrice(0.0);
		
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShoulReturnProductDTOCreatedWhenAdminLoggedAndHasNotCategory() throws Exception{
		
		product.getCategories().clear();// limpa as categorias
		
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
						.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldRetuenForbiddenWhenClientLogged() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + clientToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + invalidToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
