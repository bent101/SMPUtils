package me.doogdoog.smputils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.doogdoog.smputils.commands.SayCoords;
import me.doogdoog.smputils.commands.TellCoords;
import me.doogdoog.smputils.commands.WakeAll;

public class SMPUtilsPlugin extends JavaPlugin {

	public static final String messagePrefix = "§7§o[SMPUtils]§r ";

	@Override
	public void onEnable() {

		getCommand("saycoords").setExecutor(new SayCoords());
		getCommand("tellcoords").setExecutor(new TellCoords());
		getCommand("wakeall").setExecutor(new WakeAll());

		Bukkit.getPluginManager().registerEvents(new SMPUtilsListener(this), this);
	}
}
