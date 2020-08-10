package me.davidml16.acubelets.database;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.database.types.Database;
import me.davidml16.acubelets.database.types.MySQL;
import me.davidml16.acubelets.database.types.SQLite;
import me.davidml16.acubelets.utils.Utils;

public class DatabaseHandler {

	private Database database;

	private Main main;

	public DatabaseHandler(Main main) {
		this.main = main;
		if(main.getConfig().getBoolean("MySQL.Enabled")) {
			database = new MySQL(main);
		} else {
			database = new SQLite(main);
		}
	}

	public void openConnection() {
		Main.log.sendMessage(Utils.translate("  "));
		Main.log.sendMessage(Utils.translate("  &eLoading database:"));
		database.open();
	}

	public void changeToSQLite() {
		database = new SQLite(main);
		database.open();
	}

	public Database getDatabase() { return database; }

}
