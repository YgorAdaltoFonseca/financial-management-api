package side.financialmanagementapi.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import side.financialmanagementapi.enums.category.CategoryTypeEnum;

public record CategoryRequest(
        @NotBlank @Size(max = 80) String name ,
        @NotNull CategoryTypeEnum categoryType
) {}
