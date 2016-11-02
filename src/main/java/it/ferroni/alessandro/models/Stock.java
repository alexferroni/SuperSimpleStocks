/**
 * 
 */
package it.ferroni.alessandro.models;

import java.io.Serializable;
import java.math.BigDecimal;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.constants.StockType;

/**
 * 
 */

/**
 * @author Alessandro Ferroni
 * 
 * This should be an entity bean, annotated with javax.persistence annotations in order to be used with Hibernate or another entity manager.
 * Field 'stockSymbol' is the @Id of the table
 *
 */
public class Stock implements Serializable {
	private static final long serialVersionUID = 8619537347607764465L;

	private StockSymbols stockSymbol;
	private StockType type;
	private BigDecimal lastDividend;
	private BigDecimal fixedDividend;
	private Long parValue;
	
	/**
	 * Empty constructor
	 */
	public Stock() {
	}
	
	public Stock(StockSymbols stockSymbol, StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, Long parValue) {
		this.stockSymbol = stockSymbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}
	
	public StockSymbols getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(StockSymbols stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	
	public StockType getType() {
		return type;
	}
	public void setType(StockType type) {
		this.type = type;
	}
	public BigDecimal getLastDividend() {
		return lastDividend;
	}
	public void setLastDividend(BigDecimal lastDividend) {
		this.lastDividend = lastDividend;
	}
	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}
	public void setFixedDividend(BigDecimal fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	public Long getParValue() {
		return parValue;
	}
	public void setParValue(Long parValue) {
		this.parValue = parValue;
	}
	
	
}
