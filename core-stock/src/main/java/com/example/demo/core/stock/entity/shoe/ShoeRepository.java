package com.example.demo.core.stock.entity.shoe;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to query shoe entities.
 */
public interface ShoeRepository extends JpaRepository<ShoeEntity, Long> {

}
