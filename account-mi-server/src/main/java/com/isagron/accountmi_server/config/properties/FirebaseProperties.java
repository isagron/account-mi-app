package com.isagron.accountmi_server.config.properties;

import lombok.Data;

@Data
public class FirebaseProperties {
    String apikey;
    boolean enable;
    FirebaseAccountProperties account;


}
