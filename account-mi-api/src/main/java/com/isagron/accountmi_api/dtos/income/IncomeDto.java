package com.isagron.accountmi_api.dtos.income;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeDto {

    private String id;

    @NotNull(message = "Income type must be present")
    private String type;

    @Builder.Default
    private double amount = 0.0;

    @NotNull(message = "Income date must be present")
    private Date date;

    @Builder.Default
    private boolean consistent = false;

    private String accountId;
}
