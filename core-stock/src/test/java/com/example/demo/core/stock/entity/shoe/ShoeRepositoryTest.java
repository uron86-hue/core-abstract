package com.example.demo.core.stock.entity.shoe;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.DataLoadingTestManager;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DatabaseSetup("shoeData.xml")
class ShoeRepositoryTest extends DataLoadingTestManager {

  @Autowired
  private ShoeRepository shoeRepository;

  @Test
  void shouldFindAShoeByItsColorAndItsSize() {
    // when
    Optional<ShoeEntity> shoe = shoeRepository
        .findByColorAndSize(Color.BLACK, BigInteger.valueOf(41));

    // then
    assertThat(shoe).isPresent().get().hasFieldOrPropertyWithValue("id", 2L);
  }
}
