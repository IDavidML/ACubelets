package me.davidml16.acubelets.commands;

import me.davidml16.acubelets.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabCompleter_ACubelets implements TabCompleter {

	private Main main;
	public TabCompleter_ACubelets(Main main) {
		this.main = main;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}

		Player p = (Player) sender;

		List<String> list = new ArrayList<String>();
		List<String> auto = new ArrayList<String>();

		if (args.length == 1) {
			if (main.playerHasPermission(p, "acubelets.admin")) {
				list.add("help");
				list.add("give");
				list.add("machine");
				list.add("type");
				list.add("setup");
				list.add("reload");
			}
		}

		if (args[0].equalsIgnoreCase("give")) {
			if (args.length == 2) {
				if (main.playerHasPermission(p, "acubelets.admin")) {
					for (Player target : main.getServer().getOnlinePlayers()) {
						list.add(target.getName());
					}
				}
			} else {
				if (main.playerHasPermission(p, "acubelets.admin")) {
					list.addAll(main.getCubeletTypesHandler().getTypes().keySet());
				}
			}
		} else if (args[0].equalsIgnoreCase("setup")) {
			if(args.length == 2) {
				if (main.playerHasPermission(p, "acubelets.admin")) {
					for (File file : Objects.requireNonNull(new File(main.getDataFolder(), "types").listFiles())) {
						list.add(file.getName().replace(".yml", ""));
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("machine")) {
			if(args.length == 2) {
				if (main.playerHasPermission(p, "acubelets.admin")) {
					list.add("create");
					list.add("remove");
				}
			}
		} else if (args[0].equalsIgnoreCase("type")) {
			if (main.playerHasPermission(p, "acubelets.admin")) {
				if(args.length == 2) {
					list.add("create");
					list.add("remove");
				} else if(args.length == 3) {
					if(args[1].equalsIgnoreCase("remove")) {
						for (String type : main.getCubeletTypesHandler().getTypes().keySet()) {
							list.add(type.toLowerCase());
						}
					}
				}
			}
		}

		for (String s : list) {
			if (s.startsWith(args[args.length - 1])) {
				auto.add(s);
			}
		}

		return auto.isEmpty() ? list : auto;
	}

}