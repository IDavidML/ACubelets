package me.davidml16.acubelets.database;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.database.types.Database;
import me.davidml16.acubelets.database.types.MySQL;
import me.davidml16.acubelets.database.types.SQLite;
import me.davidml16.acubelets.utils.ColorUtil;

public class DatabaseHandler {

	private final Database database;

	public DatabaseHandler(Main main) {
		if(main.getConfig().getBoolean("MySQL.Enabled")) {
			database = new MySQL(main);
		} else {
			database = new SQLite(main);
		}
	}

	public void openConnection() {
		Main.log.sendMessage(ColorUtil.translate("  "));
		Main.log.sendMessage(ColorUtil.translate("  &eLoading database:"));
		database.open();
	}

	public Database getDatabase() { return database; }

}
