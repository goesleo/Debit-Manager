package com.leonardo.debt_manager_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ParcelaResponseDTO(
        UUID id,
        UUID dividaId,       // NOVO
        String nomeDivida,   // NOVO
        Integer numeroParcela,
        BigDecimal valor,
        LocalDate dataVencimento,
        String status,
        LocalDate dataPagamento
) {}