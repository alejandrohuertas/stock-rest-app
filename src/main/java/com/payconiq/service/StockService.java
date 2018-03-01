package com.payconiq.service;

import com.payconiq.exception.StockConstraintsViolationException;
import com.payconiq.model.Stock;

import java.util.List;

/**
 * Created by AHuertasA on 22/02/2018.
 */
public interface StockService {

  List<Stock> getStocks();

  Stock getStock(Integer id);

  Stock updateStock(Integer id, Double price);

  Stock createStock (Stock stock) throws StockConstraintsViolationException;

}
