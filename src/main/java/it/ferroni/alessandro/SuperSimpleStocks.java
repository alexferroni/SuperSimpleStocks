/**
 * 
 */
package it.ferroni.alessandro;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.ferroni.alessandro.constants.ConfigurationConstants;
import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;
import it.ferroni.alessandro.service.StockService;
import it.ferroni.alessandro.service.impl.StockServiceImpl;
import it.ferroni.alessandro.utils.StockUtils;

/**
 * @author Alessandro Ferroni
 *
 */
public class SuperSimpleStocks {
	
	private static Logger LOGGER;
	
	private StockService stockService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			SuperSimpleStocks.class.newInstance().run();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public SuperSimpleStocks() {
		LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		LOGGER.setLevel(Level.INFO);
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s %n");
		
		stockService = new StockServiceImpl();
	}
	
	public void run() {
		calculateDividentYield();
		calculatePERating();
		recordTrades();
		calculateStockPrices();
		calculateGBCEAllShareIndex();
	}
	
	/**
	 * Calculates stock dividend yield
	 */
	public void calculateDividentYield() {
		LOGGER.info("\nCALCULATING DIVIDEND YIELD");
		//load stockTable form manager
		List<Stock> stockTable = stockService.selectStockTable();
		
		//load ticker price
		TreeMap<StockSymbols, BigDecimal> stockTickerPrices = stockService.selectTickerPrices();
		
		for (Stock stock: stockTable) {
			try {
				BigDecimal tickerPrice =  stockTickerPrices.get(stock.getStockSymbol());
				BigDecimal dividendYield = stockService.calculateDividendYield(stock, tickerPrice);
				
				//save and print dividend yields
				LOGGER.info("Dividend Yield for stock " + stock.getStockSymbol() + " is " + dividendYield);
				stockService.saveDividendYield(stock.getStockSymbol(), dividendYield);
			}
			catch (Exception e) {
				LOGGER.severe(e.getMessage());
			}
		}
	}
	
	/**
	 * Calculates stock P/E rating
	 */
	public void calculatePERating() {
		LOGGER.info("\nCALCULATING PE RATING");
		
		//load stockTable form DB. 
		List<Stock> stockTable = stockService.selectStockTable();
		
		//load the dividend yield data 
		TreeMap<StockSymbols, BigDecimal> dividendYieldMap = stockService.selectDividendYield();
		
		//load stock ticker prices
		TreeMap<StockSymbols, BigDecimal> stockTickerPrices = stockService.selectTickerPrices();
		
		BigDecimal pe_rating;
		
		for (Stock stock: stockTable) {
			try {
				BigDecimal tickerPrice = stockTickerPrices.get(stock.getStockSymbol());
				BigDecimal dividendYield = dividendYieldMap.get(stock.getStockSymbol());
				pe_rating = stockService.calculatePERating(stock, tickerPrice, dividendYield);
				//save and print pe rating value
				
				LOGGER.info("PE Rating for stock " + stock.getStockSymbol() + " is " + pe_rating);
				stockService.saveStockPERating(stock.getStockSymbol(), pe_rating);
			}
			catch (Exception e) {
				LOGGER.severe(e.getMessage());
			}
		}
	}
	
	/**
	 * Simulate and record stock trades
	 */
	public void recordTrades() {
		LOGGER.info("\nRECORDING TRADES");
		//record trades for all the given stockSymbols with random data
		LocalDateTime specificDate;
		
		for (StockSymbols stockSymbol: StockSymbols.values()) {
			
			LOGGER.info("Recording trades for stock " + stockSymbol.toString());
			specificDate = LocalDateTime.of(2016, Month.OCTOBER, 31, ConfigurationConstants.START_TRADE_RECORDS_HOUR, 0, 00);
			
			for (int tradenumber = 0; tradenumber < ConfigurationConstants.NUMBER_OF_TRADE_SIMULATIONS; tradenumber++) {
				specificDate = specificDate.plusMinutes(StockUtils.getNextTradeRecord());
				StockTrade trade = new StockTrade(stockSymbol, specificDate, StockUtils.getRandomQuantity(), StockUtils.getRandomPrice(), StockUtils.getRandomTradeType());
				
				try {
					stockService.stockTradeRecord(trade);
					LOGGER.config("Trade record saved for stock " + trade);
				} catch (Exception e) {
					LOGGER.severe("Unable to record the trade for stock " + stockSymbol.toString());
				}
			}
		}
	}
	
	/**
	 * Calculate price for each stock symbol
	 */
	public void calculateStockPrices() {
		LOGGER.info("\nCalculating stock prices");
		
		//set startDate and endDate in order to retrieve trades in the given range
		LocalDateTime startDate = LocalDateTime.of(2016, Month.OCTOBER, 31, ConfigurationConstants.START_TRADE_RECORDS_HOUR+1, 0, 00);
		LocalDateTime endDate = startDate.plusMinutes(15);
		LOGGER.config("Considering trade records between " + startDate + " and " + endDate);
		
		//load stock trades
		TreeMap<StockSymbols, List<StockTrade>> stockTradeRecordsMap = stockService.selectStockTradeRecords();
		
		for (StockSymbols stockSymbol: stockTradeRecordsMap.keySet()) {
			LOGGER.config("Calculating stock price for stock " + stockSymbol);
			
			//retrieve all trade recorded in the given range
			List<StockTrade> traderecords = stockTradeRecordsMap.get(stockSymbol);
			
			try {
				BigDecimal price = stockService.calculateStockPrice(stockSymbol, traderecords, startDate, endDate);
				LOGGER.info("Stock price for stock " + stockSymbol + " is " + price);
			} catch (Exception e) {
				LOGGER.severe("Unable to calculate stock price for stock " + stockSymbol + ": " + e.getMessage());
			}
		}
	}
	
	/**
	 * Calculates GBCE All Share Index
	 */
	public void calculateGBCEAllShareIndex() {
		LOGGER.info("\nCalculating GBCE All Share Index");
		
		try {
			BigDecimal index = stockService.calculateGBCEAllShareIndex();
			LOGGER.info("GBCE All Share Index is " + index);
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
		}
	}
	
}
