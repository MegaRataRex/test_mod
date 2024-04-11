package net.megarata.xdmod.entity.client;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.megarata.xdmod.XdMod;
import net.megarata.xdmod.block.entity.MysteryBoxBlockEntity;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MysteryBoxModel extends GeoModel<MysteryBoxBlockEntity> {
	@Override
	public ResourceLocation getModelResource(MysteryBoxBlockEntity animatable) {
		return new ResourceLocation(XdMod.MODID, "geo/mystery_box.geo.json");
	}


	@Override
	public ResourceLocation getTextureResource(MysteryBoxBlockEntity animatable) {
		return new ResourceLocation(XdMod.MODID, "textures/block/mystery_box.png");
	}

	@Override
	public ResourceLocation getAnimationResource(MysteryBoxBlockEntity animatable) {
		return new ResourceLocation(XdMod.MODID, "animations/mystery_box.animation.json");
	}

}