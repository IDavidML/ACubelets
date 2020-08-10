package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LanguageHandler {

	private String language = null;

	private File file;
	private FileConfiguration config;

	private HashMap<String, String> messages;
	private HashMap<String, List<String>> messageList;

	private Main main;

	public LanguageHandler(Main main, String language) {
		this.main = main;
		new File(main.getDataFolder().toString() + "/language").mkdirs();
		loadLanguage("en");
		loadLanguage("es");
		this.language = checkLanguage(language);
		this.messages = new HashMap<String, String>();
		this.messageList = new HashMap<String, List<String>>();
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public FileConfiguration getConfig() { return config; }

	public String getPrefix() {
		return Utils.translate(messages.get("Prefix"));
	}

	public String getMessage(String message) {
		return Utils.translate(messages.get(message).replaceAll("%prefix%", messages.get("Prefix")));
	}

	public List<String> getMessageList(String message) {
		List<String> lines = new ArrayList<>();
		for(String line : messageList.get(message)) {
			lines.add(Utils.translate(line));
		}
		return lines;
	}

	public String checkLanguage(String lang) {
		File f = new File("plugins/ACubelets/language/messages_" + lang + ".yml");
		if(f.exists())
			return lang;
		return "en";
	}

	public void pushMessages() {
		Main.log.sendMessage(Utils.translate(""));
		Main.log.sendMessage(Utils.translate("  &eLoading language:"));

		file = new File("plugins/ACubelets/language/messages_" + language + ".yml");
		config = YamlConfiguration.loadConfiguration(file);

		for(String key : config.getKeys(true)) {
			if (!(config.get(key) instanceof MemorySection)) {
				if(config.get(key) instanceof ArrayList)
					messageList.put(key, config.getStringList(key));
				else
					messages.put(key, config.getString(key));
			}
		}

		Main.log.sendMessage(Utils.translate("    &a'" + language + "' loaded!"));
	}

	public void loadLanguage(String lang) {
		File file = new File(main.getDataFolder() + "/language/messages_" + lang + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.options().header("\nThis is the messsages file.\nYou can change any messages that are in this file\n\nIf you want to reset a message back to the default,\ndelete the entire line the message is on and restart the server.\n\t");

		Map<String, Object> msgDefaults = new LinkedHashMap<String, Object>();

		InputStreamReader input = new InputStreamReader(main.getResource("language/messages_" + lang + ".yml"));
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

		int newSize = Math.max(cfg.getStringList("Holograms.Reward.New.Me").size(), cfg.getStringList("Holograms.Reward.New.Other").size());
		int duplicateSize = cfg.getStringList("Holograms.Reward.Duplicate").size();

		List<String> newLinesMe = new ArrayList<>();
		for(int i = 0; i < (duplicateSize - newSize); i++)
			newLinesMe.add("");
		newLinesMe.addAll(cfg.getStringList("Holograms.Reward.New.Me"));
		cfg.set("Holograms.Reward.New.Me", newLinesMe);

		List<String> newLinesOther = new ArrayList<>();
		for(int i = 0; i < (duplicateSize - newSize); i++)
			newLinesOther.add("");
		newLinesOther.addAll(cfg.getStringList("Holograms.Reward.New.Other"));
		cfg.set("Holograms.Reward.New.Other", newLinesOther);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ConfigUpdater.update(main, "language/messages_" + lang + ".yml", new File(main.getDataFolder() + "/language/messages_" + lang + ".yml"), Collections.emptyList());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}