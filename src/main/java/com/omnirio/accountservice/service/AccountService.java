package com.omnirio.accountservice.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

	@Autowired
	private WebClient.Builder webClientBuilder;

	@SuppressWarnings("unchecked")
	public Account saveAccount(Account account) throws JsonMappingException, JsonProcessingException {
		String response = webClientBuilder.build().get()
				.uri(baseUrl + "/users/" + account.getCustomerName() + "/username").retrieve().bodyToMono(String.class)
				.block();
		ObjectMapper mapper = new ObjectMapper();
		if (response != null) {
			Map<String, Object> res = mapper.readValue(response, Map.class);
			int customerId = (int) res.get("userId");
			account.setCustomerId(customerId);
			if(res.get("dateOfBirth")!=null)
			{
				String dob = res.get("dateOfBirth").toString();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate date = LocalDate.parse(dob, format);
				Period period = Period.between(date, LocalDate.now());
				if (period.getYears() > 18) {
					account.setMinorIndicator("Y");
				} else {
					account.setMinorIndicator("N");
				}	
			}
			
		} else {
			Map<String, String> customerObj = new HashMap<>();
			customerObj.put("userName", account.getCustomerName());
			customerObj.put("roleName", "Customer");
			customerObj.put("gender", "M");
			Map<String,Object> jsonMap;
			String postResult = webClientBuilder.build().post().uri(baseUrl + "/users")
					.body(Mono.just(customerObj), Map.class).retrieve().bodyToMono(String.class).block();
			String rest = webClientBuilder.build().get()
					.uri(baseUrl + "/users/" + account.getCustomerName() + "/username").retrieve()
					.bodyToMono(String.class).block();
			ObjectMapper jsonMapper = new ObjectMapper();

			if(rest!=null)
			{
				try {
					jsonMap = jsonMapper.readValue(rest, Map.class);
					account.setCustomerId((int)jsonMap.get("userId"));
				} catch (JsonProcessingException e) {
				e.printStackTrace();
				}
			}
		
			
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
