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
		return null;
	}

	@GetMapping("/accounts")
	public ResponseEntity<?> getAccounts(@RequestBody Account user) {
		return ResponseEntity.ok(service.getAllAccount());
	}

	@GetMapping("/accounts/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id")long id) {

		return ResponseEntity.ok(service.getAccountById(id));
	}

	@PutMapping("/accounts/{id}")
	public ResponseEntity<?> updateAccount(@RequestBody Account user, @PathVariable("id") long id) {

		return null;
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") long id) {

		return null;
	}
}
