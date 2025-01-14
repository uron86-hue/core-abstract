package com.example.demo.controller.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import com.example.demo.core.stock.StockCoreNew;
import com.example.demo.core.stock.entity.shoe.ShoeEntity;
import com.example.demo.core.stock.entity.shoe.ShoeRepository;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.StockPoint;
import com.example.demo.facade.stock.StockFacade;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class StockControllerTest {

  private StockController stockController;
  private ShoeRepository shoeRepository;

  private List<ShoeEntity> shoeEntities;

  @BeforeAll
  void setUp() {
    StockFacade stockFacade = new StockFacade();
    shoeRepository = mock(ShoeRepository.class);
    stockFacade.register(1, new StockCoreNew(shoeRepository));

    stockController = new StockController(stockFacade);
  }

  @BeforeEach
  void initData() {
    shoeEntities = List.of(
        new ShoeEntity(BigInteger.valueOf(40), Color.BLACK, BigInteger.valueOf(10)),
        new ShoeEntity(BigInteger.valueOf(41), Color.BLACK, BigInteger.valueOf(0)),
        new ShoeEntity(BigInteger.valueOf(39), Color.BLUE, BigInteger.valueOf(10))
    );
  }

  @Test
  void shouldGetStock() {
    // given
    when(shoeRepository.findAll()).thenReturn(shoeEntities);

    // when
    ResponseEntity<Stock> stock = stockController.get(1);

    // then
    assertThat(stock.getStatusCode()).isEqualTo(OK);
    assertThat(stock.getBody()).isEqualTo(getExpectedInitialStock());
  }

  @Test
  void shouldCreateAStockPoint() {
    // given
    Color blueColor = Color.BLUE;
    BigInteger size36 = BigInteger.valueOf(36);
    BigInteger quantity = BigInteger.valueOf(7);
    StockPoint stockPointToCreate = StockPoint.builder()
        .color(blueColor.name())
        .size(size36)
        .quantity(quantity)
        .build();
    ShoeEntity shoe = new ShoeEntity(size36, blueColor, quantity);

    when(shoeRepository.sumAllQuantities()).thenReturn(BigInteger.valueOf(20));
    when(shoeRepository.findByColorAndSize(blueColor, size36)).thenReturn(Optional.empty());
    ArgumentCaptor<ShoeEntity> shoeCaptor = ArgumentCaptor.forClass(ShoeEntity.class);
    when(shoeRepository.save(shoeCaptor.capture())).thenReturn(shoe);

    // when
    ResponseEntity<Void> result = stockController.update(1, stockPointToCreate);

    // then
    assertThat(result.getStatusCode()).isEqualTo(OK);
    assertThat(shoeCaptor.getValue()).usingRecursiveComparison().isEqualTo(shoe);
  }

  @Test
  void shouldUpdateAStockPoint() {
    // given
    Color blueColor = Color.BLUE;
    BigInteger size36 = BigInteger.valueOf(36);
    BigInteger quantity = BigInteger.valueOf(7);
    StockPoint stockPointToUpdate = StockPoint.builder()
        .color(blueColor.name())
        .size(size36)
        .quantity(quantity)
        .build();
    ShoeEntity shoe = new ShoeEntity(size36, blueColor, quantity);

    when(shoeRepository.sumAllQuantities()).thenReturn(BigInteger.valueOf(20));
    when(shoeRepository.findByColorAndSize(blueColor, size36)).thenReturn(Optional.of(shoe));
    ArgumentCaptor<ShoeEntity> shoeCaptor = ArgumentCaptor.forClass(ShoeEntity.class);
    when(shoeRepository.save(shoeCaptor.capture())).thenReturn(shoe);

    // when
    ResponseEntity<Void> result = stockController.update(1, stockPointToUpdate);

    // then
    assertThat(result.getStatusCode()).isEqualTo(OK);
    assertThat(shoeCaptor.getValue()).usingRecursiveComparison().isEqualTo(shoe);
  }

  @Test
  void shouldNotUpdateStock_whenGlobalCapacityIsExceeded() {
    // given
    Color blueColor = Color.BLUE;
    BigInteger size36 = BigInteger.valueOf(36);
    BigInteger quantity = BigInteger.valueOf(7);
    StockPoint stockPointToUpdate = StockPoint.builder()
        .color(blueColor.name())
        .size(size36)
        .quantity(quantity)
        .build();

    when(shoeRepository.sumAllQuantities()).thenReturn(BigInteger.valueOf(24));

    // when
    assertThatThrownBy(() -> stockController.update(1, stockPointToUpdate))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Global capacity (31 instead of 30) is exceeded with quantity : 7");
  }

  /**
   * In order to improve readability of unit tests, we create here the object for the initial
   * expected stock.
   *
   * @return the initial expected stock
   */
  private Stock getExpectedInitialStock() {
    List<StockPoint> stockPoints = List.of(
        StockPoint.builder()
            .color("BLACK")
            .size(BigInteger.valueOf(40))
            .quantity(BigInteger.valueOf(10))
            .build(),
        StockPoint.builder()
            .color("BLACK")
            .size(BigInteger.valueOf(41))
            .quantity(BigInteger.ZERO)
            .build(),
        StockPoint.builder()
            .color("BLUE")
            .size(BigInteger.valueOf(39))
            .quantity(BigInteger.valueOf(10))
            .build());

    return Stock.builder()
        .state("SOME")
        .shoes(stockPoints)
        .build();
  }
}
