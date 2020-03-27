package me.doogdoog.smputils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.doogdoog.smputils.SMPUtilsPlugin;

public class WakeAll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		boolean areSleepingPlayers = false;

		for (Player p : player.getWorld().getPlayers()) {
			if (p.isSleeping()) {
				p.damage(0.0);
				areSleepingPlayers = true;
			}
		}

		if (areSleepingPlayers) {
			Bukkit.broadcastMessage(
					SMPUtilsPlugin.messagePrefix + player.getDisplayName() + "§r doesn't want to sleep!");
		}

		return true;
	}
}
