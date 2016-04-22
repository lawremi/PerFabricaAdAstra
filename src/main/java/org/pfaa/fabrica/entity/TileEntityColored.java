package org.pfaa.fabrica.entity;

import net.minecraftforge.common.util.ForgeDirection;
import openmods.sync.SyncableIntArray;
import openmods.tileentity.SyncedTileEntity;

public class TileEntityColored extends SyncedTileEntity {

	private SyncableIntArray baseColors;
	
	public TileEntityColored() {
		this.syncMap.addUpdateListener(createRenderUpdateListener());
	}
	
	@Override
	protected void createSyncedFields() {
		baseColors = new SyncableIntArray(new int[] { 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF });
	}

	public int getColor(int renderSide) {
		return baseColors.getValue(renderSide);
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	public boolean setColor(int color, ForgeDirection... sides) {
		for (ForgeDirection side : sides) {
			final int sideId = side.ordinal();
			baseColors.setValue(sideId, color);
		}

		boolean hasChanged = baseColors.isDirty();
		if (!worldObj.isRemote) sync();
		return hasChanged;
	}
}
