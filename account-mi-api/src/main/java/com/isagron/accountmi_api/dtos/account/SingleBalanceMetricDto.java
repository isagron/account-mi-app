package com.isagron.accountmi_api.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleBalanceMetricDto {

    private Double currentMonth;

    private Double previousMonth;

    private Double totalThisYear;

}
