package com.example.demo.controller.stock;

import com.example.demo.dto.out.Stock;
import com.example.demo.facade.stock.StockFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/stock")
@RequiredArgsConstructor
public class StockController {

  private final StockFacade stockFacade;

  /**
   * Get the shoes stock and its state (EMPTY, SOME, FULL).
   *
   * @param version version of the implementation we want to use (only one implementation in this
   *                exercise)
   * @return the shoes stock
   */
  @GetMapping
  public ResponseEntity<Stock> get(@RequestHeader Integer version) {
    return ResponseEntity.ok(stockFacade.get(version).get());
  }

}
