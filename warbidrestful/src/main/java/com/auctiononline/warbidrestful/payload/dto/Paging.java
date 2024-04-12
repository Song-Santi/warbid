package com.auctiononline.warbidrestful.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private int totalItems;
}
