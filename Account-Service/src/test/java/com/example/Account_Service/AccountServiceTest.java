package com.example.Account_Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        account1 = new Account();
        account1.setId(1L);
        account1.setName("John Doe");
        account1.setEmail("john@example.com");

        account2 = new Account();
        account2.setId(2L);
        account2.setName("Jane Doe");
        account2.setEmail("jane@example.com");
    }

    @Test
    void getAllAccounts() {
        List<Account> accounts = Arrays.asList(account1, account2);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getAccountById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account1));

        Optional<Account> result = accountService.getAccountById(1L);
        assertEquals("John Doe", result.get().getName());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void createAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account1);

        Account result = accountService.createAccount(account1);
        assertEquals("John Doe", result.getName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account1));
        when(accountRepository.save(any(Account.class))).thenReturn(account1);

        Account result = accountService.updateAccount(1L, account1);
        assertEquals("John Doe", result.getName());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_NotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.updateAccount(1L, account1));
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteAccount() {
        doNothing().when(accountRepository).deleteById(anyLong());

        accountService.deleteAccount(1L);
        verify(accountRepository, times(1)).deleteById(anyLong());
    }
}
