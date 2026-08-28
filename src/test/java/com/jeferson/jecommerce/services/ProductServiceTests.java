package com.jeferson.jecommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jeferson.jecommerce.dto.ProductDTO;
import com.jeferson.jecommerce.dto.ProductMinDTO;
import com.jeferson.jecommerce.entities.Product;
import com.jeferson.jecommerce.repositories.ProductRepository;
import com.jeferson.jecommerce.services.exceptions.DatabaseException;
import com.jeferson.jecommerce.services.exceptions.ResourceNotFoundException;
import com.jeferson.jecommerce.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private long existingProductId, nonExistingProductId, dependentProductId;

	private Product product;

	private String productName;

	private PageImpl<Product> page;

	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {

		existingProductId = 1L;
		nonExistingProductId = 2L;
		dependentProductId = 3L;

		productName = "Console Playstation 5";

		product = ProductFactory.createProduct(productName);

		page = new PageImpl<>(List.of(product));

		productDTO = new ProductDTO(product);

		Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());
		Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);
		Mockito.when(repository.save(any())).thenReturn(product);
		Mockito.when(repository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.existsById(existingProductId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingProductId)).thenReturn(false);// aqui dispara recurso nao
																					// encontrado para delete id nao
																					// existente
		Mockito.when(repository.existsById(dependentProductId)).thenReturn(true);

		Mockito.doNothing().when(repository).deleteById(existingProductId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentProductId);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {

		ProductDTO result = service.findById(existingProductId);

		Assertions.assertNotNull(result);

		Assertions.assertEquals(result.getId(), existingProductId);

		Assertions.assertEquals(result.getName(), product.getName());
	}

	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingProductId);
		});
	}

	@Test
	public void findAllShouldReturnPagedProductMinDTO() {

		Pageable pageable = PageRequest.of(0, 12);

		// MinDTO pois tras uma versao mais enxuta de porductDTO sem categorias e
		// descricoes
		Page<ProductMinDTO> result = service.findAll(productName, pageable);

		Assertions.assertNotNull(result);

		Assertions.assertEquals(result.getSize(), 1);

		Assertions.assertEquals(result.iterator().next().getName(), productName);
	}

	@Test
	public void insertShouldReturnProductDTO() {

		ProductDTO result = service.insert(productDTO);

		Assertions.assertNotNull(result);

		Assertions.assertEquals(result.getId(), product.getId());

		// Verifica se o save do repository foi invocado 1 vez
		Mockito.verify(repository, Mockito.times(1)).save(any());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {

		ProductDTO result = service.update(existingProductId, productDTO);

		Assertions.assertNotNull(result);

		Assertions.assertEquals(result.getId(), existingProductId);

		Assertions.assertEquals(result.getName(), productDTO.getName());

		// Verifica se o save do repository foi invocado 1 vez
		Mockito.verify(repository, Mockito.times(1)).save(any());
	}

	@Test
	public void updateShoulddReturnResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {

			service.update(nonExistingProductId, productDTO);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {

			service.delete(existingProductId);
		});

		// Verifica se o metodo deleteById do repository foi chamado exatamente 1 vez com o existingId
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingProductId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {

			service.delete(nonExistingProductId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdExistsAndDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {

			service.delete(dependentProductId);
		});
	}
}
