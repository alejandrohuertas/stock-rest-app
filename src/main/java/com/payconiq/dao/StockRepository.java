package com.payconiq.dao;

import com.payconiq.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

  List<Stock> findAll();
}
