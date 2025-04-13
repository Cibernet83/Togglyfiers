package com.sarahk.togglyfiers.data;

import com.sarahk.togglyfiers.Togglyfiers;
import com.sarahk.togglyfiers.blockEntity.TogglyfierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TogglyfiersSaveData extends SavedData {

	private final Map<UUID, BlockPos> togglyfierMap = new HashMap<>();

	public static void indexTogglyfier(TogglyfierBlockEntity togglyfier) {

		if(!togglyfier.hasLevel())
			Togglyfiers.LOGGER.atError().log("Attempted to index Togglyfier at ${} before being initialized.", togglyfier.getBlockPos());
		else if(togglyfier.getLevel().isClientSide())
			Togglyfiers.LOGGER.atError().log("Attempted to index Togglyfier at ${} on the client.", togglyfier.getBlockPos());
		else {

			togglyfier.assignId();
			indexTogglyfier((ServerLevel) togglyfier.getLevel(), togglyfier.getId(), togglyfier.getBlockPos());
		}
	}

	public static void indexTogglyfier(ServerLevel level, UUID id, BlockPos pos) {
		var save = compute(level);
		save.togglyfierMap.put(id, pos);
		save.setDirty();
	}

	public static void invalidateTogglyfier(ServerLevel level, UUID id) {
		var save = compute(level);
		save.togglyfierMap.remove(id);
		save.setDirty();
	}

	public static boolean isTogglyfierValid(ServerLevel level, UUID id) {
		return compute(level).togglyfierMap.containsKey(id);
	}

	public static BlockPos getTogglyfierPos(ServerLevel level, UUID id) {
		return compute(level).togglyfierMap.get(id);
	}

	public static TogglyfiersSaveData compute(ServerLevel level)
	{
		return level.getDataStorage().computeIfAbsent(new Factory<>(TogglyfiersSaveData::new, TogglyfiersSaveData::load), Togglyfiers.MODID);
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {

		ListTag list = new ListTag();

		togglyfierMap.forEach((key, pos) -> {
			CompoundTag entry = new CompoundTag();
			entry.putUUID("id", key);
			entry.put("pos", NbtUtils.writeBlockPos(pos));
			list.add(entry);
		});

		compoundTag.put("togglyfiers", list);

		return compoundTag;
	}

	private static TogglyfiersSaveData load(CompoundTag tag, HolderLookup.Provider registries) {

		TogglyfiersSaveData data = new TogglyfiersSaveData();

		ListTag list = tag.getList("togglyfiers", ListTag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			NbtUtils.readBlockPos(entry, "pos").ifPresent(pos -> data.togglyfierMap.put(entry.getUUID("id"), pos));
		}

		return data;
	}
}
