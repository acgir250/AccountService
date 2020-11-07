package com.omnirio.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.omnirio.accountservice.entities.Account;
import com.omnirio.accountservice.service.AccountService;

@RestController
@RequestMapping("/api/accountservice")
public class AccountController {

	@Autowired
	AccountService service;

	@PostMapping("/accounts")
	public ResponseEntity<?> saveUser(@RequestBody Account account) throws JsonMappingException, JsonProcessingException {
		service.saveAccount(account);
		return ResponseEntity.created(null).build();
	}

	@GetMapping("/accounts")
	public ResponseEntity<?> getAccounts() {
		return ResponseEntity.ok(service.getAllAccount());
	}

	@GetMapping("/accounts/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id")long id) {
		Account fef= service.getAccountById(id).orElse(null);
		return ResponseEntity.ok(fef);
	}

	@PutMapping("/accounts/{id}")
	public ResponseEntity<?> updateAccount(@RequestBody Account user, @PathVariable("id") long id) {
		service.updateAccount(user, id);
		return ResponseEntity.created(null).build();
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") long id) {

		return null;
	}
}
