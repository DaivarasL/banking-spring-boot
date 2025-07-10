package com.dl38.service;

import com.dl38.dto.AccountDto;
import com.dl38.entity.Account;
import com.dl38.exception.AccountNotFoundException;
import com.dl38.exception.InsufficientFundsException;
import com.dl38.repository.AccountRepository;
import com.dl38.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
//    @Mock
//    private AccountRepository accountRepository;
//    @InjectMocks
//    AccountServiceImpl accountService;

//    @Test
//    void createAccountShouldAddAccountSuccessfully() {
//        Account account = new Account(1L, "Name", 100);
//        AccountDto accountDto = AccountMapper.mapToAccountDto(account);
//        Mockito.when(accountRepository.save(account)).thenReturn(account);
//        Account addedAccount = AccountMapper.mapToAccount(accountService.createAccount(accountDto));
//        Assertions.assertEquals(addedAccount.getId(), account.getId());
//    }

    private AccountRepository accountRepository;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    void testCreateAccount() {
        AccountDto accountDto = new AccountDto(null, "John", 1000);
        Account savedAccount = new Account(1L, "John", 1000);

        // Mocks what to return with simulated repo
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(savedAccount);
        AccountDto result = accountService.createAccount(accountDto);

        assertEquals(1L, result.id());
        assertEquals("John", result.holderName());
        assertEquals(1000, result.balance());
    }

    @Test
    void testGetAccountById_Success() {
        Account account = new Account(1L, "Alice", 500.0);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDto dto = accountService.getAccountById(1L);

        assertEquals("Alice", dto.holderName());
        assertEquals(500.0, dto.balance());
    }

    @Test
    void testGetAccountById_NotFound() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testDeposit_Success() {
        Account existing = new Account(1L, "Bob", 200.0);
        Account updated = new Account(1L, "Bob", 700.0);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updated);

        AccountDto result = accountService.deposit(1L, 500.0);

        assertEquals(700.0, result.balance());
    }

    @Test
    void testWithdraw_Success() {
        Account existing = new Account(1L, "Charlie", 800.0);
        Account updated = new Account(1L, "Charlie", 300.0);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updated);

        AccountDto result = accountService.withdraw(1L, 500.0);

        assertEquals(300.0, result.balance());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        Account existing = new Account(1L, "Dana", 100.0);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(1L, 200.0));
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1L, "User1", 100.0),
                new Account(2L, "User2", 200.0)
        );

        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountDto> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).holderName());
        assertEquals("User2", result.get(1).holderName());
    }

    @Test
    void testDeleteAccountById_Success() {
        Account account = new Account(1L, "Zack", 500.0);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccountById(1L);

        // Checks if method was successfully called 1 time
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAccountById_NotFound() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccountById(1L));
    }
}
