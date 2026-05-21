package com.leonardo.debt_manager_api.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErroValidacaoDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        List<CampoErro> mensagens
) {
    public record CampoErro(String campo, String mensagem) {}
}
