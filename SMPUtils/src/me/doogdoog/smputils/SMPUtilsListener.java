package me.doogdoog.smputils;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SMPUtilsListener implements Listener {

	private JavaPlugin plugin;
	private WakeUpRunnable wakeUpRunnable;

	public SMPUtilsListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		Damageable meta = (Damageable) item.getItemMeta();

		int durability = item.getType().getMaxDurability() - meta.getDamage();

		String itemName = "" + item.getType();

		// convert "ITEM_NAME" to "Item Name"
		itemName = itemName.toLowerCase().replaceAll("_", " ");
		itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
		for (int i = 0; i < itemName.length(); i++) {
			if (itemName.charAt(i) == ' ') {
				itemName = itemName.substring(0, i + 1) + itemName.substring(i + 1, i + 2).toUpperCase()
						+ itemName.substring(i + 2);
			}
		}

		if (durability == 20) {
			player.sendMessage(SMPUtilsPlugin.messagePrefix + "Your §b" + itemName + "§r"
					+ (itemName.endsWith("s") || itemName.endsWith("a") ? " are" : " is") + " about to break!");
		}
	}

	@EventHandler
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		if (event.isCancelled() || event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
			return;
		}

		Player player = event.getPlayer();
		World world = player.getWorld();
		wakeUpRunnable = new WakeUpRunnable(world);

		TextComponent wakeUpButton = new TextComponent(SMPUtilsPlugin.messagePrefix + "§e" + player.getName()
				+ "§r went to sleep. §c[Click here to wake them up]");

		wakeUpButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Left-click or type /wakeall to §bwake everyone up§r.").create()));

		wakeUpButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wakeall"));

		Bukkit.spigot().broadcast(new TextComponent("§m§6               §r\n"), wakeUpButton);

		// wake up 5 seconds later
		wakeUpRunnable.runTaskLater(plugin, 100);
	}

	@EventHandler
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
		// if no one else was sleeping...
		for (Player p : event.getPlayer().getWorld().getPlayers()) {
			if (p.isSleeping())
				return;
		}

		// cancel the wake up runnable
		wakeUpRunnable.cancel();
	}

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		for (Player p : event.getRecipients()) {
			p.sendMessage(event.getPlayer().getDisplayName() + "§7: §r" + event.getMessage());
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setDisplayName("§e" + player.getName() + "§r");
		event.setJoinMessage("§e" + player.getDisplayName() + " joined the game");
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(event.getPlayer().getDisplayName() + " left the game");
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String deathMsg = event.getDeathMessage();
		String playerName = event.getEntity().getDisplayName();

		event.setDeathMessage(playerName + deathMsg.substring(deathMsg.indexOf(" ")));
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Wolf && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
			return;
		}

		Wolf wolf = (Wolf) event.getEntity();

		if (wolf.isTamed() && event.getDamage() <= 1) {
			wolf.playEffect(EntityEffect.WOLF_HEARTS);
			event.setCancelled(true);
		}
	}
}
