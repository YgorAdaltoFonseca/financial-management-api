package side.financialmanagementapi.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.category.CategoryRequest;
import side.financialmanagementapi.dto.response.category.CategoryResponse;
import side.financialmanagementapi.entities.category.CategoryTypeEntity;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.CategoryNotFoundException;
import side.financialmanagementapi.exceptions.UserNotFoundException;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryTypeRepository categoryTypeRepository;
    private final AuthenticatedUserService authenticatedUserService;


    //Criar categoria
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        CategoryTypeEntity categoryTypeEntity = CategoryTypeEntity.builder()
                .name(categoryRequest.name())
                .categoryType(categoryRequest.categoryType())
                .user(user)
                .build();

        CategoryTypeEntity categoryTypeSaved = categoryTypeRepository.save(categoryTypeEntity);

        return new CategoryResponse(
                categoryTypeSaved.getId(),
                categoryTypeSaved.getName(),
                categoryTypeSaved.getCategoryType()
        );
    }

    //Atualizar categoria
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        CategoryTypeEntity category = categoryTypeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        category.setName(categoryRequest.name());
        category.setCategoryType(categoryRequest.categoryType());

        CategoryTypeEntity saved = categoryTypeRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getCategoryType()
        );
    }

    //Deletar Categoria
    public void deleteCategory(Long id) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        CategoryTypeEntity category = categoryTypeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        categoryTypeRepository.delete(category);
    }

    //Listar todas as categorias
    public List<CategoryResponse> listarCategorias(){

        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        List<CategoryTypeEntity> categoryTypeEntities =
                categoryTypeRepository.findAllByUserId(user.getId());
        
        return categoryTypeEntities.stream()
                .map(category -> new CategoryResponse(
                        category.getId() ,
                        category.getName() ,
                         category.getCategoryType()
                ))
                .toList();
    }


}
