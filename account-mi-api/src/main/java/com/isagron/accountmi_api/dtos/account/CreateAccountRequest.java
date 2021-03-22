package com.isagron.accountmi_api.dtos.account;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotBlank
    private String userId;

    @Builder.Default
    private Double accountBalance = 0.0;

    private List<String> expenseCategories;

    private List<String> incomeCategories;

}
