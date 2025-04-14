package com.sarahk.togglyfiers.api.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public interface ToggleBehavior {

	static void removeBlock(Level level, BlockPos pos) {
		FluidState fluidstate = level.getFluidState(pos);
		level.setBlock(pos, fluidstate.createLegacyBlock(), Block.UPDATE_ALL);
	}

	boolean applicableTo(ItemStack stack);

	Result placeDown(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData);

	Result pickUp(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData);

	default int priority() {
		return 0;
	}

	default int compare(ToggleBehavior other)
	{
		return priority() - other.priority();
	}

	static BlockHitResult hitResult(BlockPos pos, Direction direction) {
		return new BlockHitResult(
				new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ()).add(0.5f, 0.5f, 0.5f),
				direction,
				pos,
				false);
	}

	enum Result {
		SUCCESS,
		FAIL,
		PASS;

		public boolean consumesAction() {
			return this != PASS;
		}
	}
}
