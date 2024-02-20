package com.accountsService.services;

import com.accountsService.persistence.AccountRepository;
import com.accountsService.exception.AccountNotfoundException;
import com.accountsService.model.Account;
import com.accountsService.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountService implements IAccountService {
    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        Date current_Date = new Date();
        account.setOpeningDate(current_Date);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null; // Will be gotten from user service
        account.setOwner(owner);
        return account;
    }

    @Override
    public List<Account> getAccountByOwnerId(Long ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        newAccount.setType(account.getType());
        return accountRepository.save(newAccount);
    }

    @Override
    public Account addBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null;// Will be gotten from user service
        int newBalance = newAccount.getBalance() + amount;
        newAccount.setBalance(newBalance);
        return accountRepository.save(newAccount);
    }

    @Override
    public Account withdrawBalance(Long id, int amount, Long ownerId) {
        Account newAccount = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        Customer owner = null; // Will be gotten from user service
        int newBalance = newAccount.getBalance() - amount;
        newAccount.setBalance(newBalance);
        return accountRepository.save(newAccount);
    }

    @Override
    public void delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotfoundException(id));
        this.accountRepository.delete(account);
    }

    @Override
    public void deleteAccountsUsingOwnerId(Long ownerId) {
        List<Account> accounts = accountRepository.findByOwnerId(ownerId);
        if(accounts.size() == 0){
            throw new AccountNotfoundException();
        } else {
            for (Account account : accounts) {
                this.accountRepository.delete(account);
            }
        }
    }


}
