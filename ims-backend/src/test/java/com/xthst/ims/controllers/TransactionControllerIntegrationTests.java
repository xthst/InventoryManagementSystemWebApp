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

import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionDto;
import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.domain.entities.TransactionEntity;
import com.xthst.ims.exceptions.ResourceNotFoundException;
import com.xthst.ims.mappers.ProductMapper;
import com.xthst.ims.mappers.TransactionMapper;
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
public class TransactionControllerIntegrationTests {
	
	private final MockMvc mockMvc;
	
	private final TransactionMapper transactionMapper;
	private final ProductMapper productMapper;
	private final ObjectMapper objectMapper;
	
	private final TransactionRepository transactionRepo;
	private final ProductRepository productRepo;
	
	private EntityManager entityManager;
	
	@Autowired
	public TransactionControllerIntegrationTests(MockMvc mockMvc,
			TransactionRepository transactionRepo,
			ProductRepository productRepo,
			TransactionMapper transactionMapper,
			ProductMapper productMapper,
			EntityManager entityManager) {
		this.mockMvc = mockMvc;
		this.transactionMapper = transactionMapper;
		this.productMapper = productMapper;
		this.entityManager = entityManager;
		this.transactionRepo = transactionRepo;
		this.productRepo = productRepo;
		this.objectMapper = new ObjectMapper();
	}
	
	// Get Transaction
	@Test
	public void testThatGetTransactionSuccessfullyReturnsHttp200SuccessAndReturnsTheCorrectTransaction() throws Exception {
		ProductEntity productEntity = productRepo.save(TestDataUtil.createProductEntityB());
		TransactionEntity createdTransaction = transactionRepo.save(TestDataUtil.createTransactionEntityA(productEntity));
		entityManager.flush();
		entityManager.clear();
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/transaction/" + createdTransaction.getId())
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		TransactionDto responseDto = objectMapper.readValue(responseJson, TransactionDto.class);
		
		TransactionEntity transactionEntity = transactionRepo.findById(responseDto.getId()).orElseThrow();
		TransactionDto transactionDto = transactionMapper.toDto(transactionEntity);
		
		assertThat(transactionEntity).isEqualTo(createdTransaction);
		assertThat(responseDto).isEqualTo(transactionDto);
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	@Test
	public void testThatGetTransactionSuccessfullyReturnsHttp404NotFoundIfTransactionDoesNotExist() throws Exception{
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/transaction/0")
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertTransactionNotFoundResponse(responseJson);
	}
	
	// Get Transactions
	@Test
	public void testThatGetTransactionsSuccessfullyReturnsHttp200SuccessAndReturnsAListOfTransactions() throws Exception{
		ProductEntity productA = productRepo.save(TestDataUtil.createProductEntityA());
		ProductEntity productB = productRepo.save(TestDataUtil.createProductEntityB());
		
		List<TransactionEntity> createdTransactions = List.of(
				TestDataUtil.createTransactionEntityA(productA),
				TestDataUtil.createTransactionEntityB(productB)
		);
		
		createdTransactions.forEach(transaction -> {
			transactionRepo.save(transaction);
		});
		entityManager.flush();
		entityManager.clear();
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/transactions")
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		List<TransactionDto> responseObj = objectMapper.readValue(responseJson, new TypeReference<List<TransactionDto>>() {});
		
		List<TransactionEntity> returnedTransactions = transactionRepo.findAll();
		assertThat(returnedTransactions).isNotEmpty();
		List<TransactionDto> transactionDtoList = new ArrayList<TransactionDto>();
		
		returnedTransactions.forEach(transaction -> {
			transactionDtoList.add(transactionMapper.toDto(transaction));
		});
		
		assertThat(returnedTransactions)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(createdTransactions);
		
		assertThat(responseObj)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(transactionDtoList);
		
		assertThat(responseObj).hasSize(createdTransactions.size());
	}
	
	@Test
	public void testThatGetProductSuccessfullyReturnsHttp200SuccessAndReturnsAnEmptyList() throws Exception {
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/products")
		).andExpect(
				MockMvcResultMatchers.status().isOk()
		).andReturn();
				
		String responseJson = result.getResponse().getContentAsString();
		List<TransactionDto> responseObj = objectMapper.readValue(responseJson, new TypeReference<List<TransactionDto>>() {});
				
		assertThat(responseObj).hasSize(0);
	}
	
	// Create Transaction
	@Test
	public void testThatCreateTransactionSuccessfullyReturnsHttp201CreatedAndThatProductAndTransactionTablesAreUpdated() throws Exception {
		ProductEntity productEntity = TestDataUtil.createProductEntityA();
		ProductEntity createdProduct = productRepo.save(productEntity);
		entityManager.flush();
		entityManager.clear();
		
		TransactionCreateRequest createRequest = TestDataUtil.createValidTransactionCreateRequestA(createdProduct.getId());
		String transactionJson = objectMapper.writeValueAsString(createRequest);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/transaction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(transactionJson)
		).andExpect(
				MockMvcResultMatchers.status().isCreated()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		TransactionDto responseDto = objectMapper.readValue(responseJson, TransactionDto.class); 
		
		TransactionEntity transactionEntity = transactionRepo.findById(responseDto.getId()).orElseThrow();
		TransactionDto transactionDto = transactionMapper.toDto(transactionEntity);
		
		assertThat(responseDto).isEqualTo(transactionDto);
		
		ProductDto responseProduct = responseDto.getProduct();
		System.out.print("This: " + responseProduct);
		ProductEntity updatedProduct = productRepo.findById(responseProduct.getId()).orElseThrow();
		int newQuantity = productEntity.getQuantityOnHand() + createRequest.getQuantity();
		
		assertThat(updatedProduct.getQuantityOnHand()).isEqualTo(newQuantity);
		assertThat(responseProduct).isEqualTo(productMapper.toDto(updatedProduct));
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(1L);
	}
	
	@Test
	public void testThatCreateTransactionSuccessfullyReturnsHttp400BadRequestWithInvalidRequestAndRelatedTablesAreUnmodified() throws Exception {
		ProductEntity productEntity = TestDataUtil.createProductEntityA();
		ProductEntity createdProduct = productRepo.save(productEntity);
		entityManager.flush();
		entityManager.clear();
		
		TransactionCreateRequest createRequest = TestDataUtil.createInvalidTransactionCreateRequest(createdProduct.getId());		
		String invalidJson = objectMapper.writeValueAsString(createRequest);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/transaction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(invalidJson)
		).andExpect(
				MockMvcResultMatchers.status().isBadRequest()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		Map<String, String> expectedMap = Map.of(
			"productId", TransactionCreateRequest.PRODUCT_ID_NULL_ERROR,
			"transactionDate", TransactionCreateRequest.TRANSACTION_DATE_NULL_ERROR,
			"transactionType", TransactionCreateRequest.TRANSACTION_TYPE_NULL_ERROR,
			"quantity", TransactionCreateRequest.QUANTITY_VALUE_ERROR
		);
		assertThat(responseMap).isEqualTo(expectedMap);
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(0L);
		
		ProductEntity currentProduct = productRepo.findById(productEntity.getId()).orElseThrow();
		
		assertThat(currentProduct).isEqualTo(productEntity);
	}
	
	@Test
	public void testThatCreateTransactionSuccessfullyReturnsHttp404NotFoundWhenProductIsNonExistent() throws Exception {		
		TransactionCreateRequest createRequest = TestDataUtil.createValidTransactionCreateRequestB(1L);		
		String validJson = objectMapper.writeValueAsString(createRequest);
				
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/transaction")
					.contentType(MediaType.APPLICATION_JSON)
					.content(validJson)
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		Map<String, String> errorMap = new HashMap<>();
		String errorMessage = new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, 1L).getMessage();
		errorMap.put("errorMessage", errorMessage);
		
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		assertThat(responseMap).isEqualTo(errorMap);
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(0L);
	}

	// Delete Transaction
	@Test
	public void testThatDeleteTransactionSuccessfullyReturnsHttp204NoContentAndIsDeletedFromTheDatabase() throws Exception {
		ProductEntity productEntity = TestDataUtil.createProductEntityA();
		TransactionEntity existingTransaction = transactionRepo.save(TestDataUtil.createTransactionEntityA(productEntity));
		
		String deleteProductJson = objectMapper.writeValueAsString(existingTransaction);
		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/transaction/" + existingTransaction.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(deleteProductJson)
		).andExpect(
				MockMvcResultMatchers.status().isNoContent()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertThat(responseJson).isEmpty();
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(0L);	
	}
	
	@Test
	public void testThatDeleteTransactionSuccessfullyReturnsHttp404NotFoundIfTheTransactionDoesNotExist() throws Exception {		
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/transaction/0")
		).andExpect(
				MockMvcResultMatchers.status().isNotFound()
		).andReturn();
		
		String responseJson = result.getResponse().getContentAsString();
		assertTransactionNotFoundResponse(responseJson);
		
		long count = transactionRepo.count();
		assertThat(count).isEqualTo(0L);
	}
	
	private void assertTransactionNotFoundResponse(String responseJson){
		Map<String, String> errorMap = new HashMap<>();
		String errorMessage = new ResourceNotFoundException(ResourceNotFoundException.TRANSACTION, 0L).getMessage();
		errorMap.put("errorMessage", errorMessage);
		
		Map<String, String> responseMap = objectMapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});
		
		assertThat(responseMap).isEqualTo(errorMap);
	}
	
	
}
