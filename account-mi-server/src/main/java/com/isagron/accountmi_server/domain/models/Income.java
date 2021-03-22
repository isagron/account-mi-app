package com.isagron.accountmi_server.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
@Document
public class Income {

    @Id
    private String id;

    private String userId;

    private String accountId;

    private String type;

    private Double amount;

    private Date date;

    private boolean consistent;

}
