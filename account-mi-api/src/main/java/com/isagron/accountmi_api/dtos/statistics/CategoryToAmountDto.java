package com.isagron.accountmi_api.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
public class CategoryToAmountDto {

    private String category;

    private int month;

    private int year;

    private Double amount;

    private Double goal;
}
