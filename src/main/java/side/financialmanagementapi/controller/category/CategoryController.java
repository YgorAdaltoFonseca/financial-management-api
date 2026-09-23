package side.financialmanagementapi.controller.category;

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
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserEntityRepository userEntityRepository;

    @PostMapping("/createCategory/{userId}")
    public ResponseEntity<CategoryResponse> criarCategoria(
            @PathVariable Long userId,
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse response = categoryService.createCategory(request, userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateCategory/{userId}")
    public ResponseEntity<CategoryResponse> atualizarCategoria(
            @PathVariable Long userId,
            @Valid @RequestBody CategoryRequest request
    ){
        CategoryResponse response = categoryService.updateCategory(userId, request);

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
