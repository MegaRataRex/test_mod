package net.megarata.xdmod.client.anim;

import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationSerializing;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationRegistry {
    static final Logger LOGGER = LogManager.getLogger();
    public static Map<String, KeyframeAnimation> animations = new HashMap<>();

    public static void load(ResourceManager resourceManager) {
        var dataFolder = "player_animation";
        for (var entry : resourceManager.listResources(dataFolder, fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                List<KeyframeAnimation> readAnimations = AnimationSerializing.deserializeAnimation(resource.open());
                KeyframeAnimation animation = readAnimations.get(0);

                var id = identifier
                        .toString()
                        .replace(dataFolder + "/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                AnimationRegistry.animations.put(id, animation);
                LOGGER.debug(id);
            } catch (Exception e) {
                LOGGER.error("Failed to load animation " + identifier.toString());
                e.printStackTrace();
            }
        }
    }
}