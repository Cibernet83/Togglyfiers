package com.sarahk.togglyfiers.blockEntity;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.block.ChangeBlock;
import com.sarahk.togglyfiers.block.TogglyfierBlock;
import com.sarahk.togglyfiers.data.TogglyfiersSaveData;
import com.sarahk.togglyfiers.data.components.TogglyOwnerComponent;
import com.sarahk.togglyfiers.registries.TogglyfiersBlockEntities;
import com.sarahk.togglyfiers.registries.TogglyfiersBlocks;
import com.sarahk.togglyfiers.registries.TogglyfiersDataComponents;
import com.sarahk.togglyfiers.registries.TogglyfiersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TogglyfierBlockEntity extends BlockEntity {

	private UUID id;
	private final List<ChangeBlock.Entry> changeBlocks = new ArrayList<>();

	public TogglyfierBlockEntity(BlockPos pos, BlockState blockState) {
		super(TogglyfiersBlockEntities.TOGGLYFIER.value(), pos, blockState);
	}

	public boolean isToggled() {
		return getBlockState().getValue(TogglyfierBlock.TOGGLED);
	}

	public boolean isInEditMode() {
		return getBlockState().getValue(TogglyfierBlock.EDIT_MODE);
	}

	public void setChangeBlockToggle(ChangeBlock.Entry changeBlock, boolean value) {
		changeBlock.getLevel().setBlock(changeBlock.getPos(), value ?
				Blocks.LIME_WOOL.defaultBlockState() : Blocks.RED_WOOL.defaultBlockState(), Block.UPDATE_ALL);
	}

	public void setToggled(boolean value) {

		getLevel().setBlock(getBlockPos(), getBlockState().setValue(TogglyfierBlock.TOGGLED, value), Block.UPDATE_ALL);

		if(!isInEditMode())
			for (ChangeBlock.Entry changeBlock : changeBlocks) {
				setChangeBlockToggle(changeBlock, value);
			}
	}

	public void setInEditMode(boolean value) {

		getLevel().setBlock(getBlockPos(), getBlockState().setValue(TogglyfierBlock.EDIT_MODE, value), Block.UPDATE_ALL);

		boolean toggled = isToggled();
		for (ChangeBlock.Entry changeBlock : changeBlocks) {

			if(value)
				changeBlock.getLevel().setBlock(changeBlock.getPos(), TogglyfiersBlocks.CHANGE_BLOCK.get().defaultBlockState().setValue(ChangeBlock.FACING, changeBlock.getDirection()), Block.UPDATE_ALL);
			else setChangeBlockToggle(changeBlock, toggled);
		}
	}

	public ItemStack createChangeBlockItem(int count) {

		ItemStack stack = TogglyfiersItems.CHANGE_BLOCK.toStack();
		stack.set(TogglyfiersDataComponents.TOGGLYFIER_OWNER, new TogglyOwnerComponent(getLevel().dimension(), getId()));
		stack.setCount(count);
		return stack;
	}

	public ItemStack getDefaultEnabled() {
		return ItemStack.EMPTY;
	}

	public ItemStack getDefaultDisabled() {
		return ItemStack.EMPTY;
	}

	public void addChangeBlock(@NonNull ChangeBlockEntity changeBlock) {

		if (!changeBlock.hasLevel())
			Togglyfiers.LOGGER.atError().log("Attempted to add Change Block ${} to Togglyfier at ${} before being initialized.", changeBlock.getBlockPos(), getBlockPos());
		else if (!(changeBlock.getLevel() instanceof ServerLevel serverLevel))
			Togglyfiers.LOGGER.atError().log("Attempted to add Change Block ${} to Togglyfier at ${} on the client.", changeBlock.getBlockPos(), getBlockPos());
		else {
			removeChangeBlock(changeBlock);
			changeBlocks.add(new ChangeBlock.Entry(serverLevel, changeBlock.getBlockPos(), changeBlock.getFacing(), getDefaultEnabled().split(1), getDefaultDisabled().split(1)));
		}
	}

	public boolean removeChangeBlock(@NonNull ChangeBlockEntity changeBlock) {
		if (!changeBlock.hasLevel())
			Togglyfiers.LOGGER.atError().log("Attempted to remove Change Block ${} from Togglyfier at ${} before being initialized.", changeBlock.getBlockPos(), getBlockPos());
		else if (!(changeBlock.getLevel() instanceof ServerLevel serverLevel))
			Togglyfiers.LOGGER.atError().log("Attempted to remove Change Block ${} from Togglyfier at ${} on the client.", changeBlock.getBlockPos(), getBlockPos());
		else
			return changeBlocks.removeIf(entry -> entry.getLevel().equals(changeBlock.getLevel()) && entry.getPos().equals(changeBlock.getBlockPos()));
		return false;
	}

	public UUID getId() {
		return id;
	}

	public void assignId() {

		if (id != null) return;

		if (!hasLevel())
			Togglyfiers.LOGGER.atError().log("Attempted to assign an id to Togglyfier at ${} before being initialized.", getBlockPos());
		else if (!(level instanceof ServerLevel serverLevel))
			Togglyfiers.LOGGER.atError().log("Attempted to assign an id to Togglyfier at ${} on the client.", getBlockPos());
		else while (id == null || TogglyfiersSaveData.isTogglyfierValid(serverLevel, id))
				id = UUID.randomUUID();
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.hasUUID("togglyfier_id"))
			id = tag.getUUID("togglyfier_id");

		if (hasLevel() && !getLevel().isClientSide())
			TogglyfiersSaveData.indexTogglyfier(this);

		changeBlocks.clear();
		ListTag list = tag.getList("change_blocks", Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++)
			changeBlocks.add(ChangeBlock.Entry.fromNbt(list.getCompound(i), registries));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		if (id != null)
			tag.putUUID("togglyfier_id", id);

		ListTag list = new ListTag();
		changeBlocks.forEach(entry -> list.add(entry.toNbt(registries)));
		tag.put("change_blocks", list);
	}
}
