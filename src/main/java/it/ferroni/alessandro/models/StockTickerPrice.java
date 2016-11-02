/**
 * 
 */
package it.ferroni.alessandro.models;

import java.math.BigDecimal;

import it.ferroni.alessandro.constants.StockSymbols;

/**
 * @author Alessandro Ferroni
 *
 */
public class StockTickerPrice {
	
	private StockSymbols stockSymbol;
	private BigDecimal price;
	
	public StockTickerPrice(StockSymbols stockSymbol, BigDecimal price) {
		this.stockSymbol = stockSymbol;
		this.price = price;
	}

	public StockSymbols getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(StockSymbols stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	

}
