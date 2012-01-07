package me.meiamsome.myriadvirtual;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MyriadVirtual extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");

	private MyriadChestManager chestManager;

	public void onEnable() {
		// Load/create configuration
		final FileConfiguration config = getConfig();
		/*if (!new File(getDataFolder(), "config.yml").exists()) {
			ArrayList<String> admincmds = new ArrayList<String>();
			admincmds.add("ac.admin");
			admincmds.add("ac.save");
			admincmds.add("ac.reload");

			config.setProperty("admincmds", admincmds);
			config.setProperty("admins", getOps());

			config.setProperty("autosave", 10);

			config.save();
		}*/


		// Initialize
		chestManager = new MyriadChestManager(new File(getDataFolder(), "chests"));
		chestManager.load();

		// Set command executors
		final ChestCommands chestCommands = new ChestCommands(this, chestManager);
		getCommand("chest").setExecutor(chestCommands);
		getCommand("clearchest").setExecutor(chestCommands);
		getCommand("changechest").setExecutor(chestCommands);
		getCommand("savechests").setExecutor(chestCommands);
		getCommand("workbench").setExecutor(new WorkbenchCommand(this));

		// Schedule auto-saving
		int autosaveInterval = config.getInt("autosave", 10) * 3000;
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				chestManager.save();
				log.fine("[AlphaChest] auto-saved chests");
			}
		}, autosaveInterval, autosaveInterval);

		// Success
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[" + pdfFile.getName() + "] version [" + pdfFile.getVersion() + "] enabled");
	}

	public void onDisable() {
		chestManager.save();

		PluginDescriptionFile pdfFile = getDescription();
		log.info("[" + pdfFile.getName() + "] version [" + pdfFile.getVersion() + "] disabled");
	}



}
