package side.financialmanagementapi.controller.category;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.category.CategoryRequest;
import side.financialmanagementapi.dto.response.category.CategoryResponse;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/category")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryResponse> criarCategoria(
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<CategoryResponse> atualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id){
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoriesList")
    public ResponseEntity<List<CategoryResponse>> categoriaList(){
        List<CategoryResponse> response = categoryService.listarCategorias();

        return ResponseEntity.ok(response);
    }
}
