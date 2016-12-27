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
		Gdx.app.debug(getClass().getSimpleName(), "Loading galaxy from '" + GALACTIC_FILE + "'");
		Galaxy galaxy = json.fromJson(Galaxy.class, Gdx.files.internal(GALACTIC_FILE));
        Gdx.app.debug(getClass().getSimpleName(), "Populating a galaxy-full of merchants...");
		for(Station station : galaxy.getStations().values()) {
            Gdx.app.debug(getClass().getSimpleName(), "Restocking merchant " + station.getMerchant().getName() + " ... ");
			station.getMerchant().restock();
            Gdx.app.debug(getClass().getSimpleName(), "Merchant " + station.getMerchant().getName() + " has " + station.getMerchant().getInventory());
		}
        Gdx.app.debug(getClass().getSimpleName(), "... merchants populated.");

		return galaxy;
	}
}
