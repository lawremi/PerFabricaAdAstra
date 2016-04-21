package org.pfaa.fabrica.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PaintableDrywallBlock extends PaintableBlock implements IDrywallBlock {

	private DrywallBlock delegate;

	public PaintableDrywallBlock() {
		this(new DrywallBlock());
	}
	
	public PaintableDrywallBlock(DrywallBlock delegate) {
		super(delegate);
		this.delegate = delegate;
	}
	
	@Override
	public boolean jointsFilled(IBlockAccess world, int x, int y, int z) {
		return this.delegate.jointsFilled(world, x, y, z);
	}

	@Override
	public void fillJoints(World world, int x, int y, int z) {
		this.delegate.fillJoints(world, x, y, z);
	}

}
