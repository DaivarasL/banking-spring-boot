package com.dl38.mapper;

import com.dl38.dto.AccountDto;
import com.dl38.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDto accountDto) {
        return new Account(
                accountDto.id(), // with record instead of getId() it's id()
                accountDto.holderName(),
                accountDto.balance()
        );
    }

    public static AccountDto mapToAccountDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getHolderName(),
                account.getBalance()
        );
    }
}
