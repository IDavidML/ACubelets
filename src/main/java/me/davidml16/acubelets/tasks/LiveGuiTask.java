package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.DisplayedCubelet;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static me.davidml16.acubelets.utils.MessageUtils.DefaultFontInfo.i;

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

			GUILayout guiLayout = main.getLayoutHandler().getLayout("opencubelet");

			for(Menu menu : main.getMenuHandler().getOpenedMenus().values()) {
				if(!menu.getClass().equals(CubeletsMenu.class)) continue;

				List<DisplayedCubelet> displayedCubelets = (List<DisplayedCubelet>) menu.getAttribute(Menu.AttrType.CUBELET_DISPLAYED_CUBELETS_ATTR);

				if(displayedCubelets == null) continue;

				if(displayedCubelets.size() == 0) continue;

				Iterator<DisplayedCubelet> iterator = displayedCubelets.iterator();

				while (iterator.hasNext()) {
					DisplayedCubelet displayedCubelet = iterator.next();

					Cubelet cubelet = displayedCubelet.getCubelet();
					ItemStack item = displayedCubelet.getItem();

					List<String> lore = new ArrayList<>();
					if (cubelet.getExpire() < 0 || cubelet.getExpire() > System.currentTimeMillis()) {
						for (String line : cubelet.getCubeletType().getLoreAvailable()) {
							lore.add(Utils.translate(line
									.replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived())))
									.replaceAll("%expires%", (cubelet.getExpire() >= 0 ? TimeUtils.millisToLongDHMS(cubelet.getExpire() - System.currentTimeMillis()) : guiLayout.getMessage("NoneExpiration"))));
						}
					} else {
						for (String line : cubelet.getCubeletType().getLoreExpired()) {
							lore.add(Utils.translate(line
									.replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived()))));
						}
					}

					displayedCubelet.setItem(new ItemBuilder(item).setLore(lore).toItemStack());
				}

				Bukkit.getScheduler().runTask(main, () -> {
					Player target = menu.getOwner();

					if(target == null) return;

					if(!main.getMenuHandler().hasOpenedMenu(target, CubeletsMenu.class)) return;

					for (DisplayedCubelet displayedCubelet : displayedCubelets) {
						target.getOpenInventory().getTopInventory().setItem(displayedCubelet.getSlot(), displayedCubelet.getItem());
					}
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
