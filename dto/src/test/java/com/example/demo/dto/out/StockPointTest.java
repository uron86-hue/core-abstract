package com.example.demo.dto.out;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockPointTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldNotValidateNullValues() {
    // when
    StockPoint stockPoint = StockPoint.builder().build();

    // then
    assertThat(validator.validate(stockPoint)).hasSize(3)
        .extracting("interpolatedMessage")
        .containsExactlyInAnyOrder("Color must be specified", "Size must be specified",
            "Quantity must be specified");
  }

  @Test
  void shouldNotValidateANegativeQuantity() {
    // when
    StockPoint stockPoint = StockPoint.builder()
        .color("BLACK")
        .size(BigInteger.valueOf(40))
        .quantity(BigInteger.valueOf(-1))
        .build();

    // then
    assertThat(validator.validate(stockPoint)).hasSize(1)
        .extracting("interpolatedMessage").first()
        .isEqualTo("Quantity must not be negative");
  }

  @Test
  void shouldNotValidateATooLittleSize() {
    // when
    StockPoint stockPoint = StockPoint.builder()
        .color("BLACK")
        .size(BigInteger.valueOf(17))
        .quantity(BigInteger.valueOf(1))
        .build();

    // then
    assertThat(validator.validate(stockPoint)).hasSize(1)
        .extracting("interpolatedMessage").first()
        .isEqualTo("Size must be greater than 27");
  }

  @Test
  void shouldNotValidateATooBigSize() {
    // when
    StockPoint stockPoint = StockPoint.builder()
        .color("BLACK")
        .size(BigInteger.valueOf(52))
        .quantity(BigInteger.valueOf(1))
        .build();

    // then
    assertThat(validator.validate(stockPoint)).hasSize(1)
        .extracting("interpolatedMessage").first()
        .isEqualTo("Size must be less than 52");
  }

  @Test
  void shouldNotValidateABadColor() {
    // when
    StockPoint stockPoint = StockPoint.builder()
        .color("ORANGE")
        .size(BigInteger.valueOf(44))
        .quantity(BigInteger.valueOf(1))
        .build();

    // then
    assertThat(validator.validate(stockPoint)).hasSize(1)
        .extracting("interpolatedMessage").first()
        .isEqualTo("Color must be black or blue");
  }
}
