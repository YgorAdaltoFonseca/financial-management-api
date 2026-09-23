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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryTypeRepository categoryTypeRepository;
    private final UserEntityRepository userEntityRepository;


    //Criar categoria
    public CategoryResponse createCategory(CategoryRequest categoryRequest , Long userId) {
        UserEntity user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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
    public CategoryResponse updateCategory(Long id , CategoryRequest categoryRequest){
        CategoryTypeEntity categoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        categoryType.setName(categoryRequest.name());
        categoryType.setCategoryType(categoryRequest.categoryType());

        categoryTypeRepository.save(categoryType);

        return new CategoryResponse(
                categoryType.getId(),
                categoryType.getName(),
                categoryType.getCategoryType()
        );
    }

    //Deletar Categoria
    public void deleteCategory(Long id){
        CategoryTypeEntity categoryType = categoryTypeRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        categoryTypeRepository.delete(categoryType);
    }
    
    //Listar todas as categorias
    public List<CategoryResponse> listarCategorias(){
        List<CategoryTypeEntity> categoryTypeEntities = categoryTypeRepository.findAll();
        
        return categoryTypeEntities.stream()
                .map(category -> new CategoryResponse(
                        category.getId() ,
                        category.getName() ,
                         category.getCategoryType()
                ))
                .toList();
    }


}
