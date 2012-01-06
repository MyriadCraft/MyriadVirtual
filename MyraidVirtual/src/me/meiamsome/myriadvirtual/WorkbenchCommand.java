package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet100OpenWindow;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class WorkbenchCommand implements CommandExecutor {

	private final MyriadVirtual plugin;

	public WorkbenchCommand(MyriadVirtual plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (sender.hasPermission("ac.workbench")) {
				final EntityPlayer eh = ((CraftPlayer) sender).getHandle();
				
				final int windowId = 1; // should be safe to use a static window ID
		        eh.netServerHandler.sendPacket(new Packet100OpenWindow(windowId, 1, "Virtual Crafting", 9));
		        eh.activeContainer = new MyriadWorkbench(eh, windowId);
			} else {
				sender.sendMessage(ChatColor.RED+"You\'re not allowed to use this command.");
			}
			return true;
		}
		return false;
	}

}
