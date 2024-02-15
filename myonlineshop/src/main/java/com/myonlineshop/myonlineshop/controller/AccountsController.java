package com.myonlineshop.myonlineshop.controller;

import com.myonlineshop.myonlineshop.exception.AccountNotfoundException;
import com.myonlineshop.myonlineshop.model.Account;
import com.myonlineshop.myonlineshop.model.Customer;
import com.myonlineshop.myonlineshop.persistence.AccountRepository;
import com.myonlineshop.myonlineshop.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts")
@Validated
public class AccountsController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> listar(){
        List<Account> accs = accountService.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accs);
    }
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Account> geOne(@PathVariable("id")@Min(1) Long aid) {
        return ResponseEntity.status(HttpStatus.OK).body(
                accountRepository.findById(aid).orElseThrow(() -> new AccountNotfoundException(aid))
        );
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Account> create(@RequestBody @Valid Account newAcc) {
        return new ResponseEntity<>(accountService.create(newAcc), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Account> update(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid Account account) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.updateAccount(id, account));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(1)Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/addBalance/{id}")
    public ResponseEntity addBalance(@PathVariable("id") @Min(1) Long id,@RequestParam Long cid,@RequestParam int mount){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.addBalance(id, mount,cid));
    }
    @PutMapping("/withdrawBalance/{id}")
    public ResponseEntity withdrawBalance(@PathVariable("id") @Min(1)Long id,@RequestParam Long cid,@RequestParam int mount){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.withdrawBalance(id, mount,cid));
    }
    @DeleteMapping(value = "/delete/{cid}")
    public ResponseEntity deleteAll(@PathVariable("cid") @Min(1) Long id){
        accountService.deleteAccountsUsingOwnerId(id);
        return ResponseEntity.noContent().build();
    }
}
