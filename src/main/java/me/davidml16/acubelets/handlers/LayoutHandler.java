package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LayoutHandler {

	private HashMap<String, GUILayout> layouts;

	private Main main;

	public LayoutHandler(Main main) {
		this.main = main;
		this.layouts = new HashMap<String, GUILayout>();

		new File(main.getDataFolder().toString() + "/gui_layouts").mkdirs();

		loadLayouts();
	}

	public GUILayout getLayout(String layout) {
		return layouts.get(layout);
	}

	public void createLayoutFile(String layout) {
		File file = new File(main.getDataFolder() + "/gui_layouts/" + layout + "_layout.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.options().header("\nThis is the gui layout file.\nYou can change any messages that are in this file\n\nIf you want to reset a message back to the default,\ndelete the entire line the message is on and restart the server.\n\nIMPORTANT: Slots numbers between 1-9, if not, the plugin will crash\n\t");

		Map<String, Object> msgDefaults = new LinkedHashMap<String, Object>();

		InputStreamReader input = new InputStreamReader(main.getResource("gui_layouts/" + layout + "_layout.yml"));
		FileConfiguration data = YamlConfiguration.loadConfiguration(input);

		for(String key : data.getKeys(true)) {
			if(!(data.get(key) instanceof MemorySection)) {
				msgDefaults.put(key, data.get(key));
			}
		}

		for (String key : msgDefaults.keySet()) {
			if (!cfg.isSet(key)) {
				cfg.set(key, msgDefaults.get(key));
			}
		}

		for(String key : cfg.getKeys(true)) {
			if(!(cfg.get(key) instanceof MemorySection)) {
				if (!data.isSet(key)) {
					cfg.set(key, null);
				}
			}
		}

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadLayouts() {
		this.layouts.clear();

		createLayoutFile("crafting");
		loadLayout("crafting");

		createLayoutFile("craftingconfirmation");
		loadLayout("craftingconfirmation");

		createLayoutFile("opencubelet");
		loadLayout("opencubelet");

		createLayoutFile("preview");
		loadLayout("preview");
	}

	public void loadLayout(String layout) {
		File file = new File(main.getDataFolder() + "/gui_layouts/" + layout + "_layout.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		GUILayout guiLayout = new GUILayout();

		for(String key : config.getKeys(true)) {
			if (!(config.get(key) instanceof MemorySection)) {
				if(config.get(key) instanceof ArrayList)
					guiLayout.getMessageList().put(key, config.getStringList(key));
				else
					guiLayout.getMessages().put(key, config.getString(key));
			}
		}

		layouts.put(layout, guiLayout);
	}

}