package com.accountsService.controller;

import com.accountsService.persistence.AccountRepository;
import com.accountsService.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:datos_prueba.sql")
class AccountsTest_MockMvc {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Test
    void addBalance_OK() throws Exception {

//        given
        Long id = 1L;
        Long cid = 1L;
        int mount = 20;

//        when + then
       mvc.perform(put("/accounts/addBalance/" + id + "?cid=" + cid + "&mount=" +  mount).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        ;
    }

    @Test
    void addBalance_NotAccountFound() throws Exception {

//        given
        Long id = 10L;
        Long cid = 1L;
        int mount = 20;

//        when + then
       mvc.perform(put("/accounts/addBalance/" + id + "?cid=" + cid + "&mount=" +  mount).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
        ;
    }
    @Test
    void deleteAll_OK() throws Exception {

//        given
        Long cid = 1L;

//        when + then
       mvc.perform(delete("/accounts/delete/" + cid).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    void deleteAll_NotAccountFound() throws Exception {

//        given
        Long cid = 4L;

//        when + then
       mvc.perform(delete("/accounts/delete/" + cid).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
        ;
    }
}