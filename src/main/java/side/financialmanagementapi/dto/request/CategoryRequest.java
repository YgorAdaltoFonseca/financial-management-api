package side.financialmanagementapi.dto.request;

import side.financialmanagementapi.enums.CategoryTypeEnum;

public record CategoryRequest(
        String name ,
        CategoryTypeEnum categoryType
) {}
