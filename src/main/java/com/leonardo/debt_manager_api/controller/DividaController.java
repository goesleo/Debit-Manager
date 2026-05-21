package com.leonardo.debt_manager_api.controller;

import com.leonardo.debt_manager_api.dto.DividaRequestDTO;
import com.leonardo.debt_manager_api.dto.DividaResponseDTO;
import com.leonardo.debt_manager_api.service.DividaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dividas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DividaController {

    private final DividaService dividaService;


    @PostMapping
    public ResponseEntity<DividaResponseDTO> criarDivida(
            @RequestBody @Valid DividaRequestDTO dto,
            UriComponentsBuilder uriBuilder
    ) {

        DividaResponseDTO response = dividaService.criarDivida(dto);


        URI uri = uriBuilder.path("/api/v1/dividas/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DividaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(dividaService.listarTodas());
    }

    // Rota: DELETE /api/v1/dividas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        dividaService.excluirDivida(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (Padrão ouro para DELETE)
    }

    // Rota: DELETE /api/v1/dividas/todas
    // Usamos "/todas" no final para não confundir com a exclusão de uma única dívida
    @DeleteMapping("/todas")
    public ResponseEntity<Void> excluirTodas() {
        dividaService.excluirTodasAsDividas();
        return ResponseEntity.noContent().build();
    }
}
