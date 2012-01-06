package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.TileEntityChest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChestCommands implements CommandExecutor {

	private final MyriadVirtual plugin;
	private final MyriadChestManager chestManager;

	public ChestCommands(MyriadVirtual plugin, MyriadChestManager chestManager) {
		this.plugin = plugin;
		this.chestManager = chestManager;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final String name = command.getName();
		if (name.equals("chest"))
			return performChestCommand(sender, args);
		else if (name.equalsIgnoreCase("clearchest"))
			return performClearChestCommand(sender, args);
		else if (name.equalsIgnoreCase("savechests"))
			return performSaveChestsCommand(sender, args);
		else
			return false;
	}

	
	private boolean performChestCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			EntityPlayer eh;
			if (args.length == 1) {
				if (sender.hasPermission("ac.admin")) {
					eh = ((CraftPlayer) sender).getHandle();
					eh.a(chestManager.getChest(args[0]));
					eh.a(new TileEntityChest());
				} else {
					sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				}
				return true;
	
			} else if (args.length == 0) {
				if (sender.hasPermission("ac.chest")) {
					eh = ((CraftPlayer) sender).getHandle();
					eh.a((MyriadChest)chestManager.getChest(player.getName()));
				} else {
					sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				}
				return true;
			}
		}
		return false;
	}

	private boolean performClearChestCommand(CommandSender sender, String[] args) {
		if (args.length >= 1) {
			if ((sender instanceof Player) && !sender.hasPermission("ac.admin")) {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to clear other user's chests.");
				return true;
			}
			chestManager.removeChest(args[0]);
			sender.sendMessage(ChatColor.GREEN+"Successfully cleared " + args[0] + "\'s chest.");
			return true;
		} else {
			if (sender instanceof Player) {
				final Player player = (Player) sender;
				if (!sender.hasPermission("ac.chest")) {
					sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				} else {
					chestManager.removeChest(player.getName());
					sender.sendMessage(ChatColor.GREEN+"Successfully cleared your chest.");
				}
				return true;
			}
		}
		return false;
	}

	private boolean performSaveChestsCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (!sender.hasPermission("ac.save")) {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				return true;
			}
		}

		chestManager.save();
		sender.sendMessage(ChatColor.GREEN+"Saved all chests.");
		return true;
	}

}
