package com.leonardo.debt_manager_api.service;

import com.leonardo.debt_manager_api.dto.ParcelaResponseDTO;
import com.leonardo.debt_manager_api.model.Parcela;
import com.leonardo.debt_manager_api.model.StatusParcela;
import com.leonardo.debt_manager_api.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;

    // Retorna todas as parcelas de uma dívida, ordenadas pelo número da parcela
    public List<ParcelaResponseDTO> buscarParcelasPorDivida(UUID dividaId) {
        return parcelaRepository.findByDividaId(dividaId)
                .stream()
                // Aqui ordenamos para que a Parcela 1 sempre apareça antes da Parcela 2 no Front-end
                .sorted((p1, p2) -> p1.getNumeroParcela().compareTo(p2.getNumeroParcela()))
                .map(this::converterParaDTO)
                .toList();
    }
    public List<ParcelaResponseDTO> buscarTodas() {
        return parcelaRepository.findAll().stream()
                .sorted((p1, p2) -> p1.getDataVencimento().compareTo(p2.getDataVencimento()))
                .map(this::converterParaDTO)
                .toList();
    }

    // Regra de Negócio: Efetuar o pagamento
    @Transactional
    public ParcelaResponseDTO pagarParcela(UUID parcelaId) {
        // 1. Busca a parcela no banco. Se não achar, lança erro 404 (Not Found)
        Parcela parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parcela não encontrada"));

        // 2. Validação: Impede de pagar uma parcela que já foi paga
        if (parcela.getStatus() == StatusParcela.PAGA) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Esta parcela já está paga!");
        }

        // 3. Atualiza os dados
        parcela.setStatus(StatusParcela.PAGA);
        parcela.setDataPagamento(LocalDate.now()); // Registra o dia exato que o usuário clicou no botão

        // 4. Salva e retorna o DTO atualizado
        parcela = parcelaRepository.save(parcela);
        return converterParaDTO(parcela);
    }

    private ParcelaResponseDTO converterParaDTO(Parcela parcela) {
        return new ParcelaResponseDTO(
                parcela.getId(),
                parcela.getDivida().getId(),          // Puxa o ID da relação
                parcela.getDivida().getNome(),        // Puxa o Nome da relação
                parcela.getNumeroParcela(),
                parcela.getValor(),
                parcela.getDataVencimento(),
                parcela.getStatus().name(),
                parcela.getDataPagamento()
        );
    }
}