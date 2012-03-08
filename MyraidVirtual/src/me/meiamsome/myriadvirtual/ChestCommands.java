package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Bukkit;
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
		else if (name.equalsIgnoreCase("changechest"))
			return performChangeChestsCommand(sender, args);
		else
			return false;
	}

	
	private boolean performChestCommand(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			EntityPlayer eh;
			if (args.length == 1) {
				if (sender.hasPermission("mv.admin")) {
					eh = ((CraftPlayer) sender).getHandle();
					MyriadChest chest = (MyriadChest) chestManager.getChest(args[0]);
					chest.fix(player);
					eh.openContainer(chest);
					//eh.a(new TileEntityChest());
				} else {
					sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				}
				return true;
	
			} else if (args.length == 0) {
				if (sender.hasPermission("mv.chest")) {
					eh = ((CraftPlayer) sender).getHandle();
					MyriadChest chest = (MyriadChest) chestManager.getChest(player.getName());
					chest.fix(player);
					eh.openContainer(chest);
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
			if ((sender instanceof Player) && !sender.hasPermission("mv.admin")) {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to clear other user's chests.");
				return true;
			}
			chestManager.removeChest(args[0]);
			sender.sendMessage(ChatColor.GREEN+"Successfully cleared " + args[0] + "\'s chest.");
			return true;
		} else {
			if (sender instanceof Player) {
				final Player player = (Player) sender;
				if (!sender.hasPermission("mv.chest")) {
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
			if (!sender.hasPermission("mv.save")) {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				return true;
			}
		}

		chestManager.save();
		sender.sendMessage(ChatColor.GREEN+"Saved all chests.");
		return true;
	}

	private boolean performChangeChestsCommand(CommandSender sender, String[] args) {
		if ((sender instanceof Player) && !sender.hasPermission("mv.admin")) {
			sender.sendMessage(ChatColor.RED+"You\'re not allowed to alter chests.");
			return true;
		}
		Player who=null;
		boolean to;
		if(sender instanceof Player) who=(Player) sender;
		if(args.length==2) {
			who=Bukkit.getPlayer(args[0]);
			to=args[1].equalsIgnoreCase("double");
		} else to=args[0].equalsIgnoreCase("double");
		if(who==null) {
			sender.sendMessage(ChatColor.RED+"No user specified!");
			return true;
		}
		MyriadChest chest = (MyriadChest) chestManager.getChest(who.getName());
		chest.type=to;
		sender.sendMessage(ChatColor.GREEN+"Chest updated!");
		return true;
	}

}
