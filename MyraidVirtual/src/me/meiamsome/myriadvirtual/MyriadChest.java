package me.meiamsome.myriadvirtual;

import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;

import org.bukkit.entity.Player;

public class MyriadChest extends InventoryLargeChest{
	Player owner;
	public MyriadChest(String s, TileEntityChest a, TileEntityChest b, Player play) {
		super(s,a,b);
		owner=play;
	}
	public ItemStack[] getContents() {
		//if(owner.hasPe rmission("mv.doubleChest")) return super.getContents();
		ItemStack[] ret=new ItemStack[27];
		System.arraycopy(super.getContents(), 0, ret, 0, 27);
		return ret;
	}
	@Override
	public int getSize() {
		//if(owner.hasPermission("mv.doubleChest")) return super.getSize();
		return 1;
	}
	@Override
	public void f() {
		// TODO Auto-generated method stub
		//super.f();
	}
	@Override
	public void g() {
		// TODO Auto-generated method stub
		//super.g();
	}
	
	
}
