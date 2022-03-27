package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LiveGuiTask {

	private int id;

	private Main main;
	public LiveGuiTask(Main main) {
		this.main = main;
	}

	class Task implements Runnable {
		@Override
		public void run() {

			if(main.getPlayerCount() == 0) return;

			for(Menu menu : main.getMenuHandler().getOpenedMenus().values()) {

				if(!menu.getClass().equals(CubeletsMenu.class)) continue;

				List<Cubelet> cubelets = (List<Cubelet>) menu.getAttribute(Menu.AttrType.CUBELET_DISPLAYED_LIST_ATTR);
				List<ItemStack> items = (List<ItemStack>) menu.getAttribute(Menu.AttrType.CUBELET_DISPLAYED_ITEMS_ATTR);

				if(cubelets == null) continue;
				if(items == null) continue;

				if(cubelets.size() == 0) continue;

				List<ItemStack> updatedItems = new ArrayList<>();

				for(int i = 0; i < cubelets.size(); i++) {

					Cubelet cubelet = cubelets.get(i);

					if(i >= items.size()) continue;

					ItemStack item = items.get(i);

					List<String> lore = new ArrayList<>();
					if (cubelet.getExpire() > System.currentTimeMillis()) {
						for (String line : cubelet.getCubeletType().getLoreAvailable()) {
							lore.add(Utils.translate(line
									.replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived())))
									.replaceAll("%expires%", TimeUtils.millisToLongDHMS(cubelet.getExpire() - System.currentTimeMillis())));
						}
					} else {
						for (String line : cubelet.getCubeletType().getLoreExpired()) {
							lore.add(Utils.translate(line
									.replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived()))));
						}
					}

					updatedItems.add(new ItemBuilder(item).setLore(lore).toItemStack());

				}

				Bukkit.getScheduler().runTask(main, () -> {

					Player target = menu.getOwner();

					if(target == null) return;

					if(!main.getMenuHandler().hasOpenedMenu(target, CubeletsMenu.class)) return;

					for(int i = 0; i < updatedItems.size(); i++) target.getOpenInventory().getTopInventory().setItem(i, updatedItems.get(i));

				});

			}

		}
	}
	
	public int getId() { return id; }

	@SuppressWarnings("deprecation")
	public void start() {
		id = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(main, new Task(), 0, 20);
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
