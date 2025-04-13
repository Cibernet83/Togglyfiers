package com.sarahk.togglyfiers.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record TogglyOwnerComponent(ResourceKey<Level> levelKey, UUID id) {

	public static final Codec<TogglyOwnerComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(TogglyOwnerComponent::levelKey),
			UUIDUtil.CODEC.fieldOf("id").forGetter(TogglyOwnerComponent::id)
	).apply(instance, TogglyOwnerComponent::new));

	public static final StreamCodec<FriendlyByteBuf, TogglyOwnerComponent> STREAM_CODEC = StreamCodec.composite(
			ResourceKey.streamCodec(Registries.DIMENSION), TogglyOwnerComponent::levelKey,
			UUIDUtil.STREAM_CODEC, TogglyOwnerComponent::id,
			TogglyOwnerComponent::new
	);

}
