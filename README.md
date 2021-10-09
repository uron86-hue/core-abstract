# Explanations

## Features

Here, we want to access two features:

- Get the global stock of shoes
- Patch this stock

### Get the global stock of shoes

For this feature, we want to have some information:

- All shoe models (a shoe model is characterized by a color and a size) with their respective
  quantities in stock (the respective DTO is StockPoint - see `com.example.demo.dto.out.StockPoint`)
- The state of the stock (if it is empty, full or not full and not empty - 'SOME' value)

The DTO we return for this feature is Stock (see `com.example.demo.dto.out.Stock`)

### Patch the stock

For this feature, I made the choice to patch the stock only for one shoe model at a time. For each
patch, we verify that the global stock does not exceed the global capacity (30 shoes).

We have to use the previous StockPoint DTO to patch the stock. Here is an example:

```json
{
  "color": "BLACK",
  "size": 36,
  "quantity": 11
}
```

## Embedded database

To store all shoe models, I create a ShoeEntity
(see `com.example.demo.core.stock.entity.shoe.ShoeEntity`) and its repository, and I made the choice
of an HSQL embedded database (see configuration in `com.example.demo.config.DatabaseConfig`).

## DTO validation

In order to validate the StockPoint DTO, javax.validation is used.

@Valid annotation in the controller (
see `com.example.demo.controller.stock.StockController`) and several annotations (@NotNull, @Min,
@Max, @PositiveOrZero, @Pattern) in StockPoint are used.

## Test strategy

Unit tests are divided in 3 categories:

- Functional tests of the features (see `com.example.demo.controller.stock.StockControllerTest`).
  Mockito is used to isolate the database part
- Database tests (with dbUnit and springtest-db-unit) for the repository accesses (
  see `com.example.demo.core.stock.entity.shoe.ShoeRepositoryTest`)
- DTO validation tests (see `com.example.demo.dto.out.StockPointTest`)

JUnit 5 is used for all these unit tests

# Validating the application

To run the application, you can run the following command in the root folder of the project:

```shell script
mvn clean install && java -jar controller/target/controller-1.0.jar
```

## Get stock feature

To test this feature, you can call:

```shell script
curl -X GET "http://localhost:8080/stock" -H "version: 1"
```

which should answer this default initial stock (see `com.example.demo.core.stock.StockCoreNew.get`):

```json
{
  "state": "SOME",
  "shoes": [
    {
      "color": "BLACK",
      "size": 40,
      "quantity": 10
    },
    {
      "color": "BLACK",
      "size": 41,
      "quantity": 0
    },
    {
      "color": "BLUE",
      "size": 39,
      "quantity": 10
    }
  ]
}
```

## Patch stock feature

To test this feature, you can call:

```shell script
curl -X PATCH "http://localhost:8080/stock" -H "version: 1"
```

which should just return a http code 200 (see `com.example.demo.core.stock.StockCoreNew.update`):

To verify the update of the stock, you can call again the get stock feature

## Postman

If you prefer, you can import Postman collection which is at the root of the project