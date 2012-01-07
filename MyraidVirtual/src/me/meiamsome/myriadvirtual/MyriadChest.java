package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;

import org.bukkit.entity.Player;

public class MyriadChest extends TileEntityChest implements IInventory{
	Player owner;
	String name;
	ItemStack[] stack;
	boolean type;
	public MyriadChest(String s, Player play) {
		owner=play;
		name=s;
		stack=new ItemStack[54];
		type=play.hasPermission("mv.largeChest");
		if(name.length()>15) {
			if(name.lastIndexOf(' ')>15) {
				name="Virtual Chest";
			} else {
				name=name.substring(0,name.lastIndexOf(' '));
			}
		}
	}
	public ItemStack[] getContents() {
		return stack;
		//ItemStack[] ret = new ItemStack[27];
		//System.arraycopy(stack, 0, ret, 0, 27);
		//return ret;
		
	}
	@Override
	public int getSize() {
		return type?54:27;
	}
	@Override
	public boolean a(EntityHuman arg0) {
		return true;
	}
	@Override
	public void f() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void g() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ItemStack getItem(int i) {
		return stack[i];
	}
	@Override
	public int getMaxStackSize() {
		// TODO Auto-generated method stub
		return 64;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setItem(int i, ItemStack in) {
		stack[i]=in;
		
	}
	@Override
	public ItemStack splitStack(int i, int j) {
		super.setItem(0, getItem(i));
		ItemStack ret=super.splitStack(0, j);
		setItem(i,super.getItem(0));
		return ret;
		/*ItemStack a=getItem(i), b=new ItemStack(a.getItem(),j);
		if(j==a.count) {
			a=null;
		} else a.count-=j;
		return b;*/
		
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}
	
	
}
