package com.payconiq.dto;

/**
 * Created by AHuertasA on 22/02/2018.
 */
public class StockDTO {

  private Integer id;
  private String name;
  private Double currentPrice;
  private String lastUpdate;

  public StockDTO() {
  }
  private StockDTO(Integer id, String name, Double currentPrice, String lastUpdate) {
    this.id = id;
    this.name = name;
    this.currentPrice = currentPrice;
    this.lastUpdate = lastUpdate;
  }

  public String getName() {
    return name;
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public Integer getId() {
    return id;
  }

  public static class StockDTOBuilder {

    private Integer id;
    private String name;
    private Double currentPrice;
    private String lastUpdate;

    public StockDTOBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public StockDTO build() {
      return new StockDTO(id, name, currentPrice, lastUpdate);
    }

    public StockDTOBuilder withCurrentPrice(double price) {
      this.currentPrice = price;
      return this;
    }

    public StockDTOBuilder withId(Integer id) {
      this.id=id;
      return this;
    }

    public StockDTOBuilder withLastUpdate(String lastUpdate) {
      this.lastUpdate = lastUpdate;
      return this;
    }
  }
}
