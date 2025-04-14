package com.sarahk.togglyfiers.block;

import com.mojang.serialization.MapCodec;
import com.sarahk.togglyfiers.blockEntity.ChangeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class ChangeBlock extends DirectionalBlock implements EntityBlock {

	public ChangeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {

		if(!level.isClientSide() && level.getBlockEntity(pos) instanceof ChangeBlockEntity changeBlock && changeBlock.hasOwner() && changeBlock.isDestroyed())
			changeBlock.getOwner().removeChangeBlock(changeBlock);
		super.onRemove(state, level, pos, newState, movedByPiston);
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ChangeBlockEntity(pos, state);
	}

	public static class Entry {
		private final ServerLevel level;
		private final BlockPos pos;
		private final Direction direction;
		private ItemStack enabledStack = ItemStack.EMPTY;
		private ItemStack disabledStack = ItemStack.EMPTY;
		private CompoundTag enabledAdditional = new CompoundTag();
		private CompoundTag disabledAdditional = new CompoundTag();

		public Entry(ServerLevel level, BlockPos pos, Direction direction) {
			this.level = level;
			this.pos = pos;
			this.direction = direction;
		}

		public Entry(ServerLevel level, BlockPos pos, Direction direction, ItemStack enabledStack, ItemStack disabledStack) {
			this(level, pos, direction);
			this.enabledStack = enabledStack;
			this.disabledStack = disabledStack;
		}

		public CompoundTag toNbt(HolderLookup.Provider registryAccess) {

			CompoundTag tag = new CompoundTag();

			tag.putString("dimension", level.dimension().location().toString());
			tag.put("pos", NbtUtils.writeBlockPos(pos));
			tag.putString("direction", direction.getName());

			if(!enabledStack.isEmpty())
				tag.put("enabled_item", enabledStack.save(registryAccess));
			if(!enabledAdditional.isEmpty())
				tag.put("enabled_data", enabledAdditional);
			if(!disabledStack.isEmpty())
				tag.put("disabled_item", disabledStack.save(registryAccess));
			if(!disabledAdditional.isEmpty())
				tag.put("disabled_data", disabledAdditional);

			return tag;
		}

		@Nullable
		public static Entry fromNbt(CompoundTag tag, HolderLookup.Provider registryAccess) {

			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if (server == null || !tag.contains("dimension", Tag.TAG_STRING) || !tag.contains("pos") || !tag.contains("direction", Tag.TAG_STRING))
				return null;

			ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("dimension"))));
			var pos = NbtUtils.readBlockPos(tag, "pos");

			if (level == null || pos.isEmpty())
				return null;

			Entry entry = new Entry(
					level,
					pos.get(),
					Direction.byName(tag.getString("direction"))
			);

			ItemStack.parse(registryAccess, tag.getCompound("enabled_item")).ifPresent(entry::setEnabledStack);
			ItemStack.parse(registryAccess, tag.getCompound("disabled_item")).ifPresent(entry::setDisabledStack);

			if (tag.contains("enabled_data", Tag.TAG_COMPOUND))
				entry.setEnabledAdditional(tag.getCompound("enabled_data"));
			if (tag.contains("disabled_data", Tag.TAG_COMPOUND))
				entry.setDisabledAdditional(tag.getCompound("disabled_data"));

			return entry;
		}

		public ServerLevel getLevel() {
			return level;
		}

		public BlockPos getPos() {
			return pos;
		}

		public Direction getDirection() {
			return direction;
		}

		public ItemStack getDisabledStack() {
			return disabledStack;
		}

		public ItemStack getEnabledStack() {
			return enabledStack;
		}

		public CompoundTag getDisabledAdditional() {
			return disabledAdditional;
		}

		public CompoundTag getEnabledAdditional() {
			return enabledAdditional;
		}

		public void setDisabledAdditional(CompoundTag disabledAdditional) {
			this.disabledAdditional = disabledAdditional;
		}

		public void setDisabledStack(ItemStack disabledStack) {
			this.disabledStack = disabledStack;
		}

		public void setEnabledAdditional(CompoundTag enabledAdditional) {
			this.enabledAdditional = enabledAdditional;
		}

		public void setEnabledStack(ItemStack enabledStack) {
			this.enabledStack = enabledStack;
		}

		@Override
		public String toString() {
			return "Change Block: %s in %s".formatted(getPos(), getLevel().dimension());
		}
	}
}
