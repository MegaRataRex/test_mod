package net.megarata.xdmod.client;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class SlideBehaviour {
    private int timeSinceLastSlide = 0;
    private boolean isSliding = false;
    private final int SLIDE_DURATION = 10;
    public int timeSinceLastSprint = 0;
    private int slideCooldown = 5;

    public SlideBehaviour() {
    }

    public boolean isSliding() {
        return isSliding;
    }

    public int getSlideCooldown() {
        return slideCooldown;
    }

    public boolean isSlideAvailable(Player player) {
        return !isSliding
                && player.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0;
    }

    public void onSlide() {
        isSliding = true;
        timeSinceLastSlide = 0;
    }

    public void sprintTick(){
        timeSinceLastSprint = 0;
    }

    public void tick() {
        timeSinceLastSprint += 1;
        slideCooldown -= 1;
        if (isSliding) {
            timeSinceLastSlide += 1;

            if (timeSinceLastSlide >= SLIDE_DURATION) {
                isSliding = false;
                timeSinceLastSlide = 0;
                slideCooldown = 5;
            }
        }
    }
}