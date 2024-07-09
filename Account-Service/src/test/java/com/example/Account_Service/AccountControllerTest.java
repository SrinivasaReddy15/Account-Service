package com.example.Account_Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

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
    void getAllAccounts() throws Exception {
        List<Account> accounts = Arrays.asList(account1, account2);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void getAccountById() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account1));

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void createAccount() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(account1);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    void updateAccount() throws Exception {
        when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(account1);

        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(accountService, times(1)).updateAccount(anyLong(), any(Account.class));
    }

    @Test
    void deleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(anyLong());

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(anyLong());
    }
}
