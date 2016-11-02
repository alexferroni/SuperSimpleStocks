package it.ferroni.alessandro;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import it.ferroni.alessandro.constants.StockSymbols;
import it.ferroni.alessandro.constants.StockType;
import it.ferroni.alessandro.constants.TradeType;
import it.ferroni.alessandro.models.Stock;
import it.ferroni.alessandro.models.StockTrade;
import it.ferroni.alessandro.service.StockService;
import it.ferroni.alessandro.service.impl.StockServiceImpl;

public class ServiceTest {
	
	private StockService stockService;

	@Before
	public void setUp() throws Exception {
		stockService = new StockServiceImpl();
	}

	@Test
	public void calculateDividendYieldTest() {
		Stock stockTest = new Stock(StockSymbols.ALE, StockType.COMMON, null, null, null);
		
		//first tests with stock type equals to COMMON
		//all necessary values are null. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, null);
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		//ticker price is not null but 0.0. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, new BigDecimal(0.0));
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		//ticker price is not null and not 0.0 but last dividend is still null. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		
		stockTest.setLastDividend(new BigDecimal(0.0));
		try {
			//ticker price is not null and not 0.0 but last dividend is 0.0. I expect a valid result (0.0)
			BigDecimal result = stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			assertNotNull(result);
			assertEquals(new BigDecimal(0.0).setScale(7, RoundingMode.HALF_EVEN), result);
			
			//ticker price is not null and not 0.0 and last dividend is 5.0. I expect a valid result (1.0)
			stockTest.setLastDividend(new BigDecimal(5.0));
			result = stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			assertNotNull(result);
			assertEquals(new BigDecimal(1.0).setScale(7, RoundingMode.HALF_EVEN), result);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
		
		//second part: stock type equals to PREFERRED
		stockTest.setType(StockType.PREFERRED);
		
		//all necessary values are null. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, null);
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		//ticker price is not null but 0.0. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, new BigDecimal(0.0));
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		stockTest.setFixedDividend(new BigDecimal(0.10));
		
		//ticker price is not null ant not 0.0, fixed dividend is not null but parValue is null. An exception should be thrown
		try {
			stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		stockTest.setParValue(new Long(0));
		
		//all necessary values are not null. I expect valid results
		try {
			//parvalue is 0.0. I expect a valid result (0.0)
			BigDecimal result = stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			assertNotNull(result);
			assertEquals(new Double(0.0), new Double(result.doubleValue()));
			
			stockTest.setParValue(new Long(100));
			//parvalue is 5.0. I expect a valid result (2.0 according to the given formula)
			result = stockService.calculateDividendYield(stockTest, new BigDecimal(5.0));
			assertNotNull(result);
			assertEquals(new BigDecimal(2.0).setScale(7, RoundingMode.HALF_EVEN), result);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
	}
	
	@Test
	public void calculatePERatingTest() {
		Stock stockTest = new Stock(StockSymbols.ALE, StockType.COMMON, null, null, null);
		
		BigDecimal tickerPrice = null;
		BigDecimal dividend = null;
		//both tickerPrice and dividend are null. An exception in expected
		try {
			stockService.calculatePERating(stockTest, tickerPrice, dividend);
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		tickerPrice = new BigDecimal(0.0);
		//tickerPrice is not null but dividend is still null. An exception is expected.
		try {
			stockService.calculatePERating(stockTest, tickerPrice, dividend);
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		dividend = new BigDecimal(0.0);
		//both tickerPrice and dividend are not null, but dividend is 0.0. An exception is expected.
		try {
			stockService.calculatePERating(stockTest, tickerPrice, dividend);
			fail("An exception should be thrown");
		}
		catch (Exception e) {
		}
		
		dividend = new BigDecimal(10.0);
		//both tickerPrice and dividend are not null, and dividend is not 0.0. A valid result is expected.
		try {
			//ticker price is 0.0. I expect 0.0 as result
			BigDecimal result = stockService.calculatePERating(stockTest, tickerPrice, dividend);
			assertNotNull(result);
			assertEquals(new BigDecimal(0.0).setScale(7, RoundingMode.HALF_EVEN), result);
			
			//both values are not null and not 0.0. I expect a valid result (2.0, according to the set values)
			tickerPrice = new BigDecimal(20.0);
			result = stockService.calculatePERating(stockTest, tickerPrice, dividend);
			assertNotNull(result);
			assertEquals(new BigDecimal(2.0).setScale(7, RoundingMode.HALF_EVEN), result);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
	}
	
	@Test
	public void stockTradeRecordTest() {
		//both traderecord and trade map are null. An exception is expected
		try {
			stockService.stockTradeRecord(null);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		
		//trade record is null but trade map is not null. An exception is expected
		try {
			stockService.stockTradeRecord(null);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		
		StockTrade record = new StockTrade(null, null, null, null, null);
		//record has been created but all values are null. An exception is expected
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, null, null, null, null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), null, null, null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(-10), null, null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(0), null, null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(5), null, null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(5), new BigDecimal(-10.0), null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(5), new BigDecimal(0.0), null);
		try {
			stockService.stockTradeRecord(record);
			fail("An exception should be thrown here");
		}
		catch (Exception e) {
		}
		//now all values are set. I expect a record in the trade map
		record = new StockTrade(StockSymbols.ALE, LocalDateTime.now(), new Long(5), new BigDecimal(10.0), TradeType.BUY);
		try {
			stockService.stockTradeRecord(record);
			TreeMap<StockSymbols, List<StockTrade>> tradeMap = stockService.selectStockTradeRecords();
			List<StockTrade> records = tradeMap.get((StockSymbols.ALE));
			assertTrue(records.size() == 1);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
	}
	
	@Test
	public void calculateStockPriceTest() {
		//both stock symbols and trade list are null. An exception should be thrown
		try {
			stockService.calculateStockPrice(null, null, null, null);
			fail("An exception should be thrown here");
		} catch (Exception e) {
		}
		
		StockSymbols stockSymbol = StockSymbols.ALE;
		//stock symbol has been set but not trade list. I expect an exception
		try {
			stockService.calculateStockPrice(stockSymbol, null, null, null);
			fail("An exception should be thrown here");
		} catch (Exception e) {
		}
		
		List<StockTrade> tradeRecords = new ArrayList<>();
		//both symbol and list have been set. The list is empty so I expect 0.0 as result
		try {
			BigDecimal stockPrice = stockService.calculateStockPrice(stockSymbol, tradeRecords, null, null);
			assertEquals(new BigDecimal(0.0).setScale(7, RoundingMode.HALF_EVEN), stockPrice);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
		
		tradeRecords = new ArrayList<>();
		tradeRecords.add(new StockTrade(stockSymbol, LocalDateTime.now(), new Long(10), new BigDecimal(10.0), TradeType.BUY));
		tradeRecords.add(new StockTrade(stockSymbol, LocalDateTime.now(), new Long(40), new BigDecimal(50.0), TradeType.BUY));
		//list has been populated with valid trade records: I expect a result
		try {
			BigDecimal stockPrice = stockService.calculateStockPrice(stockSymbol, tradeRecords, null, null);
			assertEquals(new BigDecimal(42.0).setScale(7, RoundingMode.HALF_EVEN), stockPrice);
		}
		catch (Exception e) {
			fail("An exception should not be thrown here");
		}
	}
	
	@Test
	public void calculateGBCEAllShareIndexTest() {
		stockService.resetStockTradeRecords();
		
		try {
			stockService.calculateGBCEAllShareIndex();
			fail("An exception should be thrown here");
		} catch (Exception e) {
		}
		
		for (StockSymbols stock: StockSymbols.values()) {
			try {
				stockService.stockTradeRecord(new StockTrade(stock, LocalDateTime.now(), new Long(1), new BigDecimal(2), TradeType.BUY));
			} catch (Exception e) {
				fail("An exception should not be thrown here");
			}
		}
		try {
			BigDecimal index = stockService.calculateGBCEAllShareIndex();
			assertEquals(new BigDecimal(2.0).setScale(2, RoundingMode.HALF_EVEN), index);
			
		} catch (Exception e) {
			fail("An exception should not be thrown here");
		}
	}



}
