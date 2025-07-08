package com.dl38.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String holderName;
    private double balance;
}
