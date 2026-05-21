package com.leonardo.debt_manager_api.service;

import com.leonardo.debt_manager_api.dto.DividaRequestDTO;
import com.leonardo.debt_manager_api.dto.DividaResponseDTO;
import com.leonardo.debt_manager_api.model.Divida;
import com.leonardo.debt_manager_api.model.Parcela;
import com.leonardo.debt_manager_api.model.StatusParcela;
import com.leonardo.debt_manager_api.repository.DividaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DividaService {

    private final DividaRepository dividaRepository;

    @Transactional
    public DividaResponseDTO criarDivida(DividaRequestDTO dto) {
        Divida divida = new Divida();
        divida.setNome(dto.nome());
        divida.setValorTotal(dto.valorTotal());
        divida.setQuantidadeParcelas(dto.quantidadeParcelas());
        divida.setCategoria(dto.categoria());
        divida.setObservacoes(dto.observacoes());

        BigDecimal valorParcelaBase = dto.valorTotal()
                .divide(new BigDecimal(dto.quantidadeParcelas()), 2, RoundingMode.DOWN);
        BigDecimal valorJaDistribuido = valorParcelaBase.multiply(new BigDecimal(dto.quantidadeParcelas()));
        BigDecimal sobraCentavos = dto.valorTotal().subtract(valorJaDistribuido);

        LocalDate dataVencimentoAtual = dto.dataPrimeiroVencimento();

        for (int i = 1; i <= dto.quantidadeParcelas(); i++) {
            Parcela parcela = new Parcela();
            parcela.setNumeroParcela(i);
            parcela.setStatus(StatusParcela.PENDENTE);
            parcela.setDataVencimento(dataVencimentoAtual);
            parcela.setDivida(divida);

            if (i == dto.quantidadeParcelas()) {
                parcela.setValor(valorParcelaBase.add(sobraCentavos));
            } else {
                parcela.setValor(valorParcelaBase);
            }

            divida.getParcelas().add(parcela);
            dataVencimentoAtual = dataVencimentoAtual.plusMonths(1);
        }

        Divida dividaSalva = dividaRepository.save(divida);
        return toResponseDTO(dividaSalva);
    }

    private DividaResponseDTO toResponseDTO(Divida divida) {
        return new DividaResponseDTO(
                divida.getId(),
                divida.getNome(),
                divida.getValorTotal(),
                divida.getQuantidadeParcelas(),
                "PENDENTE"
        );
    }
    @Transactional
    public void excluirDivida(UUID id) {
        if (!dividaRepository.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Dívida não encontrada");
        }
        dividaRepository.deleteById(id);
    }
    @Transactional
    public void excluirTodasAsDividas() {
        // O deleteAll do JPA vai apagar todas as dívidas e, graças ao CascadeType.ALL,
        // todas as parcelas sumirão junto automaticamente!
        dividaRepository.deleteAll();
    }

    // ATUALIZAÇÃO do listarTodas para calcular dinamicamente se está PAGA ou PENDENTE
    public List<DividaResponseDTO> listarTodas() {
        return dividaRepository.findAll().stream()
                .map(divida -> {
                    // Verifica se todas as parcelas dessa dívida estão PAGAS
                    boolean todasPagas = divida.getParcelas().stream()
                            .allMatch(p -> p.getStatus() == com.leonardo.debt_manager_api.model.StatusParcela.PAGA);

                    String statusAtual = todasPagas ? "PAGA" : "PENDENTE";

                    return new DividaResponseDTO(
                            divida.getId(),
                            divida.getNome(),
                            divida.getValorTotal(),
                            divida.getQuantidadeParcelas(),
                            statusAtual
                    );
                })
                .toList();
    }
}
