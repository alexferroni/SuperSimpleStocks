/**
 * 
 */
package it.ferroni.alessandro.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.constants.TradeType;
import it.ferroni.alessandro.manager.StockManager;
import it.ferroni.alessandro.manager.impl.StockManagerImpl;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;
import it.ferroni.alessandro.service.StockService;

/**
 * @author Alessandro Ferroni
 *
 */
public class StockServiceImpl implements StockService {
	
	private StockManager stockManager;
	
	public StockServiceImpl() {
		stockManager = new StockManagerImpl();
	}
	
	@Override
	public List<Stock> selectStockTable() {
		return stockManager.selectStockTable();
	}
	
	@Override
	public TreeMap<StockSymbols, BigDecimal> selectTickerPrices() {
		return stockManager.selectTickerPrices();
	}
	
	@Override
	public BigDecimal calculateDividendYield(Stock stock, BigDecimal tickerPrice) throws Exception {
		BigDecimal dividendYeld;
		if (tickerPrice == null || tickerPrice.doubleValue() == 0.0) {
			//cannot calculate ticker price
			//log an error and goes to the next stock
			throw new Exception("Cannot calculate dividend yield for stock " + stock.getStockSymbol() + ": Ticker price is " + (tickerPrice == null ? "null" : "0"));
		}
		switch (stock.getType()) {
			case COMMON: 
				if (stock.getLastDividend() == null) {
					//cannot calculate dividend yield. Log an error and goes to the next stock
					throw new Exception("Cannot calculate dividend yield for stock " + stock.getStockSymbol() + ": Last Dividend is null");
				}
				dividendYeld = stock.getLastDividend().divide(tickerPrice, 7, RoundingMode.HALF_EVEN);
				break;
			case PREFERRED: 
				if (stock.getFixedDividend()  == null) {
					//cannot calculate dividend yield. Log an error and goes to the next stock
					throw new Exception("Cannot calculate dividend yield for stock " + stock.getStockSymbol() + ": Fixed Dividend is null");
				}
				if (stock.getParValue() == null) {
					//cannot calculate dividend yield. Log an error and goes to the next stock
					throw new Exception("Cannot calculate dividend yield for stock " + stock.getStockSymbol() + ": Par Value is null");
				}
				dividendYeld = stock.getFixedDividend().multiply(new BigDecimal(stock.getParValue().longValue())).divide(tickerPrice, 7, RoundingMode.HALF_EVEN);
				break;
			default: 
				//log an error: stock type is an invalid value. Default should never occurs
				throw new Exception("Cannot calculate dividend yield for stock " + stock.getStockSymbol() + ": Stock type " + stock.getType() + " is an invalid value");
		}
		return dividendYeld.setScale(7, RoundingMode.HALF_EVEN);
	}
	
	@Override
	public void saveDividendYield(StockSymbols stockSymbol, BigDecimal dividendYield) {
		stockManager.saveDividendYield(stockSymbol, dividendYield);
	}
	
	@Override
	public TreeMap<StockSymbols, BigDecimal> selectDividendYield() {
		return stockManager.selectDividendYield();
	}
	
	@Override
	public BigDecimal calculatePERating(Stock stock, BigDecimal tickerPrice, BigDecimal dividendYield) throws Exception {
		if (tickerPrice == null) {
			//cannot calculate pe rating: log an error and continue to the next stock
			throw new Exception("Cannot calculate P/E rating for stock " + stock.getStockSymbol() + ": ticker price is null");
		}
		
		if (dividendYield == null || dividendYield.doubleValue() == 0.0) {
			//cannot calculate pe rating: log an error and continue to the next stock
			throw new Exception("Cannot calculate P/E rating for stock " + stock.getStockSymbol() + ": Dividend Yield price is " + (dividendYield == null ? "null" : "0"));
		}
		
		BigDecimal pe_rating = tickerPrice.divide(dividendYield, 7, RoundingMode.HALF_EVEN);
		return pe_rating;
	}
	
	@Override
	public void saveStockPERating(StockSymbols stockSymbol, BigDecimal pe_rating) {
		stockManager.saveStockPERating(stockSymbol, pe_rating);
	}
	
	@Override
	public void stockTradeRecord(StockTrade record) throws Exception {
		//check the validity of the given data. If a values is not valid, an exception will be thrown
		if (record == null) {
			throw new Exception("Trade record must not be null");
		}
		
		if (record.getStockSymbol() == null) {
			throw new Exception("Stock symbol must be specified");
		}
		if (record.getPrice() == null || record.getPrice().doubleValue() <= 0.0) {
			throw new Exception ("Trade price must be a valid value (not null and greater than zero)");
		}
		if (record.getQuantity() == null || record.getQuantity() <= 0) {
			throw new Exception("Trade quantity must be a valid value (not null and greater than zero)");
		}
		if (record.getTimestamp() == null) {
			throw new Exception("Trade timestamp must be specified");
		}
		if (record.getType() == null || (record.getType() != null && !record.getType().equals(TradeType.BUY) && !record.getType().equals(TradeType.SELL))) {
			throw new Exception("Trade type must be a valid value (" + TradeType.BUY + " or " + TradeType.SELL + ")");
		}
		
		stockManager.saveStockTrade(record);
	}
	
	@Override
	public TreeMap<StockSymbols, List<StockTrade>> selectStockTradeRecords() {
		return stockManager.selectStockTradeRecords();
	}
	
	@Override
	public void resetStockTradeRecords() {
		stockManager.resetStockTradeRecords();
	}
	
	@Override
	public BigDecimal calculateStockPrice(StockSymbols stockSymbol, List<StockTrade> stockTrades, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
		if (stockSymbol == null) {
			throw new Exception("Stock Symbol must be specified");
		}
		if (stockTrades == null) {
			throw new Exception("Stock Trades list must be specified");
		}
		
		List<StockTrade> filteredStockTrades = new ArrayList<>();
		
		if (startDate != null || endDate != null) {
			for (StockTrade traderecord: stockTrades) {
				if (startDate != null && endDate == null) {
					//extracts only trades after the given startDate
					if (traderecord.getTimestamp().isAfter(startDate)) {
						filteredStockTrades.add(traderecord);
					}
				}
				else if (startDate == null && endDate != null) {
					//extracts only trades before the given endDate
					if (traderecord.getTimestamp().isBefore(endDate)) {
						filteredStockTrades.add(traderecord);
					}
				}
				else {
					//both dates are not null: extracts trades between the given values
					if (traderecord.getTimestamp().isAfter(startDate) && traderecord.getTimestamp().isBefore(endDate)) {
						filteredStockTrades.add(traderecord);
					}
				}
			}
		}
		else {
			//no date filter has been requested
			filteredStockTrades = stockTrades;
		}
		
		if (filteredStockTrades.size() == 0) {
			//no records in the list: price is 0.0
			return new BigDecimal(0.0).setScale(7, RoundingMode.HALF_EVEN);
		}
		
		BigDecimal quantitySum = new BigDecimal(0.0);
		BigDecimal quantityPriceSum = new BigDecimal(0.0); 
		
		for (StockTrade trade: filteredStockTrades) {
			if (!trade.getStockSymbol().equals(stockSymbol)) {
				//stock symbol of the trade is not equal to the given stock symbol
				throw new Exception("Stock Trade Symbol (" + trade.getStockSymbol() + " is not equal to the given Stock Symbol (" + stockSymbol + ")");
			}
			
			//here I should check that quantity and price are not equal to null, but this check has been already performed in stockTradeRecord method.
			//I omit this checks for simplicity
			
			quantitySum = quantitySum.add(new BigDecimal(trade.getQuantity().longValue()));
			quantityPriceSum = quantityPriceSum.add(trade.getPrice().multiply(new BigDecimal(trade.getQuantity().longValue())));
		}
		
		return quantityPriceSum.divide(quantitySum, 7, RoundingMode.HALF_EVEN);
	}
	
	@Override
	public BigDecimal calculateGBCEAllShareIndex() throws Exception {
		TreeMap<StockSymbols, List<StockTrade>> tradeMap = stockManager.selectStockTradeRecords();
		
		if (tradeMap == null) {
			throw new Exception("Trade map must be created");
		}
		
		BigDecimal prices = new BigDecimal(1.0);
		int numberOfPrices = 0;
		
		for (List<StockTrade> tradeRecordsForStock: tradeMap.values()) {
			for (StockTrade tradeRecord: tradeRecordsForStock) {
				prices = prices.multiply(tradeRecord.getPrice());
				numberOfPrices++;
			}
		}
		
		if (numberOfPrices == 0) {
			throw new Exception("Unable to calculate index: no prices have been set");
		}
		
		return new BigDecimal(Math.pow(prices.doubleValue(), 1 / (double)numberOfPrices)).setScale(2, RoundingMode.HALF_EVEN);
	}

}
