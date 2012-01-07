package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;

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
		else if (name.equalsIgnoreCase("fixchest"))
			return performFixChestsCommand(sender, args);
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
					eh.a(chestManager.getChest(args[0]));
					//eh.a(new TileEntityChest());
				} else {
					sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
				}
				return true;
	
			} else if (args.length == 0) {
				if (sender.hasPermission("ac.chest")) {
					eh = ((CraftPlayer) sender).getHandle();
					eh.a(chestManager.getChest(player.getName()));
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

	private boolean performFixChestsCommand(CommandSender sender, String[] args) {
		if (args.length >= 1) {
			if ((sender instanceof Player) && !sender.hasPermission("mv.admin")) {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to fix chests.");
				return true;
			}
			MyriadChest chest=(MyriadChest) chestManager.getChest(args[0]);
			if(chest.getSize()==27) {
				for(int i=27;i<54;i++) {
					if(chest.getItem(i)!=null) {
						ItemStack is = chest.getItem(i);
						boolean done=false;
						for(int a=0;a<27&&!done;a++) {
							if(chest.getItem(a).doMaterialsMatch(is)) {
								int stack1=is.count, stack2=chest.getItem(a).count;
								if(stack1+stack2<is.getMaxStackSize()) {
									chest.getItem(a).count=stack1+stack2;
									done=true;
								} else {
									is.count=stack1+stack2-is.getMaxStackSize();
									chest.getItem(a).count=is.getMaxStackSize();
								}
							}
						}
						for(int a=0;a<27&&!done;a++) {
							if(chest.getItem(a)==null) {
								chest.setItem(a, is);
								done=true;
							}
						}
						if(!done) {
							sender.sendMessage(ChatColor.RED+"Failed to fix "+ args[0] + "\'s chest! Please make more space.");
							return true;
						}
						chest.setItem(i, null);
					}
				}
				sender.sendMessage(ChatColor.GREEN+"Successfully fixed " + args[0] + "\'s chest.");
			} else sender.sendMessage(ChatColor.GREEN+ args[0] + "\'s chest doesn't need fixing.");
			return true;
		} else {
			 sender.sendMessage(ChatColor.RED+"Specify whose chest to fix");
		}
		return false;
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
