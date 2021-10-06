package com.example.demo.dto.out;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.StockPoint.StockPointBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = StockPointBuilder.class)
public class StockPoint {

  Color color;
  BigInteger size;
  BigInteger quantity;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockPointBuilder {

  }


}
