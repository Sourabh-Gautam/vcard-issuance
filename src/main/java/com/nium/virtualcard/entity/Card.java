package com.nium.virtualcard.entity;

import com.nium.virtualcard.entity.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardholderName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
}
