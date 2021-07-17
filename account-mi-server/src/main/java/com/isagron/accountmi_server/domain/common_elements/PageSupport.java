package com.isagron.accountmi_server.domain.common_elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class PageSupport<T> {

    public static final String FIRST_PAGE_NUM = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";

    List<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;
    double totalAmount;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean first() {
        return pageNumber == Integer.parseInt(FIRST_PAGE_NUM);
    }

    @JsonProperty
    public boolean last() {
        return (pageNumber + 1) * pageSize >= totalElements;
    }

    public static  <T> PageSupport of(List<T> content){
        PageSupport<T> pageSupport = new PageSupport<>();
        pageSupport.pageNumber = 0;
        pageSupport.content = content;
        return pageSupport;
    }
}
