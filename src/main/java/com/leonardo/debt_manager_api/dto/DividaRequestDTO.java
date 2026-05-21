package com.leonardo.debt_manager_api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DividaRequestDTO(

        @NotBlank(message = "O nome da dívida é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotNull(message = "O valor total é obrigatório")
        @Positive(message = "O valor total deve ser maior que zero")
        BigDecimal valorTotal,

        @NotNull(message = "A quantidade de parcelas é obrigatória")
        @Min(value = 1, message = "A dívida deve ter no mínimo 1 parcela")
        Integer quantidadeParcelas,

        @NotNull(message = "A data do primeiro vencimento é obrigatória")
        @FutureOrPresent(message = "A data de vencimento não pode estar no passado")
        LocalDate dataPrimeiroVencimento,

        String categoria,

        String observacoes
) {}
