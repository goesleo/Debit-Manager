package com.leonardo.debt_manager_api.repository;

import com.leonardo.debt_manager_api.model.Parcela;
import com.leonardo.debt_manager_api.model.StatusParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, UUID> {


    List<Parcela> findByDividaId(UUID dividaId);


    List<Parcela> findByStatus(StatusParcela status);

    @Query("SELECT p FROM Parcela p WHERE EXTRACT(MONTH FROM p.dataVencimento) = :mes AND EXTRACT(YEAR FROM p.dataVencimento) = :ano")
    List<Parcela> buscarPorMesEAno(@Param("mes") int mes, @Param("ano") int ano);


    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE Parcela p SET p.status = 'ATRASADA' WHERE p.dataVencimento < :hoje AND p.status = 'PENDENTE'")
    void atualizarParcelasAtrasadas(@Param("hoje") LocalDate hoje);
}
