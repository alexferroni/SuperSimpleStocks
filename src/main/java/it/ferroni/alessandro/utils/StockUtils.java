/**
 * 
 */
package it.ferroni.alessandro.utils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import it.ferroni.alessandro.constants.ConfigurationConstants;
import it.ferroni.alessandro.constants.TradeType;

/**
 * @author Alessandro Ferroni
 *
 */
public class StockUtils {
	
	/**
	 * Gets a random next trade record minute
	 * @return
	 */
	public static int getNextTradeRecord() {
		return ThreadLocalRandom.current().nextInt(1, 5);
	}
	
	/**
	 * Gets a random trade quantity
	 * @param r
	 * @return
	 */
	public static Long getRandomQuantity() {
		return ThreadLocalRandom.current().nextLong(ConfigurationConstants.QUANTITY_RANGE_MIN, ConfigurationConstants.QUANTITY_RANGE_MAX);
	}
	
	/**
	 * Gets a random price
	 * @return
	 */
	public static BigDecimal getRandomPrice() {
		return new BigDecimal(ThreadLocalRandom.current().nextDouble(ConfigurationConstants.PRICE_RANGE_MIN, ConfigurationConstants.PRICE_RANGE_MAX));
	}
	
	/**
	 * Gets a random trade type
	 * @return
	 */
	public static TradeType getRandomTradeType() {
		return ThreadLocalRandom.current().nextBoolean() ? TradeType.BUY : TradeType.SELL;
	}

}
