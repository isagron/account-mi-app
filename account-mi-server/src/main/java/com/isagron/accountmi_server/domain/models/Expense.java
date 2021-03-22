package com.isagron.accountmi_server.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Expense {

    @Id
    private String id;

    private String category;

    private String store;

    private Double amount;

    private Date date;

    private String accountId;


}
