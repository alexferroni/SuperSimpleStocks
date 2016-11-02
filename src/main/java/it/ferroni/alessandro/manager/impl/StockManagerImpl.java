package it.ferroni.alessandro.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.constants.StockType;
import it.ferroni.alessandro.manager.StockManager;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;
import it.ferroni.alessandro.utils.StockUtils;

/**
 * @author Alessandro Ferroni
 *
 */
public class StockManagerImpl implements StockManager {
	
	private TreeMap<StockSymbols, BigDecimal> stockTickerPrices;
	private List<Stock> stockTable;
	
	private TreeMap<StockSymbols, BigDecimal> dividendYieldMap;
	private TreeMap<StockSymbols, BigDecimal> stockPERatingMap;
	private TreeMap<StockSymbols, List<StockTrade>> stockTradeRecordsMap;
	
	public StockManagerImpl() {
		//inizialize stock ticker prices
		stockTickerPrices = new TreeMap<>();
		stockTickerPrices.put(StockSymbols.TEA, StockUtils.getRandomPrice());
		stockTickerPrices.put(StockSymbols.POP, StockUtils.getRandomPrice());
		stockTickerPrices.put(StockSymbols.ALE, StockUtils.getRandomPrice());
		stockTickerPrices.put(StockSymbols.GIN, StockUtils.getRandomPrice());
		stockTickerPrices.put(StockSymbols.JOE, StockUtils.getRandomPrice());
		
		//initialize stock table
		stockTable = new ArrayList<>();
		stockTable.add(new Stock(StockSymbols.TEA, StockType.COMMON, new BigDecimal(0.0).setScale(7,  RoundingMode.HALF_EVEN), null, new Long(100)));
		stockTable.add(new Stock(StockSymbols.POP, StockType.COMMON, new BigDecimal(8.0).setScale(7,  RoundingMode.HALF_EVEN), null, new Long(100)));
		stockTable.add(new Stock(StockSymbols.ALE, StockType.COMMON, new BigDecimal(23.0).setScale(7,  RoundingMode.HALF_EVEN), null, new Long(60)));
		stockTable.add(new Stock(StockSymbols.GIN, StockType.PREFERRED, new BigDecimal(8.0).setScale(7,  RoundingMode.HALF_EVEN), new BigDecimal(0.02).setScale(2,  RoundingMode.HALF_EVEN), new Long(100)));
		stockTable.add(new Stock(StockSymbols.JOE, StockType.COMMON, new BigDecimal(13.0).setScale(7,  RoundingMode.HALF_EVEN), null, new Long(250)));
		
		dividendYieldMap = new TreeMap<>();
		stockPERatingMap = new TreeMap<>();
		stockTradeRecordsMap = new TreeMap<>();
	}
	
	@Override
	public List<Stock> selectStockTable() {
		return this.stockTable;
	}
	
	@Override
	public TreeMap<StockSymbols, BigDecimal> selectTickerPrices() {
		return this.stockTickerPrices;
	}
	
	@Override
	public void saveDividendYield(StockSymbols stockSymbol, BigDecimal dividendYeld) {
		this.dividendYieldMap.put(stockSymbol, dividendYeld);
	}
	
	@Override
	public TreeMap<StockSymbols, BigDecimal> selectDividendYield() {
		return this.dividendYieldMap;
	}
	

	@Override
	public void saveStockPERating(StockSymbols stockSymbol, BigDecimal pe_rating) {
		this.stockPERatingMap.put(stockSymbol, pe_rating);
	}

	@Override
	public TreeMap<StockSymbols, BigDecimal> selectStockPERating() {
		return this.stockPERatingMap;
	}
	
	@Override
	public void saveStockTrade(StockTrade record) {
		if (stockTradeRecordsMap == null) {
			stockTradeRecordsMap = new TreeMap<>();
		}
		List<StockTrade> stockTradeRecords = stockTradeRecordsMap.get(record.getStockSymbol());
		if (stockTradeRecords == null) {
			stockTradeRecords = new ArrayList<>();
		}
		stockTradeRecords.add(record);
		stockTradeRecordsMap.put(record.getStockSymbol(), stockTradeRecords);
	}
	
	@Override
	public TreeMap<StockSymbols, List<StockTrade>> selectStockTradeRecords() {
		return this.stockTradeRecordsMap;
	}
	
	@Override
	public void resetStockTradeRecords() {
		this.stockTradeRecordsMap.clear();
	}

}
