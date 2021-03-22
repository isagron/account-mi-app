package com.isagron.accountmi_api.dtos.goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalStatusDto {

    private String goalId;

    private String category;

    private double threshold;

    private double avg;

    private double currentStatus;

}
