package com.isagron.accountmi_server.domain.models;

import com.isagron.accountmi_api.validations.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class User {

    @Id
    private String userId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @ValidEmail
    @NotBlank
    private String email;
}
