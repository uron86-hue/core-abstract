package com.example.demo.dto.out;

import com.example.demo.dto.out.StockPoint.StockPointBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigInteger;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = StockPointBuilder.class)
public class StockPoint {

  @NotNull(message = "Color must be specified")
  @Pattern(regexp = "BLACK|BLUE", message = "Color must be black or blue")
  String color;

  @NotNull(message = "Size must be specified")
  @Min(value = 28, message = "Size must be greater than 27")
  @Max(value = 51, message = "Size must be less than 52")
  BigInteger size;

  @NotNull(message = "Quantity must be specified")
  @PositiveOrZero(message = "Quantity must not be negative")
  BigInteger quantity;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockPointBuilder {

  }


}
