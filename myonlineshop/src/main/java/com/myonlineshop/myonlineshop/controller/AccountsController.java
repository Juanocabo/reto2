package com.myonlineshop.myonlineshop.controller;

import com.myonlineshop.myonlineshop.exception.AccountNotfoundException;
import com.myonlineshop.myonlineshop.model.Account;
import com.myonlineshop.myonlineshop.persistence.AccountRepository;
import com.myonlineshop.myonlineshop.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "API de cuentas", description = "Endpoints para consumir cuentas")
public class AccountsController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Operation(summary = "Para listar todas las cuentas", description = "Devuelve todas las cuentas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuando hay cuentas a devolver."),
        @ApiResponse(responseCode = "404", description = "Cuando no hay cuentas a devolver."),
    })
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

    @Operation(summary = "Para crear una cuenta nueva", description = "Crea una cuenta nueva a partir del JSON/XML")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuando hay cuentas a devolver."),
        @ApiResponse(responseCode = "412", description = "Cuando el JSON/XML está mal formateado."),
    })
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

    @Operation(summary = "Para añadir cantidad al balance", description = "Añade una cantidad al balance de la cuenta de un cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuando hay cuentas a devolver."),
        @ApiResponse(responseCode = "412", description = "Cuando el JSON/XML está mal formateado."),
    })
    @PutMapping("/addBalance/{id}")
    public ResponseEntity addBalance(@PathVariable("id") @Min(1) Long id,
                                     @Parameter(name = "Id cliente", description = "Indica el identificador del cliente", example = "2") @RequestParam Long cid,
                                     @Parameter(name = "Cantidad", description = "Indica la cantidad a añadir al balance", example = "50") @RequestParam int mount){
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
