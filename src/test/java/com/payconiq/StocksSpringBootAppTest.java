package com.payconiq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.payconiq.controller.StockController;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by AHuertasA on 28/02/2018.
 */



@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksSpringBootAppTest {

  @Autowired
  private StockController controller;

  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
}


