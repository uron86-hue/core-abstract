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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    // Check is the global shoes capacity is respected
    checkGlobalCapacity(optionalShoe, stockPoint.getQuantity());

    // Create or update the shoe and save it into database
    ShoeEntity shoeEntity = optionalShoe
        .orElse(new ShoeEntity(stockPoint.getSize(), Color.valueOf(stockPoint.getColor())));
    shoeEntity.setQuantity(stockPoint.getQuantity());

    shoeRepository.save(shoeEntity);
  }

  /**
   * Check if the global capacity is respected with the updated quantity.
   *
   * @param optionalShoe    an optional representing the potential previous stock of the updated
   *                        shoe model
   * @param updatedQuantity the new quantity to update
   * @throws IllegalArgumentException if the global capacity is exceeded with the new quantity
   */
  private void checkGlobalCapacity(Optional<ShoeEntity> optionalShoe, BigInteger updatedQuantity) {
    // Sum the quantities of all shoes
    BigInteger sumOfAllShoes = shoeRepository.sumAllQuantities();

    // To check if the sum of all shoes does not exceed global capacity,
    // we subtract quantity of updated shoe model (if it exists - it does not exist if it is a new model)
    // and we add the quantity of the update
    BigInteger initialQuantity =
        optionalShoe.isPresent() ? optionalShoe.get().getQuantity() : BigInteger.ZERO;
    BigInteger sumOfAllShoesAfterUpdate = sumOfAllShoes.subtract(initialQuantity)
        .add(updatedQuantity);
    if (sumOfAllShoesAfterUpdate.compareTo(MAXIMUM_CAPACITY) > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Global capacity (" + sumOfAllShoesAfterUpdate + " instead of " + MAXIMUM_CAPACITY
              + ") is exceeded with quantity : " + updatedQuantity);
    }
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
