package me.doogdoog.smputils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.doogdoog.smputils.SMPUtilsPlugin;

public class SayCoords implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		Location coords = player.getLocation();
		int x = coords.getBlockX();
		int y = coords.getBlockY();
		int z = coords.getBlockZ();

		Bukkit.broadcastMessage(SMPUtilsPlugin.messagePrefix + "§e" + player.getName() + "§r is at " + "§b§l" + x
				+ " §r§l" + y + " §b§l" + z + "§r!");

		return true;
	}

}
