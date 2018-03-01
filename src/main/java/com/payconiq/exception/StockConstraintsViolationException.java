package com.payconiq.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Some constraints are violated ...")
public class StockConstraintsViolationException extends Exception {
  public StockConstraintsViolationException(String message) {
    super(message);
        
  }
}
