package com.payconiq.controller.mapper;

import com.payconiq.dto.StockDTO;
import com.payconiq.model.Stock;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by AHuertasA on 22/02/2018.
 */
public class StockMapper {
  public static Stock makeStockFromDTO(StockDTO stockDTO) {

    return new Stock(stockDTO.getName(), stockDTO.getCurrentPrice());
  }

  public static StockDTO makeStockDTO(Stock stock) {
    return new StockDTO.StockDTOBuilder().withName(stock.getName()).withCurrentPrice(stock.getCurrentPrice()).withId(stock.getId())
        .withLastUpdate(stock.getLastUpdate().toString()).build();
  }

  public static List<StockDTO> makeStockDTOList(List<Stock> stocks) {
    return stocks.stream().map(StockMapper::makeStockDTO).collect(Collectors.toList());
  }
}
