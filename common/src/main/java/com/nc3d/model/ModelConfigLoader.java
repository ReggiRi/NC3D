package com.nc3d.model;

import com.nc3d.NC3DMod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModelConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("NC3D/ModelConfig");

    public static void loadFromDirectory(Path configDir) {
        Path modelsDir = configDir.resolve("nc3d/models");
        if (!Files.exists(modelsDir)) {
            try {
                Files.createDirectories(modelsDir);
            } catch (IOException e) {
                LOGGER.warn("Could not create models directory: {}", modelsDir);
            }
            LOGGER.info("Created empty models directory: {}", modelsDir);
            return;
        }
        if (!Files.isDirectory(modelsDir)) {
            LOGGER.warn("Not a directory: {}", modelsDir);
            return;
        }

        ModelRegistry registry = NC3DMod.getInstance().getModelRegistry();
        int loaded = 0;

        try (Stream<Path> dirs = Files.list(modelsDir)) {
            for (Path dir : (Iterable<Path>) dirs::iterator) {
                if (!Files.isDirectory(dir)) continue;
                String modelId = dir.getFileName().toString().toLowerCase(Locale.ROOT);
                if (modelId.startsWith(".")) continue;

                ModelData data = scanModelDir(dir, modelId);
                if (data != null) {
                    registry.register(data);
                    loaded++;
                    LOGGER.info("Discovered model: {}", modelId);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to list models directory: {}", e.getMessage());
        }

        LOGGER.info("Discovered {} model(s) from config", loaded);
    }

    private static ModelData scanModelDir(Path dir, String modelId) {
        String geoFile = null;
        String textureFile = null;
        String animFile = null;

        try (Stream<Path> files = Files.list(dir)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(".geo.json") && geoFile == null) {
                    geoFile = modelId;
                } else if (name.endsWith(".png") && textureFile == null) {
                    textureFile = modelId;
                } else if (name.endsWith(".animation.json") && animFile == null) {
                    animFile = modelId;
                }
            }
        } catch (IOException e) {
            return null;
        }

        if (geoFile == null || textureFile == null) {
            LOGGER.warn("Model '{}' missing required .geo.json or .png file", modelId);
            return null;
        }

        ResourceLocation modelRL = ResourceLocation.parse("nc3d:geo/" + modelId + ".geo.json");
        ResourceLocation textureRL = ResourceLocation.parse("nc3d:textures/" + modelId + ".png");

        ModelData.Builder builder = ModelData.builder(modelId, modelRL, textureRL);

        if (animFile != null) {
            builder.animationPath(ResourceLocation.parse("nc3d:animations/" + modelId + ".animation.json"));
        }

        return builder.build();
    }

    private ModelConfigLoader() {}
}
