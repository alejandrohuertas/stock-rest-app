package com.payconiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@SpringBootApplication
class StocksSpringBootApp extends WebMvcConfigurerAdapter{

  public static void main(String[] args)
  {
    SpringApplication.run(StocksSpringBootApp.class, args);
  }


}
