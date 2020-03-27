package me.doogdoog.smputils.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.doogdoog.smputils.SMPUtilsPlugin;

public class TellCoords implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		if (args.length == 0) {
			player.sendMessage("§cUsage: /tellcoords <recipients>");
			return false;
		}

		Location coords = player.getLocation();
		int x = coords.getBlockX();
		int y = coords.getBlockY();
		int z = coords.getBlockZ();

		List<String> validRecipientNames = new ArrayList<>();
		List<String> invalidRecipientNames = new ArrayList<>();
		Set<String> seenNames = new HashSet<>();

		for (String arg : args) {
			if (seenNames.contains(arg))
				continue;
			seenNames.add(arg);

			Player recipient = Bukkit.getPlayer(arg);

			if (recipient == null) {
				invalidRecipientNames.add("§r" + arg);
			} else {
				recipient.sendMessage(SMPUtilsPlugin.messagePrefix + "§e" + player.getName() + "§r is at " + "§b§l"
						+ x + " §r§l" + y + " §b§l" + z + "§r!");
				validRecipientNames.add("§r" + recipient.getDisplayName());
			}
		}

		if (validRecipientNames.size() > 0) {
			player.sendMessage(SMPUtilsPlugin.messagePrefix + "§7§oSuccessfully sent your coordinates to §r"
					+ getCommaSeparatedList(validRecipientNames, "§7§o"));
		}

		if (invalidRecipientNames.size() > 0) {
			player.sendMessage(SMPUtilsPlugin.messagePrefix + "§c§oCould not send your coordinates to §r"
					+ getCommaSeparatedList(invalidRecipientNames, "§c§o")
					+ "§r§c§o. Either they are not online or their "
					+ (invalidRecipientNames.size() == 1 ? "name is" : "names are") + " spelled wrong");
		}

		return true;
	}

	private String getCommaSeparatedList(List<String> list, String format) {

		if (list.size() == 1)
			return "§r" + list.get(0);

		if (list.size() == 2)
			return "§r" + list.get(0) + "§r" + format + " and " + list.get(1);

		String ret = "§r";
		for (int i = 0; i < list.size() - 1; i++) {
			ret += list.get(i) + "§r" + format + ", ";
		}
		ret += "and " + list.get(list.size() - 1);

		return ret;
	}

}
