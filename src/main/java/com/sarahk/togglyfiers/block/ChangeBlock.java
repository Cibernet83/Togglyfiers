package com.sarahk.togglyfiers.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class ChangeBlock extends DirectionalBlock {

	public ChangeBlock(Properties properties) {
		super(properties);
	}


	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ?
				context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	private final MapCodec<ChangeBlock> CODEC = simpleCodec(ChangeBlock::new);

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}
}
