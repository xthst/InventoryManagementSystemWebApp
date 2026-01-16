package com.xthst.ims.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;
import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.domain.entities.TransactionEntity;
import com.xthst.ims.exceptions.ProductLinkedToTransactionException;
import com.xthst.ims.exceptions.ResourceNotFoundException;
import com.xthst.ims.mappers.ProductMapper;
import com.xthst.ims.repositories.ProductRepository;
import com.xthst.ims.repositories.TransactionRepository;
import com.xthst.ims.util.TestDataUtil;

import jakarta.persistence.EntityManager;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTests {
	
	private final MockMvc mockMvc;
	private final ProductMapper productMapper;
	
	private final ProductRepository productRepo;
	private final TransactionRepository transactionRepo;
	private final ObjectMapper objectMapper;
	
	private final EntityManager entityManager;
	
	@Autowired
	public ProductControllerIntegrationTests(MockMvc mockMvc,
			ProductRepository productRepo,
			TransactionRepository transactionRepo,
			ProductMapper productMapper,
			EntityManager entityManager) {
		this.mockMvc = mockMvc;
		this.productMapper = productMapper;
		this.productRepo = productRepo;
		this.transactionRepo = transactionRepo;
		this.entityManager = entityManager;
		this.objectMapper = new ObjectMapper();
	}
	
	// Get Product
	@Test
	public void testThatGetProductSuccessfullyReturnsHttp200SuccessAndReturnsTheCorrectProduct() throws Exception {
		ProductEntity createdProduct = productRepo.save(TestDataUtil.createProductEntityA());
		entityManager.flush();
		entityManager.clear();
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/product/" + createdProduct.getId())
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		ProductDto responseDto = objectMapper.readValue(responseJson, ProductDto.class);
		
		ProductEntity productEntity = productRepo.findById(responseDto.getId()).orElseThrow();
		ProductDto productDto = productMapper.toDto(productEntity);
		
		assertThat(productEntity).isEqualTo(createdProduct);
		assertThat(productDto).isEqualTo(responseDto);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	@Test
	public void testThatGetProductSuccessfullyReturnsHttp404NotFoundIfTheProductDoesNotExist() throws Exception {
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/product/0")
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertJsonNotFoundResponse(responseJson);
	}
	
	//Get Products
	@Test
	public void testThatGetProductsSuccessfullyReturnsHttp200SuccessAndReturnsAListOfProducts() throws Exception {
		List<ProductEntity> productEntities = List.of(
				TestDataUtil.createProductEntityA(),
				TestDataUtil.createProductEntityB(),
				TestDataUtil.createProductEntityC()
		);
		
		productEntities.forEach(product -> {
			productRepo.save(product);
		});
		entityManager.flush();
		entityManager.clear();
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/products")
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		List<ProductDto> responseObject = objectMapper.readValue(responseJson, new TypeReference<List<ProductDto>>() {});
		
		List<ProductEntity> returnedProducts = productRepo.findAll();
		assertThat(returnedProducts).isNotEmpty();
		
		List<ProductDto> productDtoList = new ArrayList<ProductDto>();
		
		returnedProducts.forEach(productEntity -> {
			productDtoList.add(productMapper.toDto(productEntity));
		});
		
		assertThat(returnedProducts)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(productEntities);
		
		assertThat(responseObject)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(productDtoList);
		
		assertThat(responseObject).hasSize(productEntities.size());
	}
	
	@Test
	public void testThatGetProductsSuccessfullyReturnsHttp200SuccessAndReturnsAnEmptyList() throws Exception {
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/products")
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		List<ProductDto> productDtoList = objectMapper.readValue(responseJson, new TypeReference<List<ProductDto>>() {});
		
		assertThat(productDtoList).hasSize(0);
	}
	
	// Create Product
	@Test
	public void testThatCreateProductSuccessfullyReturnsHttp201CreatedAndIsCreatedInDatabase() throws Exception {
		ProductCreateRequest createRequest = TestDataUtil.createValidProductCreateRequestA();		
		String productJson = objectMapper.writeValueAsString(createRequest);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		).andReturn();
		entityManager.flush();
		entityManager.clear();
		
		String responseJson = result.getResponse().getContentAsString();
		ProductDto responseDto = objectMapper.readValue(responseJson, ProductDto.class);
		
		ProductEntity productEntity = productRepo.findById(responseDto.getId()).orElseThrow();
		ProductDto productDto = productMapper.toDto(productEntity);
		
		assertThat(responseDto).isEqualTo(productDto);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	@Test
	public void testThatCreateProductSuccessfullyReturnsHttp400BadRequestWithInvalidRequestAndDatabaseIsEmpty() throws Exception {
		ProductCreateRequest createRequest = TestDataUtil.createInvalidProductCreateRequest();		
		String invalidJson = objectMapper.writeValueAsString(createRequest);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(invalidJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		Map<String, String> expectedMap = Map.of(
			"name", ProductCreateRequest.NAME_BLANK_ERROR,
			"unitOfMeasure", ProductCreateRequest.UNIT_OF_MEASURE_BLANK_ERROR,
			"reorderPoint", ProductCreateRequest.REORDER_POINT_VALUE_ERROR,
			"quantityOnHand", ProductCreateRequest.QUANTITY_VALUE_ERROR
		);
		assertThat(responseMap).isEqualTo(expectedMap);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(0L);
	}
	
	// Edit Products
	@Test
	public void testThatEditProductSuccessfullyReturnsHttp200SuccessAndIsUpdatedInTheDatabase() throws Exception {
		ProductEntity existingProduct = productRepo.save(TestDataUtil.createProductEntityA());
		entityManager.flush();
		entityManager.clear();
		
		existingProduct.setName("New edited name");
		existingProduct.setDescription("New edited description");
		existingProduct.setQuantityOnHand(123);
		String editedProductJson = objectMapper.writeValueAsString(existingProduct);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.put("/product/" + existingProduct.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(editedProductJson)
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		ProductDto responseDto = objectMapper.readValue(responseJson, ProductDto.class);
		
		ProductEntity productEntity = productRepo.findById(responseDto.getId()).orElseThrow();
		ProductDto productDto = productMapper.toDto(productEntity);
		
		assertThat(responseDto).isEqualTo(productDto);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	@Test
	public void testThatEditProductSuccessfullyReturnsHttp404NotFoundIfTheProductDoesNotExist() throws Exception {
		ProductUpdateRequest updateRequest = ProductUpdateRequest.builder()
				.name("New edited name")
				.build();
		String updateRequestJson = objectMapper.writeValueAsString(updateRequest);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.put("/product/0")
					.contentType(MediaType.APPLICATION_JSON)
					.content(updateRequestJson)
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertJsonNotFoundResponse(responseJson);
	}
	
	// Delete Products
	@Test
	public void testThatDeleteProductSuccessfullyReturnsHttp204NoContentAndIsDeletedFromTheDatabase() throws Exception {
		ProductEntity existingProduct = productRepo.save(TestDataUtil.createProductEntityA());
		
		String deleteProductJson = objectMapper.writeValueAsString(existingProduct);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/product/" + existingProduct.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(deleteProductJson)
		).andExpect(
				MockMvcResultMatchers.status().isNoContent()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertThat(responseJson).isEmpty();
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(0L);
	}
	
	@Test
	public void testThatDeleteProductSuccessfullyReturnsHttp404NotFoundIfTheProductDoesNotExist() throws Exception {		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/product/0")
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertJsonNotFoundResponse(responseJson);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(0L);
	}
	
	@Test
	public void testThatDeleteProductSuccessfullyReturnsHttp409ConflictIfATransactionReferencesTheProductToBeDeleted() throws Exception {
		ProductEntity existingProduct = productRepo.save(TestDataUtil.createProductEntityA());
		transactionRepo.save(TestDataUtil.createTransactionEntityA(existingProduct));
		entityManager.flush();
		entityManager.clear();
		
		String deleteProductJson = objectMapper.writeValueAsString(existingProduct);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/product/" + existingProduct.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(deleteProductJson)
		).andExpect(
				MockMvcResultMatchers.status().isConflict()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		
		Map<String, String> errorMap = new HashMap<>();
		String errorMessage = new ProductLinkedToTransactionException().getMessage();
		errorMap.put("errorMessage", errorMessage);
		
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		assertThat(responseMap).isEqualTo(errorMap);
		
		long count = productRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	
	private void assertJsonNotFoundResponse(String responseJson){
		Map<String, String> errorMap = new HashMap<>();
		String errorMessage = new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, 0L).getMessage();
		errorMap.put("errorMessage", errorMessage);
		
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		assertThat(responseMap).isEqualTo(errorMap);
	}
	
	
}
