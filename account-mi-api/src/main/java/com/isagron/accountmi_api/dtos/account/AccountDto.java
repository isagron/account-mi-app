package com.isagron.accountmi_api.dtos.account;

import com.isagron.accountmi_api.dtos.goal.GoalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String accountId;

    private String userId;

    @Builder.Default
    private Double accountBalance = 0.0;

    private List<String> expenseCategories;

    private List<String> incomeCategories;

    private List<GoalDto> goals;

}
