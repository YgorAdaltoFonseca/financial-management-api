package side.financialmanagementapi.entities;

import jakarta.persistence.*;
import lombok.*;
import side.financialmanagementapi.enums.TransactionOriginEnum;
import side.financialmanagementapi.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionOriginEnum origin;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryTypeEntity categoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
