package com.isagron.accountmi_api.dtos.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {

    private String id;

    @NotBlank
    private String accountId;

    @NotBlank
    private String category;

    private String store;

    @NotNull
    @Min(0)
    private Double amount;

    @NotNull
    private Date date;


}
