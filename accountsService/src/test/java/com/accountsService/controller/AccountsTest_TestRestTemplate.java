package com.accountsService.controller;

import com.accountsService.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("classpath:datos_prueba.sql")
class AccountsTest_TestRestTemplate {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addBalance_OK() {
        Account account = new Account(1L, "tipo1",  new Date(), 20, 1L, null);

        HttpHeaders headers = new HttpHeaders();
        headers.add("ACCEPT", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Account> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/addBalance/" + account.getId() + "?cid=" + account.getOwnerId() + "&mount=" + account.getBalance(),
                HttpMethod.PUT, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void addBalance_NotAccountFound() {
        Account account = new Account(100L, "tipo1",  new Date(), 20, 1L, null);

        HttpHeaders headers = new HttpHeaders();
        headers.add("ACCEPT", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Account> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/addBalance/" + account.getId() + "?cid=" + account.getOwnerId() + "&mount=" + account.getBalance(),
                HttpMethod.PUT, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void deleteAll_OK() {
        Account account = new Account(1L, "tipo1",  new Date(), 20, 1L, null);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/delete/" + account.getOwnerId(),
                HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteAll_NotAccountFound() {
        Account account = new Account(1L, "tipo1",  new Date(), 20, 10L, null);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/delete/" + account.getOwnerId(),
                HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}