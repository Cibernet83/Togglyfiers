package com.sarahk.togglyfiers.api.behavior;

import com.sarahk.togglyfiers.Togglyfiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayer;

public class BlockPlaceBehavior implements ToggleBehavior {
	@Override
	public boolean applicableTo(ItemStack stack) {
		return stack.getItem() instanceof BlockItem;
	}

	@Override
	public Result placeDown(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData) {

		if (!(stack.getItem() instanceof BlockItem blockItem))
			return Result.PASS;

		level.destroyBlock(pos, true);

		for (int i = 0; i < Direction.values().length; i++) {

			Direction currentDirection = Direction.values()[(i + facing.ordinal()) % Direction.values().length];
			FakePlayer fakePlayer = new FakePlayer(level, Togglyfiers.FAKE_PLAYER_PROFILE);
			fakePlayer.moveTo(pos.getCenter(), currentDirection.toYRot(), currentDirection.getStepY() * 90);
			fakePlayer.setYHeadRot(fakePlayer.getYRot());

			BlockHitResult hit = ToggleBehavior.hitResult(pos, currentDirection);
			if (blockItem.place(new BlockPlaceContext(level, fakePlayer, InteractionHand.MAIN_HAND, stack.copy(), hit)).consumesAction()) {
				if (additionalData.contains("block_entity_data", Tag.TAG_COMPOUND) && level.getBlockEntity(pos) instanceof BlockEntity blockEntity)
					blockEntity.loadCustomOnly(additionalData, level.registryAccess());
				return Result.SUCCESS;
			}
		}

		return Result.FAIL;
	}

	@Override
	public Result pickUp(ServerLevel level, BlockPos pos, Direction facing, ItemStack stack, CompoundTag additionalData) {

		if (level.getBlockState(pos).getBlock().asItem().equals(stack.getItem())) {
			if (level.getBlockEntity(pos) instanceof BlockEntity blockEntity)
				additionalData.put("block_entity_data", blockEntity.saveCustomOnly(level.registryAccess()));

			ToggleBehavior.removeBlock(level, pos);
			return Result.SUCCESS;
		} else {
			stack.shrink(1);
			return Result.FAIL;
		}
	}
}
