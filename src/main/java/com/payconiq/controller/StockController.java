package com.payconiq.controller;

import com.payconiq.controller.mapper.StockMapper;
import com.payconiq.dto.StockDTO;
import com.payconiq.exception.StockConstraintsViolationException;
import com.payconiq.model.Stock;
import com.payconiq.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@RestController
@RequestMapping("api/stocks")
public class StockController {

  private StockService stockService;

  @Autowired
  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity createStock(@Valid @RequestBody StockDTO stockDTO) throws StockConstraintsViolationException {
    Stock stock = StockMapper.makeStockFromDTO(stockDTO);
    return new ResponseEntity<>(StockMapper.makeStockDTO(stockService.createStock(stock)), HttpStatus.CREATED);
  }

  @GetMapping("/{stockId}")
  public ResponseEntity getStockById(@Valid @PathVariable Integer stockId) {
    return new ResponseEntity<>(StockMapper.makeStockDTO(stockService.getStock(stockId)), HttpStatus.OK);
  }

  @GetMapping
  public List<StockDTO> getAllStocks() {
    return StockMapper.makeStockDTOList(stockService.getStocks());
  }

  @PutMapping("/{stockId}")
  public ResponseEntity<StockDTO> updateStockPrice(@Valid @PathVariable Integer stockId, @RequestBody StockDTO stockDTO) {
    return new ResponseEntity<>(StockMapper.makeStockDTO(stockService.updateStock(stockId, stockDTO.getCurrentPrice())), HttpStatus.OK);
  }

  @ExceptionHandler
  void handleConstraintsViolationException(StockConstraintsViolationException e, HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler
  void handleEntityNotException(EntityNotFoundException e, HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value());
  }
}
