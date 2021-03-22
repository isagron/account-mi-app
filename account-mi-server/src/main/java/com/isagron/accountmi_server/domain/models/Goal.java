package com.isagron.accountmi_server.domain.models;

import com.isagron.accountmi_api.dtos.goal.GoalInterval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Goal {

    private ObjectId goalId;

    private String categoryName;

    private double amount;

    private GoalInterval goalInterval;
}
