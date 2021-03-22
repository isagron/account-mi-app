package com.isagron.accountmi_api.dtos.goal;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GoalInterval {

    MONTH("Month"),
    YEAR("Year");

    @JsonValue
    private String value;

    GoalInterval(String value) {
        this.value = value;
    }
}
