package com.microcompany.oauth2apiclient.controller;

import com.microcompany.oauth2apiclient.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/addBalance/{id}")
    public Account addBalance(
           @RegisteredOAuth2AuthorizedClient("products-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            @PathVariable("id") Long id,@RequestParam Long cid,@RequestParam int mount

    ) {
        return this.webClient
                .put()
                .uri("http://127.0.0.1:9900/accounts/addBalance/"+id+"?cid="+cid+"&mount="+mount)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Account>() {
                })
                .block();
    }
    @GetMapping(value = "/withdrawBalance/{id}")
    public Account withdrawBalance(
            @RegisteredOAuth2AuthorizedClient("products-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            @PathVariable("id") Long id,@RequestParam Long cid,@RequestParam int mount
    ) {
        return this.webClient
                .put()
                .uri("http://127.0.0.1:9900/accounts/withdrawBalance/"+id+"?cid="+cid+"&mount="+mount)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Account>() {
                })
                .block();
    }
}