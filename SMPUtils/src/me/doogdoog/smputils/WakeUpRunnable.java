package me.doogdoog.smputils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WakeUpRunnable extends BukkitRunnable {

	World world;

	public WakeUpRunnable(World world) {
		this.world = world;
	}

	@Override
	public void run() {

		// make it 6am the next day and clear the weather
		world.setTime(0);
		world.setFullTime(world.getFullTime() + 24000);
		if (world.hasStorm())
			world.setWeatherDuration(1);

		// wake everyone up
		for (Player p : world.getPlayers()) {
			if (p.isSleeping())
				p.damage(0.0);
		}
	}

}
