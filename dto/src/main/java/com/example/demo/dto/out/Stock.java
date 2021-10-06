package com.example.demo.dto.out;

import com.example.demo.dto.out.Stock.StockBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = StockBuilder.class)
public class Stock {

  String state;
  List<StockPoint> shoes;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockBuilder {

  }


}
