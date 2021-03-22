package com.isagron.accountmi_api.dtos.goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {

    private String goalId;

    private String category;

    private Double amount;

    private GoalInterval goalInterval;
}
