package tech.otter.merchant.model.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

import tech.otter.merchant.model.Galaxy;
import tech.otter.merchant.model.Station;

public class GalaxyFactory {
	private static GalaxyFactory INSTANCE = new GalaxyFactory();
	private static String GALACTIC_FILE = "galaxy.json";
	private Logger logger;

	public static GalaxyFactory get() {
		return INSTANCE;
	}

	private GalaxyFactory() {
		this.logger = LoggerService.forClass(getClass());
	}

	public Galaxy make() {
		Json json = new Json();
		logger.debug("Loading galaxy from '{0}'", GALACTIC_FILE);
		Galaxy galaxy = json.fromJson(Galaxy.class, Gdx.files.internal(GALACTIC_FILE));
		logger.debug("Populating a galaxy-full of merchants...");
		for(Station station : galaxy.getStations().values()) {
			logger.debug("Restocking merchant {0} ... ", station.getMerchant().getName());
			station.getMerchant().restock();
			logger.debug("Merchant {0} has {1}", station.getMerchant().getName(), station.getMerchant().getInventory());
		}
		logger.debug("... merchants populated.");

		return galaxy;
	}
}
