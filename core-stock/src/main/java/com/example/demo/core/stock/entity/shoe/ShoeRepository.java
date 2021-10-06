package com.example.demo.core.stock.entity.shoe;

import com.example.demo.dto.in.ShoeFilter.Color;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to query shoe entities.
 */
public interface ShoeRepository extends JpaRepository<ShoeEntity, Long> {

  /**
   * Find a shoe by its color and its size.
   *
   * @param color the color of the shoe
   * @param size  the size of the shoe
   * @return an optional shoe
   */
  Optional<ShoeEntity> findByColorAndSize(Color color, BigInteger size);
}
