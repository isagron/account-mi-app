package com.isagron.accountmi_api.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalanceDto {

    private SingleBalanceMetricDto incomes;

    private SingleBalanceMetricDto outcomes;

    private SingleBalanceMetricDto balance;

}
