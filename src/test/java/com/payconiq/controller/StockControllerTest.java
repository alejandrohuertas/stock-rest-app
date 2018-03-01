package com.payconiq.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.payconiq.controller.mapper.StockMapper;
import com.payconiq.dto.StockDTO;
import com.payconiq.exception.StockConstraintsViolationException;
import com.payconiq.model.Stock;
import com.payconiq.service.StockService;

/**
 * Created by alejandro on 2/27/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class StockControllerTest {

  private MockMvc mockMvc;

  @Mock
  private StockService stockService;

  @InjectMocks
  private StockController mainController = new StockController(stockService);

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
  }

  @Test
  public void givenAValidStockWhenCreatingStickThenCreateItSuccessfully() throws Exception {
    StockDTO stockDTO = new StockDTO.StockDTOBuilder().withName("Stock 1").withCurrentPrice(23.67).build();
    Stock newStock = aStockWithIdFromDTO(1, stockDTO);
    when(stockService.createStock(any(Stock.class))).thenReturn(newStock);

    String json = new Gson().toJson(stockDTO);
    mockMvc.perform(post("/api/stocks").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  public void givenAInvalidConstraintsStockWhenCreatingStickThenReturnError() throws Exception {
    StockDTO stockDTO = new StockDTO.StockDTOBuilder().withName("Stock 1").withCurrentPrice(23.67).build();
    when(stockService.createStock(any(Stock.class))).thenThrow(new StockConstraintsViolationException("Stock has fields that violates table constraints"));

    String json = new Gson().toJson(stockDTO);
    mockMvc.perform(post("/api/stocks").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void givenValidPriceAndIdWhenUpdatingAStockThenUpdateItSuccessfully() throws Exception {
    Integer id = 3;
    Double newPrice = 2356.67;
    StockDTO stockDTO = new StockDTO.StockDTOBuilder().withCurrentPrice(newPrice).build();
    Stock updatedStock = aStockWithIdFromDTO(id, stockDTO);

    String json = new Gson().toJson(stockDTO);
    when(stockService.updateStock(id, newPrice)).thenReturn(updatedStock);
    mockMvc.perform(put("/api/stocks/" + id).accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
  }

  @Test
  public void givenInvalidIdWhenUpdatingAStockThenResponseNotFound() throws Exception {
    Integer id = 5;
    Double newPrice = 2450.67;
    StockDTO stockDTO = new StockDTO.StockDTOBuilder().withCurrentPrice(newPrice).build();

    String json = new Gson().toJson(stockDTO);
    when(stockService.updateStock(id, newPrice)).thenThrow(new EntityNotFoundException("The stock does not exist"));
    mockMvc.perform(put("/api/stocks/" + id).accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenAValidIdWhenGettingAStockThenReturnStock() throws Exception {
    Integer id = 4;
    Stock stock = aStockWithId(id);
    StockDTO stockDTO = StockMapper.makeStockDTO(stock);

    String json = new Gson().toJson(stockDTO);
    when(stockService.getStock(id)).thenReturn(stock);
    mockMvc.perform(get("/api/stocks/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
        content().string(containsString(json)));
  }

  @Test
  public void givenAInvalidIdWhenGettingAStockThenReturnErrorResponse() throws Exception {
    Integer id = 4;

    when(stockService.getStock(id)).thenThrow(new EntityNotFoundException("Stock with id " + id + " doesnt Exist"));
    mockMvc.perform(get("/api/stocks/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void givenWhenGettingAllStockThenReturnAllStocks() throws Exception {
    Integer id = 4;
    Stock stock1 = aStockWithId(id);
    Stock stock2 = aStockWithId(id);
    Stock stock3 = aStockWithId(id);
    Stock stock4 = aStockWithId(id);

    List<Stock> stockList = Lists.newArrayList(stock1, stock2, stock3, stock4);

    List<StockDTO> stockDTOList = StockMapper.makeStockDTOList(stockList);

    String json = new Gson().toJson(stockDTOList);
    when(stockService.getStocks()).thenReturn(stockList);
    mockMvc.perform(get("/api/stocks").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
        content().string(containsString(json)));
  }

  private Stock aStockWithId(Integer id) {
    Stock stock = new Stock("Test Name for Stock " + id, Math.random());
    stock.setId(id);
    return stock;
  }

  private Stock aStockWithIdFromDTO(int id, StockDTO stockDTO) {
    Stock newStock = StockMapper.makeStockFromDTO(stockDTO);
    newStock.setId(id);
    return newStock;
  }

}