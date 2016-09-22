package tech.otter.merchant.factories;

import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import java.util.Arrays;

import tech.otter.merchant.data.Merchant;

/**
 * Created by john on 9/21/16.
 */

public class MerchantFactory {
	private Logger logger = LoggerService.forClass(getClass());
	private static MerchantFactory INSTANCE;

	private MerchantFactory() {

	}

	public static MerchantFactory get() {
		if(INSTANCE == null) INSTANCE = new MerchantFactory();
		return INSTANCE;
	}

	// TODO: Load merchants from a file
	// TODO: Give merchants random names?
	public Merchant make() {
		return new Merchant("Freddy", "images/raw/merchant_1.png", ItemFactory.get().make(Arrays.asList("category-food")));
	}
}
