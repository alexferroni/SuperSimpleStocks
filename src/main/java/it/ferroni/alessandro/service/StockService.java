/**
 * 
 */
package it.ferroni.alessandro.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;

/**
 * @author Alessandro Ferroni
 *
 */
public interface StockService {
	
	/**
	 * Selects stockTables
	 * @return
	 */
	public List<Stock> selectStockTable();
	
	/**
	 * Select ticker prices
	 * @return
	 */
	public TreeMap<StockSymbols, BigDecimal> selectTickerPrices();
	
	/**
	 * Calculates dividend yield for a given stock
	 * @param stock
	 * @param tickerPrice
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calculateDividendYield(Stock stock, BigDecimal tickerPrice) throws Exception;
	
	/**
	 * @param stockSymbol
	 * @param dividendYield
	 */
	public void saveDividendYield(StockSymbols stockSymbol, BigDecimal dividendYield);
	
	/**
	 * Select dividend yield data
	 * @return
	 */
	public TreeMap<StockSymbols, BigDecimal> selectDividendYield();
	
	/**
	 * Calculates a P/E rating for a given stock
	 * @param stock
	 * @param tickerPrice
	 * @param dividendYield
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calculatePERating(Stock stock, BigDecimal tickerPrice, BigDecimal dividendYield) throws Exception;
	
	/**
	 * Save stock pe rating
	 * @param stockSymbol
	 * @param pe_rating
	 */
	public void saveStockPERating(StockSymbols stockSymbol, BigDecimal pe_rating);
	
	/**
	 * Records a trade
	 * @param record
	 * @throws Exception
	 */
	public void stockTradeRecord(StockTrade record) throws Exception;
	
	/**
	 * Selects stock trade records
	 * @return
	 */
	public TreeMap<StockSymbols, List<StockTrade>> selectStockTradeRecords();
	
	/**
	 * Reset all stock trade records
	 */
	public void resetStockTradeRecords();
	
	/**
	 * Calculates the stock price for the given stock trade records list.
	 * All trade records in the list must be of the same stock symbol othervise an exception will be thrown
	 * @param stockSymbol
	 * @param stockTrades
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	public BigDecimal calculateStockPrice(StockSymbols stockSymbol, List<StockTrade> stockTrades, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
	
	/**
	 * Calculates GBCE All Share Index
	 * @param tradeMap
	 * @return
	 * @throws Exception
	 */
	public BigDecimal calculateGBCEAllShareIndex() throws Exception;

}
