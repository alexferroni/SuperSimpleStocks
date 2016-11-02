/**
 * 
 */
package it.ferroni.alessandro.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;

/**
 * @author Alessandro Ferroni
 *
 */
public interface StockManager {
	
	
	/**
	 * Load stock table
	 * @return
	 */
	public List<Stock> selectStockTable();
	
	/**
	 * Loads all ticker prices
	 * @return
	 */
	public TreeMap<StockSymbols, BigDecimal> selectTickerPrices();
	
	/**
	 * Save Dividend Yield data
	 * @param dividendYieldMap
	 */
	public void saveDividendYield(StockSymbols stockSymbol, BigDecimal dividendYeld);
	
	/**
	 * Loads Dividend Yield data
	 * @return
	 */
	public TreeMap<StockSymbols, BigDecimal> selectDividendYield();
	
	/**
	 * @param stockPERatingMap
	 */
	public void saveStockPERating(StockSymbols stockSymbol, BigDecimal pe_rating);
	
	/**
	 * @return
	 */
	public TreeMap<StockSymbols, BigDecimal> selectStockPERating();
	
	/**
	 * Save a single stock trade record
	 * @param record
	 */
	public void saveStockTrade(StockTrade record);
	
	/**
	 * @return
	 */
	public TreeMap<StockSymbols, List<StockTrade>> selectStockTradeRecords();
	
	/**
	 * Reset stock trade records
	 */
	public void resetStockTradeRecords();

}
