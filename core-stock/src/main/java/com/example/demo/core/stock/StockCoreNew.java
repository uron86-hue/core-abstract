package com.example.demo.core.stock;

import static java.util.stream.Collectors.toList;

import com.example.demo.core.Implementation;
import com.example.demo.core.stock.entity.shoe.ShoeEntity;
import com.example.demo.core.stock.entity.shoe.ShoeRepository;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Stock;
import com.example.demo.dto.out.StockPoint;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Implementation(version = 1)
public class StockCoreNew extends AbstractStockCore {

  private static final String EMPTY = "EMPTY";
  private static final String SOME = "SOME";
  private static final String FULL = "FULL";
  private static final BigInteger MAXIMUM_CAPACITY = BigInteger.valueOf(30);

  private final ShoeRepository shoeRepository;

  public StockCoreNew(ShoeRepository shoeRepository) {
    this.shoeRepository = shoeRepository;
  }

  @Override
  public Stock get() {
    // Get all shoes
    List<ShoeEntity> shoeEntities = shoeRepository.findAll();

    // Convert shoes to stock points
    List<StockPoint> stockPoints = shoeEntities.stream().map(this::buildStockPoint)
        .collect(toList());

    // Build and return the Stock Data Transfer Object
    return Stock.builder()
        .state(getState(stockPoints))
        .shoes(stockPoints)
        .build();
  }

  @Override
  public void update(StockPoint stockPoint) {
    // Get the shoe by its color and its size if it exists
    Optional<ShoeEntity> optionalShoe = shoeRepository
        .findByColorAndSize(Color.valueOf(stockPoint.getColor()), stockPoint.getSize());

    // Create or update the shoe and save it into database
    ShoeEntity shoeEntity = optionalShoe
        .orElse(new ShoeEntity(stockPoint.getSize(), Color.valueOf(stockPoint.getColor())));
    shoeEntity.setQuantity(stockPoint.getQuantity());

    shoeRepository.save(shoeEntity);
  }

  /**
   * Convert a shoe entity into a stock point.
   *
   * @param shoeEntity the shoe entity to convert
   * @return a stock point
   */
  private StockPoint buildStockPoint(ShoeEntity shoeEntity) {
    return StockPoint.builder()
        .color(shoeEntity.getColor().name())
        .size(shoeEntity.getSize())
        .quantity(shoeEntity.getQuantity())
        .build();
  }

  /**
   * Get the state of the stock.
   * <p>
   * The stock is EMPTY if there is no shoe. The stock is FULL if shoes have reached the maximum
   * capacity (30). Otherwise, the state of the stock is SOME.
   * </p>
   *
   * @param stockPoints all the shoes stock points
   * @return the state of the stock
   */
  private String getState(List<StockPoint> stockPoints) {
    String state = SOME;

    BigInteger shoesQuantity = stockPoints.stream().map(StockPoint::getQuantity)
        .reduce(BigInteger::add).orElse(BigInteger.ZERO);

    if (shoesQuantity.equals(BigInteger.ZERO)) {
      state = EMPTY;
    } else if (shoesQuantity.equals(MAXIMUM_CAPACITY)) {
      state = FULL;
    }

    return state;
  }
}
