package side.financialmanagementapi.dto.response.category;

import side.financialmanagementapi.enums.category.CategoryTypeEnum;

public record CategoryResponse(
        Long id,
        String name,
        CategoryTypeEnum categoryType
) {}
