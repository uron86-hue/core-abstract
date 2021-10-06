package com.example.demo.core.stock;

import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.StockPoint;

public interface StockCore {

  /**
   * Get the shoes stock and its state (EMPTY, SOME, FULL).
   *
   * @return the shoes stock
   */
  Stock get();

  /**
   * Update the stock for one model of shoe.
   *
   * @param stockPoint all information of the stock to update
   */
  void update(StockPoint stockPoint);

}
