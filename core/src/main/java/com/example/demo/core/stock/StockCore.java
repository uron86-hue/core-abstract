package com.example.demo.core.stock;

import com.example.demo.dto.out.Stock;

public interface StockCore {

  /**
   * Get the shoes stock and its state (EMPTY, SOME, FULL).
   *
   * @return the shoes stock
   */
  Stock get();

}
