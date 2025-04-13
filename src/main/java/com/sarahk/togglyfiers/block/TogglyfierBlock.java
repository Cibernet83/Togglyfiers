package com.sarahk.togglyfiers.block;

import com.sarahk.togglyfiers.blockEntity.TogglyfierBlockEntity;
import com.sarahk.togglyfiers.data.TogglyfiersSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TogglyfierBlock extends Block implements EntityBlock {

	public static BooleanProperty TOGGLED = BooleanProperty.create("toggled");
	public static BooleanProperty EDIT_MODE = BooleanProperty.create("edit_mode");

	public TogglyfierBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(TOGGLED, false).setValue(EDIT_MODE, true));
	}



	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(EDIT_MODE, TOGGLED);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

		if(level.isClientSide()) return InteractionResult.SUCCESS;

		if(!(level.getBlockEntity(pos) instanceof TogglyfierBlockEntity togglyfier)) return InteractionResult.PASS;


		if(player.getMainHandItem().is(Items.STICK))
		{
			togglyfier.setInEditMode(!togglyfier.isInEditMode());
		} else if(player.getMainHandItem().is(Items.REDSTONE_TORCH))
		{
			togglyfier.setToggled(!togglyfier.isToggled());
		}
		else player.addItem(togglyfier.createChangeBlockItem(1));


		return InteractionResult.SUCCESS;
	}

	@Override
	public @Nullable PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TogglyfierBlockEntity(blockPos, blockState);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		super.onPlace(state, level, pos, oldState, movedByPiston);

		if(!level.isClientSide() && level.getBlockEntity(pos) instanceof TogglyfierBlockEntity togglyfier)
			TogglyfiersSaveData.indexTogglyfier(togglyfier);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);

		if(level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof TogglyfierBlockEntity togglyfier)
			TogglyfiersSaveData.invalidateTogglyfier(serverLevel, togglyfier.getId());
	}
}
