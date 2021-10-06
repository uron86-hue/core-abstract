package com.example.demo.core.stock;

import com.example.demo.core.Implementation;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.StockPoint;
import java.math.BigInteger;
import java.util.List;

@Implementation(version = 1)
public class StockCoreNew extends AbstractStockCore {

  private static final String EMPTY = "EMPTY";
  private static final String SOME = "SOME";
  private static final String FULL = "FULL";
  private static final int MAXIMUM_CAPACITY = 30;

  @Override
  public Stock get() {
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
        .state(getState(stockPoints))
        .shoes(stockPoints)
        .build();
  }

  private String getState(List<StockPoint> stockPoints) {
    String state = SOME;

    int shoesQuantity = stockPoints.stream().map(StockPoint::getQuantity)
        .reduce(Integer::sum).orElse(0);

    if (shoesQuantity == 0) {
      state = EMPTY;
    } else if (shoesQuantity == MAXIMUM_CAPACITY) {
      state = FULL;
    }

    return state;
  }
}
