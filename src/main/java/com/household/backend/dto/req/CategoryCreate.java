package com.household.backend.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreate {
    private String categoryPk;
    private String categoryName;
    private String categoryType;
}
