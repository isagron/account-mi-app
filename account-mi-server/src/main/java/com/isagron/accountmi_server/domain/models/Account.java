package com.isagron.accountmi_server.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Account {

    @Id
    private String id;

    private String userId;

    @Builder.Default
    private Double accountBalance = 0.0;

    @Singular
    private List<String> expenseCategories;

    @Singular
    private List<String> incomeCategories;

    private List<Goal> goals;

    @Builder.Default
    private boolean active = false;

    public void addGoal(Goal newGoal) {
        if (CollectionUtils.isEmpty(this.goals))
        {
            this.goals = new ArrayList<>();
        }
        this.goals.add(newGoal);
    }

    public void removeGoal(String goalId) {
        if (!CollectionUtils.isEmpty(this.goals)) {
            this.goals.removeIf(goal -> goal.getGoalId().toString().equals(goalId));
        }
    }

    public void addExpenseCategory(String category) {
        if (this.expenseCategories == null){
            this.expenseCategories = new ArrayList<>();
        }
        if (!this.expenseCategories.contains(category)){
            this.expenseCategories.add(category);
        }
    }

    public void addIncomeCategory(String type) {
        if (this.incomeCategories == null){
            this.incomeCategories = new ArrayList<>();
        }
        if (!this.incomeCategories.contains(type)){
            this.incomeCategories.add(type);
        }
    }
}
