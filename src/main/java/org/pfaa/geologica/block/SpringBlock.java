package org.pfaa.geologica.block;

import java.util.List;
import java.util.Random;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.geologica.GeologicaBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Derived from the BlockSpring class from Buildcraft.
 * 
 * Distributed under the terms of the Minecraft Mod Public
 * License 2.0, or MMPL2. Please check the contents of the license located in
 * https://github.com/jakimfett/MMPLv2/blob/master/LICENSE.md
 */

public class SpringBlock extends Block {

	public interface Spring {
		public int getTickRate();
		public int getChance();
		public Block getFluidBlock();
	}
	
	public enum Springs implements Spring {
		WATER(5, -1, Blocks.water),
		LAVA(1200, 3, Blocks.lava),
		LIGHT_OIL(6000, 8, GeologicaBlocks.LIGHT_OIL),
		MEDIUM_OIL(6000, 4, GeologicaBlocks.MEDIUM_OIL),
		HEAVY_OIL(6000, 8, GeologicaBlocks.HEAVY_OIL),
		EXTRA_HEAVY_OIL(6000, 3, GeologicaBlocks.EXTRA_HEAVY_OIL),
		NATURAL_GAS(600, 5, GeologicaBlocks.NATURAL_GAS),
		BRINE(300, 4, GeologicaBlocks.BRINE),
		STEAM(25, 3, ChemicaBlocks.STEAM)
		;
		
		private final int tickRate, chance;
		private final Block fluidBlock;
		
		private Springs(int tickRate, int chance, Block fluidBlock) {
			this.tickRate = tickRate;
			this.chance = chance;
			this.fluidBlock = fluidBlock;
		}

		@Override
		public int getTickRate() {
			return this.tickRate;
		}

		@Override
		public int getChance() {
			return this.chance;
		}

		@Override
		public Block getFluidBlock() {
			return this.fluidBlock;
		}
	}

	private Spring[] springs;
	
	public SpringBlock() {
		this(Springs.values());
	}
	
	public SpringBlock(Spring... springs) {
		super(Material.rock);
		setBlockUnbreakable();
		setResistance(6000000.0F);
		setStepSound(soundTypeStone);

		disableStats();
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabBlock);
		
		this.springs = springs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list) {
		for (int meta = 0; meta < this.springs.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		assertSpring(world, x, y, z, random);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, this.getSpring(meta).getTickRate());
	}

	private Spring getSpring(int meta) {
		if (meta < 0 || meta >= this.springs.length) {
			return Springs.WATER;
		}
		return this.springs[meta];
	}

	private void assertSpring(World world, int x, int y, int z, Random random) {
		int meta = world.getBlockMetadata(x, y, z);
		Spring spring = this.getSpring(meta);
		world.scheduleBlockUpdate(x, y, z, this, spring.getTickRate());
		if (!world.isAirBlock(x, y + 1, z)) {
			return;
		}
		if (spring.getChance() != -1 && random.nextInt(spring.getChance()) != 0) {
			return;
		}
		Block fluid = spring.getFluidBlock();
		int nextY;
		for (nextY = y + 1; nextY < world.getActualHeight() && world.getBlock(x, nextY, z) == fluid; nextY++) { }
		int curY = nextY - 1;
		boolean seepable = nextY < world.getActualHeight() && world.getBlock(x, nextY, z) == Blocks.air ||
				world.getBlock(x, nextY, z) instanceof BlockFalling;
		boolean canSpring = seepable && 
								(y == curY || 
									(world.getBlock(x+1, curY, z).getMaterial().isSolid() &&
									 world.getBlock(x-1, curY, z).getMaterial().isSolid() &&
									 world.getBlock(x, curY, z+1).getMaterial().isSolid() &&
									 world.getBlock(x, curY, z-1).getMaterial().isSolid()));
		if (canSpring) {
			world.setBlock(x, nextY, z, fluid);
		}
	}

	// Prevents updates on chunk generation
	@Override
	public boolean func_149698_L() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("bedrock");
	}
}