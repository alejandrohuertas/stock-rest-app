package com.payconiq.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by AHuertasA on 22/02/2018.
 */
@Entity
@Table(name = "stock", uniqueConstraints = @UniqueConstraint(name = "uc_name", columnNames = { "name" }))
public class Stock {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Double currentPrice;

  @Column(nullable = false)
  private Instant lastUpdate;

  public Stock(String name, Double currentPrice) {
    this.name = name;
    this.currentPrice = currentPrice;
    this.lastUpdate = Instant.now();
  }

  public Stock() {
  }

  public void setCurrentPrice(Double currentPrice) {
    this.currentPrice = currentPrice;
    this.lastUpdate = Instant.now();
  }

  public String getName() {
    return name;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

  public Instant getLastUpdate() {
    return lastUpdate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
