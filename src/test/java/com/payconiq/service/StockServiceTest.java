package com.payconiq.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityNotFoundException;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.payconiq.dao.StockRepository;
import com.payconiq.exception.StockConstraintsViolationException;
import com.payconiq.model.Stock;

/**
 * Created by alejandro on 2/22/18.
 */
public class StockServiceTest {

  @Mock
  private StockRepository stockRepository;

  @Before
  public  void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void givenValidRequestWhenSearchingForAllStocksThenReturnAllStocks() {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);
    Stock stock1 = aStock("test name");
    Stock stock2 = aStock("test name");
    Stock stock3 = aStock("test name");
    List<Stock> stocksExpected = Lists.newArrayList(stock1, stock2, stock3);


    when(stockRepository.findAll()).thenReturn(stocksExpected);

    //Act
    List<Stock> stocksReturned = stockService.getStocks();


    //Assert
    assertEquals(stocksExpected.size(), stocksReturned.size());
    Assert.assertArrayEquals(stocksExpected.toArray(), stocksReturned.toArray());
  }


  @Test
  public void givenValidIdWhenSearchingForAnStockThenReturnTheMatchingStock() {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);
    Stock stockExpected = aStockWithId(1,"test name");
    Integer id = stockExpected.getId();
    when(stockRepository.findOne(id)).thenReturn(stockExpected);

    //Act
    Stock stockReturned = stockService.getStock(id);

    //Assert
    assertEquals(stockExpected, stockReturned);
  }

  @Test
  public void givenAStockWhenCreatingStockThenSaveIt() throws StockConstraintsViolationException {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);

    Stock stock = aStock("New Stock In trade");
    //Act
    stockService.createStock(stock);
    //Assert
    Mockito.verify(stockRepository).save(stock);
  }

  @Test(expected = StockConstraintsViolationException.class)
  public void givenAStockWithViolatingConstraintsFieldsWhenCreatingStockThenThrowException() throws StockConstraintsViolationException {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);

    Stock stock = aStock("New Stock In trade");
    //Act
    when(stockRepository.save(stock)).thenThrow(new DataIntegrityViolationException("Some constraint has been violated"));
    stockService.createStock(stock);

  }


  @Test
  public void givenAValidPriceWhenUpdatingAStockThenUpdateIt() throws InterruptedException {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);

    int id = 2;
    Stock stock = aStockWithId(id, "Stock to Update");
    Double newPrice = 345.6670;
    when(stockRepository.findOne(id)).thenReturn(stock);
    Instant stockLastUpdate = stock.getLastUpdate();
    //Act
    TimeUnit.MILLISECONDS.sleep(100); // sleep for 100 milliseconds
    Stock updatedStock =stockService.updateStock(id, newPrice );

    //Assert
    assertEquals(newPrice, updatedStock.getCurrentPrice());
    assertNotEquals(stockLastUpdate, updatedStock.getLastUpdate());
  }

  @Test (expected = EntityNotFoundException.class)
  public void givenAnInvalidIdWhenUpdatingAStockThenThrowException() throws StockConstraintsViolationException, InterruptedException {

    //Arrange
    StockService stockService = new StockServiceImpl(stockRepository);

    int id = 2;
    Double newPrice = 345.6670;
    when(stockRepository.findOne(id)).thenReturn(null);
    //Act
    TimeUnit.MILLISECONDS.sleep(100); // sleep for 100 milliseconds
    stockService.updateStock(id, newPrice );

  }


  private Stock aStock(String name) {
    return new Stock(name, Math.random());
  }


  private Stock aStockWithId(int id, String name) {
    Stock stock = aStock(name);
    stock.setId(id);
    return stock;
  }

}