package com.accountsService.exception;

public class AccountNotfoundException extends GlobalException {
    protected static final long serialVersionUID = 2L;

    public AccountNotfoundException() {
        super("Account not found");
    }

    public AccountNotfoundException(Long accountId) {
        super("Account with id: " + accountId + " not found");
    }
}
