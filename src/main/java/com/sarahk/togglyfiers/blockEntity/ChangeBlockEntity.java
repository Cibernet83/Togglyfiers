package com.sarahk.togglyfiers.blockEntity;

import com.sarahk.togglyfiers.block.ChangeBlock;
import com.sarahk.togglyfiers.data.TogglyfiersSaveData;
import com.sarahk.togglyfiers.data.components.TogglyOwnerComponent;
import com.sarahk.togglyfiers.registries.TogglyfiersBlockEntities;
import com.sarahk.togglyfiers.registries.TogglyfiersDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class ChangeBlockEntity extends BlockEntity {

	private TogglyfierBlockEntity owner;

	public ChangeBlockEntity(BlockPos pos, BlockState blockState) {
		super(TogglyfiersBlockEntities.CHANGE_BLOCK.get(), pos, blockState);
	}

	public void setOwner(ServerLevel ownerLevel, UUID ownerId, boolean updateOwner) {
		if (ownerLevel != null && ownerLevel.getBlockEntity(TogglyfiersSaveData.getTogglyfierPos(ownerLevel, ownerId)) instanceof TogglyfierBlockEntity togglyfier)
			setOwner(togglyfier, updateOwner);
	}

	public void setOwner(TogglyfierBlockEntity owner, boolean updateOwner) {

		if (updateOwner && this.owner != owner) {
			if (this.owner != null)
				this.owner.removeChangeBlock(this);
			if (owner != null)
				owner.addChangeBlock(this);
		}

		this.owner = owner;
	}

	public TogglyfierBlockEntity getOwner() {
		return owner;
	}

	public Direction getFacing() {
		return level.getBlockState(getBlockPos()).getValue(ChangeBlock.FACING);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		if (owner != null && owner.hasLevel()) {
			tag.putString("owner_dimension", owner.getLevel().dimension().location().toString());
			tag.putUUID("owner_id", owner.getId());
		}

	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.hasUUID("owner_id") && tag.contains("owner_dimension", CompoundTag.TAG_STRING) && getLevel() instanceof ServerLevel serverLevel)
			setOwner(serverLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("owner_dimension")))), tag.getUUID("owner_id"), false);

	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {

		TogglyOwnerComponent owner = componentInput.get(TogglyfiersDataComponents.TOGGLYFIER_OWNER);

		if (owner != null && getLevel() instanceof ServerLevel serverLevel)
			setOwner(serverLevel.getServer().getLevel(owner.levelKey()), owner.id(), true);

	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);

		if (owner != null)
			components.set(TogglyfiersDataComponents.TOGGLYFIER_OWNER, new TogglyOwnerComponent(owner.getLevel().dimension(), owner.getId()));
	}
}
