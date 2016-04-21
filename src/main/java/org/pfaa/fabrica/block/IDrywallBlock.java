package org.pfaa.fabrica.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IDrywallBlock {
	public boolean jointsFilled(IBlockAccess world, int x, int y, int z);
	public void fillJoints(World world, int x, int y, int z);
}
