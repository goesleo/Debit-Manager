package com.leonardo.debt_manager_api.controller;

import com.leonardo.debt_manager_api.dto.ParcelaResponseDTO;
import com.leonardo.debt_manager_api.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parcelas")
@CrossOrigin(origins = "*") // Liberando o CORS para o nosso React!
@RequiredArgsConstructor
public class ParcelaController {

    private final ParcelaService parcelaService;

    // Rota: GET /api/v1/parcelas/divida/{id_da_divida}
    @GetMapping("/divida/{dividaId}")
    public ResponseEntity<List<ParcelaResponseDTO>> listarPorDivida(@PathVariable UUID dividaId) {
        return ResponseEntity.ok(parcelaService.buscarParcelasPorDivida(dividaId));
    }

    @GetMapping
    public ResponseEntity<List<ParcelaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(parcelaService.buscarTodas());
    }

    // Rota: PATCH /api/v1/parcelas/{id_da_parcela}/pagar
    // Usamos PATCH (e não PUT) no mercado quando queremos atualizar apenas UMA pequena parte do recurso (neste caso, o status)
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<ParcelaResponseDTO> pagarParcela(@PathVariable UUID id) {
        return ResponseEntity.ok(parcelaService.pagarParcela(id));
    }
}