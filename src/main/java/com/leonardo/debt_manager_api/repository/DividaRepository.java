package com.leonardo.debt_manager_api.repository;

import com.leonardo.debt_manager_api.model.Divida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DividaRepository extends JpaRepository<Divida, UUID> {

}