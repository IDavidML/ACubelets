package me.davidml16.acubelets.commands;

import me.davidml16.acubelets.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
			list.add("balance");
			if (main.playerHasPermission(p, "acubelets.admin")) {
				list.add("give");
				list.add("box");
				list.add("create");
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
		}

		for (String s : list) {
			if (s.startsWith(args[args.length - 1])) {
				auto.add(s);
			}
		}

		return auto.isEmpty() ? list : auto;
	}

}