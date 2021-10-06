package com.example.demo.controller.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import com.example.demo.core.stock.StockCoreNew;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.StockPoint;
import com.example.demo.facade.stock.StockFacade;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.ResponseEntity;

@TestInstance(Lifecycle.PER_CLASS)
class StockControllerTest {

  private StockController stockController;

  @BeforeAll
  void setUp() {
    StockFacade stockFacade = new StockFacade();
    stockFacade.register(1, new StockCoreNew());

    stockController = new StockController(stockFacade);
  }

  @Test
  void shouldGetStock() {
    // when
    ResponseEntity<Stock> stock = stockController.get(1);

    // then
    assertThat(stock.getStatusCode()).isEqualTo(OK);
    assertThat(stock.getBody()).isEqualTo(getExpectedInitialStock());
  }

  /**
   * In order to improve readability of unit tests, we create here sthe object for the initial
   * expected stock.
   *
   * @return the initial expected stock
   */
  private Stock getExpectedInitialStock() {
    List<StockPoint> stockPoints = List.of(
        StockPoint.builder()
            .color(Color.BLACK)
            .size(BigInteger.valueOf(40))
            .quantity(10)
            .build(),
        StockPoint.builder()
            .color(Color.BLACK)
            .size(BigInteger.valueOf(41))
            .quantity(0)
            .build(),
        StockPoint.builder()
            .color(Color.BLUE)
            .size(BigInteger.valueOf(39))
            .quantity(10)
            .build());

    return Stock.builder()
        .state("SOME")
        .shoes(stockPoints)
        .build();
  }
}
