package com.accountsService.controller;

import com.accountsService.exception.AccountNotfoundException;
import com.accountsService.model.Account;
import com.accountsService.persistence.AccountRepository;
import com.accountsService.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsController.class)
class AccountsTest_WebMvcTest {

   @BeforeEach
    public void setUp() {

        Account account = new Account(1L, "tipo1", new Date(), 20, 1L, null);

        Mockito.when(service.addBalance(1L, 20, 1L))
                .thenReturn(account);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(account));

        Mockito.when(service.addBalance(10L, 20, 1L))
                .thenThrow(AccountNotfoundException.class);

        Mockito.when(repository.findById(10L))
                .thenThrow(AccountNotfoundException.class);

        Mockito.when(repository.findByOwnerId(4L))
                        .thenReturn(new ArrayList<>());

        Mockito.doThrow(new AccountNotfoundException()).when(service).deleteAccountsUsingOwnerId(4L);

        Mockito.when(repository.save(Mockito.any(Account.class)))
                .thenAnswer(elem -> {
                    Account ac = (Account) elem.getArguments()[0];
                    ac.setId(100L);
                    return ac;
                });
    }


    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @MockBean
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