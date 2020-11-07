package com.omnirio.accountservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnirio.accountservice.security.CustomUserConverter;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Value("${base.url}")
	private String baseUrl;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String response = webClientBuilder.build().get()
				.uri("http://user-service/username/"+username).retrieve().bodyToMono(String.class)
				.block();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> res = null;
		if (response != null) {
		try {
			res = mapper.readValue(response, Map.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return  new User(res.get("userName").toString(), res.get("password").toString(),Collections.emptyList());
	}

}