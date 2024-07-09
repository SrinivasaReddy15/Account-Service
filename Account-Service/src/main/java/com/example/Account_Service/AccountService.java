package com.example.Account_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account account) {
        if (accountRepository.findById(id).isPresent()) {
            account.setId(id);
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
