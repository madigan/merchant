package tech.otter.merchant.factories;

import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import tech.otter.merchant.data.Merchant;

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
		return Merchant.mock();
	}
}
