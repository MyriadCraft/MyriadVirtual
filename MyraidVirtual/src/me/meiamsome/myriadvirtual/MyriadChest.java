package me.meiamsome.myriadvirtual;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MyriadChest extends TileEntityChest implements IInventory{
	String lastOpen;
	String owner;
	String name;
	ItemStack[] stack;
	Boolean type = null;
	public MyriadChest(String s, String play) {
		owner=play;
		name=s;
		stack=new ItemStack[54];
		if(name.length()>15) {
			if(name.lastIndexOf(' ')>15) {
				name="Virtual Chest";
			} else {
				name=name.substring(0,name.lastIndexOf(' '));
			}
		}
		if(Bukkit.getPlayer(play) != null)
		   type=Bukkit.getPlayer(play).hasPermission("mv.largeChest");
	}
	public ItemStack[] getContents() {
		return stack;
		//ItemStack[] ret = new ItemStack[27];
		//System.arraycopy(stack, 0, ret, 0, 27);
		//return ret;
		
	}
	@Override
	public int getSize() {
		if(type==null) if(Bukkit.getPlayer(owner) != null) {
				type=Bukkit.getPlayer(owner).hasPermission("mv.largeChest");
		} else return 54;//Return 54 if the owner is not online or hasn't been since chest existed
		return type?54:27;
	}
	@Override
	public boolean a(EntityHuman arg0) {
		return true;
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
		//No idea how to implement?: HAX
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
		fix(null);// Calls super.update()
	}
	
	public void fix(Player opener) {
		boolean gDone=true;
		if(getSize()==27) {
			for(int i=27;i<54;i++) {
				if(getItem(i)!=null) {
					ItemStack is = getItem(i);
					boolean done=false;
					for(int a=0;a<27&&!done;a++) {
						if(getItem(a)!=null) if(getItem(a).doMaterialsMatch(is)) {
							int stack1=is.count, stack2=getItem(a).count;
							if(stack1+stack2<is.getMaxStackSize()) {
								getItem(a).count=stack1+stack2;
								done=true;
							} else {
								is.count=stack1+stack2-is.getMaxStackSize();
								getItem(a).count=is.getMaxStackSize();
							}
						}
					}
					for(int a=0;a<27&&!done;a++) {
						if(getItem(a)==null) {
							setItem(a, is);
							done=true;
						}
					}
					if(!done) {
						gDone=false;
					} else setItem(i, null);
				}
			}
		} 
		if(!gDone) {
			if(opener!=null) {
				lastOpen=opener.getName();
				opener.sendMessage(ChatColor.RED+"There are more items than fit the chest. Free some space!");
			}
		} else {
			if(opener==null && lastOpen!=null){
				Bukkit.getPlayer(lastOpen).sendMessage(ChatColor.GREEN+"Space issue resolved.");
			}
			lastOpen=null;
		}
		super.update();
	}
	
	
}
