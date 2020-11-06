package com.omnirio.accountservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnirio.accountservice.entities.Account;
import com.omnirio.accountservice.repositories.AccountRepository;

import reactor.core.publisher.Mono;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepo;

	@Value("${base.url}")
	private String baseUrl;

	@SuppressWarnings("unchecked")
	public Account saveAccount(Account account) throws JsonMappingException, JsonProcessingException {
		WebClient client = WebClient.builder().baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
		String response = client.get().uri("/users/" + account.getCustomerName() + "/username").retrieve()
				.bodyToMono(String.class).block();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> res = mapper.readValue(response, Map.class);
		if (res.get("userId") != null) {
			long customerId = Long.valueOf(res.get("userId") != null ? res.get("userId") : "0");
			account.setCustomerId(customerId);

		} else {
			Map<String, String> customerObj = new HashMap<>();
			customerObj.put("userName",account.getCustomerName());
			customerObj.put("roleName","Customer");
			Mono<ClientResponse> postResult =client.post().uri("/users").body(Mono.just(customerObj),Map.class).exchange();
			postResult.block();
		}

		return accountRepo.save(account);
	}

	public Account updateAccount(Account account, long id) {
		Account response = accountRepo.findById(id).orElse(null);
		if (response != null) {
			response.setAccountType(account.getAccountType());
			response.setBranch(account.getBranch());
			response.setCustomerName(account.getCustomerName());
			response.setOpenDate(account.getOpenDate());
			accountRepo.saveAndFlush(response);
		}
		return null;
	}

	public List<Account> getAllAccount() {
		return accountRepo.findAll();

	}

	public Optional<Account> getAccountById(long id) {

		return accountRepo.findById(id);

	}

	public void deleteAccount(long id) {
		Account response = accountRepo.findById(id).orElseGet(null);
		accountRepo.delete(response);

	}

}
