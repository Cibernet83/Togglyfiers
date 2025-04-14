package com.sarahk.togglyfiers.api.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

public class EmptyBehavior implements ToggleBehavior {
	@Override
	public boolean applicableTo(ItemStack stack) {
		return stack.isEmpty();
	}

	@Override
	public Result placeDown(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData) {

		ToggleBehavior.removeBlock(level, pos);

		return Result.SUCCESS;
	}

	@Override
	public Result pickUp(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData) {
		return Result.SUCCESS;
	}
}
