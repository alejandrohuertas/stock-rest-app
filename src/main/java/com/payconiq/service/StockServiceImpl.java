package com.payconiq.service;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.payconiq.dao.StockRepository;
import com.payconiq.exception.StockConstraintsViolationException;
import com.payconiq.model.Stock;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@Service
public class StockServiceImpl implements StockService {

  private final StockRepository stockRepository;
  private static final Logger LOG = LoggerFactory.getLogger(StockServiceImpl.class);

  public StockServiceImpl(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  @Override
  public List<Stock> getStocks() {
    return stockRepository.findAll();
  }

  @Override
  public Stock getStock(Integer id) {
    Stock stock =  stockRepository.findOne(id);
    if (stock!=null)
      return stock;
    else{
      String errorMessage = MessageFormat.format("Stock with id {0} was not found", id);
      LOG.error(errorMessage);
      throw new EntityNotFoundException(errorMessage);
    }
  }

  @Override
  public Stock updateStock(Integer id, Double price) {
    {
      Stock stock = stockRepository.findOne(id);
      if (stock != null) {
        stock.setCurrentPrice(price);
        stockRepository.save(stock);
      }else{
        String errorMessage = MessageFormat.format("Could not find stock with id: {0}" , id);
        LOG.error(errorMessage);
        throw new EntityNotFoundException(errorMessage);
      }
      return stock;
    }
  }

  @Override
  public Stock createStock(Stock stock) throws StockConstraintsViolationException {
    Stock newStock;
    try
    {
        newStock = stockRepository.save(stock);
    }
    catch (DataIntegrityViolationException e) {
      LOG.error("Some constraints violations are thrown due to stock creation", e);
      throw new StockConstraintsViolationException(e.getMessage());
    }
    return newStock;
  }
}
