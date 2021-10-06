package com.example.demo.facade.stock;

import com.example.demo.core.stock.StockCore;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class StockFacade {

  private final Map<Integer, StockCore> implementations = new HashMap<>();

  public StockCore get(Integer version) {
    return implementations.get(version);
  }

  public void register(Integer version, StockCore implementation) {
    this.implementations.put(version, implementation);
  }

}
