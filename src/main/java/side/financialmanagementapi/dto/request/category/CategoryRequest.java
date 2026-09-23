package side.financialmanagementapi.dto.request.category;

import side.financialmanagementapi.enums.category.CategoryTypeEnum;

public record CategoryRequest(
        String name ,
        CategoryTypeEnum categoryType
) {}
