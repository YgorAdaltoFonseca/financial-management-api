package side.financialmanagementapi.dto.response;

import side.financialmanagementapi.enums.CategoryTypeEnum;

public record CategoryResponse(
        Long id,
        String name,
        CategoryTypeEnum categoryType
) {}
