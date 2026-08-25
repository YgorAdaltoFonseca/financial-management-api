package side.financialmanagementapi.entities;

import jakarta.persistence.*;
import lombok.*;
import side.financialmanagementapi.enums.FrequencyEnum;
import side.financialmanagementapi.enums.SubscriptionStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "SignatureEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate nextCharge;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionStatusEnum subscriptionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryTypeEntity categoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
