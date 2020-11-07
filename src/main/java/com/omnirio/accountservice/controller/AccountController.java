package com.omnirio.accountservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
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
	public ResponseEntity<?> saveUser(@RequestBody Account account) throws JsonMappingException, JsonProcessingException,Exception {
		Account result = service.saveAccount(account);
		EntityModel<Account> modelResource = EntityModel.of(result, getAllLinks(result.getAccountId()));
		
		return modelResource.getLink(IanaLinkRelations.SELF).map(Link::getHref).map(href -> {
			try {
				return new URI(href);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).map(uri -> ResponseEntity.noContent().location(uri).build())
				.orElse(ResponseEntity.badRequest().body("Unable to Add Account " + account));

	}

	@GetMapping("/accounts")
	public ResponseEntity<CollectionModel<EntityModel<Account>>> getAccounts()throws Exception {
		List<Account> accounts= service.getAllAccount();
		List<EntityModel<Account>> finalResponse =new ArrayList<EntityModel<Account>>();
		 for(Account userObject: accounts)
		 {
			   EntityModel<Account> model =EntityModel.of(userObject,getAllLinks(userObject.getAccountId()));
			   finalResponse.add(model);
		 }
		return ResponseEntity.ok(CollectionModel.of( //
				finalResponse, //
				linkTo(methodOn(AccountController.class).getAccounts()).withSelfRel()
						.andAffordance(afford(methodOn(AccountController.class).saveUser(null)))));
	}

	@GetMapping("/accounts/{id}")
	public ResponseEntity<?> getAccountById(@PathVariable("id")long id)throws Exception {
		return service.getAccountById(id).map(mapper->EntityModel.of(mapper,getAllLinks(mapper.getAccountId())))
				.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/accounts/{id}")
	public ResponseEntity<?> updateAccount(@RequestBody Account user, @PathVariable("id") long id)throws Exception {
		Account account = service.updateAccount(user, id);
		EntityModel<Account> modelResource = EntityModel.of(account, getAllLinks(account.getAccountId()));

		return modelResource.getLink(IanaLinkRelations.SELF).map(Link::getHref).map(href -> {
			try {
				return new URI(href);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).map(uri -> ResponseEntity.noContent().location(uri).build())
				.orElse(ResponseEntity.badRequest().body("Unable to update " + user));
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") long id)throws Exception {
		service.deleteAccount(id);
		EntityModel<String> modelResource = EntityModel.of("Content Delete", getAllLinks(id));
		
		return modelResource.getLink(IanaLinkRelations.SELF).map(Link::getHref).map(href -> {
			try {
				return new URI(href);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).map(uri -> ResponseEntity.noContent().location(uri).build())
				.orElse(ResponseEntity.badRequest().body("Unable to delete "+id));
	}
	
	private List<Link> getAllLinks(long id) {
		List<Link> links=null;
		try {
			 links =Arrays.asList(
					linkTo(methodOn(AccountController.class).getAccountById(id)).withSelfRel()
							.andAffordance(afford(methodOn(AccountController.class).updateAccount(null, id)))
							.andAffordance(afford(methodOn(AccountController.class).deleteAccount(id))),
					linkTo(methodOn(AccountController.class).getAccounts()).withRel("accounts"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return links;
	}
}
