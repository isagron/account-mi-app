package com.isagron.accountmi_server.domain.common_elements;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DateRange {

    private Date from;

    private Date to;
}
