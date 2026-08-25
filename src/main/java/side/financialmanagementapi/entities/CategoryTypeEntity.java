package side.financialmanagementapi.entities;

import jakarta.persistence.*;
import lombok.*;
import side.financialmanagementapi.enums.CategoryTypeEnum;

@Entity
@Table(name = "CategoryTypeEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryTypeEnum categoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
