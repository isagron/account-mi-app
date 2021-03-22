package com.isagron.accountmi_server.config.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseAccountProperties {

    String type;
    String projectId;
    String privateKeyId;
    String privateKey;
    String clientEmail;
    String clientId;
    String authUri;
    String tokenUri;
    String authProviderCertUrl;
    String clientCertUrl;

    public String getType() {
        return type;
    }
    @JsonProperty("project_id")
    public String getProjectId() {
        return projectId;
    }
    @JsonProperty("private_key_id")
    public String getPrivateKeyId() {
        return privateKeyId;
    }
    @JsonProperty("private_key")
    public String getPrivateKey() {
        return privateKey;
    }
    @JsonProperty("client_email")
    public String getClientEmail() {
        return clientEmail;
    }
    @JsonProperty("client_id")
    public String getClientId() {
        return clientId;
    }
    @JsonProperty("auth_uri")
    public String getAuthUri() {
        return authUri;
    }
    @JsonProperty("token_uri")
    public String getTokenUri() {
        return tokenUri;
    }
    @JsonProperty("auth_provider_x509_cert_url")
    public String getAuthProviderCertUrl() {
        return authProviderCertUrl;
    }
    @JsonProperty("client_x509_cert_url")
    public String getClientCertUrl() {
        return clientCertUrl;
    }

    public InputStream getAsInputStream(){
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"type\":").append("\"").append(type).append("\"").append(",\n");
        builder.append("\"project_id\":").append("\"").append(projectId).append("\"").append(",\n");
        builder.append("\"private_key_id\":").append("\"").append(privateKeyId).append("\"").append(",\n");
        builder.append("\"private_key\":").append("\"").append(privateKey).append("\"").append(",\n");
        builder.append("\"client_email\":").append("\"").append(clientEmail).append("\"").append(",\n");
        builder.append("\"client_id\":").append("\"").append(clientId).append("\"").append(",\n");
        builder.append("\"auth_uri\":").append("\"").append(authUri).append("\"").append(",\n");
        builder.append("\"token_uri\":").append("\"").append(tokenUri).append("\"").append(",\n");
        builder.append("\"auth_provider_x509_cert_url\":").append("\"").append(authProviderCertUrl).append("\"").append(",\n");
        builder.append("\"client_x509_cert_url\":").append("\"").append(clientCertUrl).append("\"").append("\n");
        builder.append("}");
        return new ByteArrayInputStream(builder.toString().getBytes());
    }
}
