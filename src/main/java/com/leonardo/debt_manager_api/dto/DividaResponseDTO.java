package com.leonardo.debt_manager_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DividaResponseDTO(
        UUID id,
        String nome,
        BigDecimal valorTotal,
        Integer quantidadeParcelas,
        String status // Resumo para o app saber se a dívida como um todo está em dia
) {}