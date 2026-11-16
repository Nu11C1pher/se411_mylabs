package edu.psu.se411.lab15.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Table("product")
public record Product(
        @Id Long id,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 100) String description,
        @Digits(integer = 6, fraction = 2) BigDecimal price
) {}
