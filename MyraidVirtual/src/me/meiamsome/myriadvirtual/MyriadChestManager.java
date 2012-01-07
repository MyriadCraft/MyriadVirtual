package me.meiamsome.myriadvirtual;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

import org.bukkit.Bukkit;

public class MyriadChestManager {
	private static Logger log = Logger.getLogger("Minecraft");

	private final HashMap<String, IInventory> chests;
	private final File dataFolder;

	public MyriadChestManager(File dataFolder) {
		this.dataFolder = dataFolder;
		this.chests = new HashMap<String, IInventory>();
	}

	public IInventory getChest(String playerName) {
		IInventory chest = chests.get(playerName.toLowerCase());

		if (chest == null)
			chest = addChest(playerName);

		return chest;
	}

	private IInventory addChest(String playerName) {
		IInventory chest = new MyriadChest(playerName+"'s Chest", Bukkit.getPlayer(playerName));
		chests.put(playerName.toLowerCase(), chest);
		return chest;
	}

	public void removeChest(String playerName) {
		chests.remove(playerName.toLowerCase());
	}

	public void load() {
		chests.clear();

		int loadedChests = 0;

		dataFolder.mkdirs();
		final FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".chest");
			}
		};
		for (File chestFile : dataFolder.listFiles(filter)) {
			try {
				final String playerName = chestFile.getName().substring(0, chestFile.getName().length() - 6);
				final MyriadChest chest = new MyriadChest(playerName+"'s Chest", Bukkit.getPlayer(playerName));

				final BufferedReader in = new BufferedReader(new FileReader(chestFile));

				String line;
				int field = 0;
				while ((line = in.readLine()) != null) {
					if (line != "") {
						final String[] parts = line.split(":");
						try {
							int type = Integer.parseInt(parts[0]);
							int amount = Integer.parseInt(parts[1]);
							short damage = Short.parseShort(parts[2]);
							if (type != 0) {
								chest.setItem(field, new ItemStack(type, amount, damage));
							}
						} catch (NumberFormatException e) {
							// ignore
						}
						++field;
					}
				}

				in.close();
				chests.put(playerName.toLowerCase(), chest);

				++loadedChests;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		log.info("[Alpha Chest] loaded " + loadedChests + " chests");
	}

	public void save() {
		int savedChests = 0;

		dataFolder.mkdirs();

		for (String playerName : chests.keySet()) {
			final IInventory chest = chests.get(playerName);

			try {
				final File chestFile = new File(dataFolder, playerName + ".chest");
				if (chestFile.exists())
					chestFile.delete();
				chestFile.createNewFile();

				final BufferedWriter out = new BufferedWriter(new FileWriter(chestFile));

				for (ItemStack stack : chest.getContents()) {
					if (stack != null)
						out.write(stack.id + ":" + stack.count + ":" + stack.getData() + "\r\n");
					else
						out.write("0:0:0\r\n");
				}

				out.close();

				savedChests++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		log.info("[Alpha Chest] saved " + savedChests + " chests");
	}
}
