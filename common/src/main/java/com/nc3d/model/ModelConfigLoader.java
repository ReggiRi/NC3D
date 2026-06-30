package com.nc3d.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nc3d.NC3DMod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModelConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("NC3D/ModelConfig");
    private static final Gson GSON = new Gson();

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

        try (Stream<Path> files = Files.list(modelsDir)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                if (!file.toString().endsWith(".json")) continue;
                try {
                    String content = Files.readString(file);
                    JsonObject json = GSON.fromJson(content, JsonObject.class);
                    ModelData data = parseModelConfig(json);
                    if (data != null) {
                        registry.register(data);
                        loaded++;
                        LOGGER.info("Loaded model config: {}", data.modelId());
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to load model config: {} - {}", file.getFileName(), e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to list models directory: {}", e.getMessage());
        }

        LOGGER.info("Loaded {} model(s) from config", loaded);
    }

    private static ModelData parseModelConfig(JsonObject json) {
        String modelId = getString(json, "model_id");
        if (modelId == null || modelId.isBlank()) {
            LOGGER.warn("Missing or empty 'model_id' in model config");
            return null;
        }

        String modelPath = getString(json, "model_path");
        if (modelPath == null) {
            LOGGER.warn("Missing 'model_path' in model config: {}", modelId);
            return null;
        }

        String texturePath = getString(json, "texture_path");
        if (texturePath == null) {
            LOGGER.warn("Missing 'texture_path' in model config: {}", modelId);
            return null;
        }

        ResourceLocation modelRL = ResourceLocation.parse(modelPath);
        ResourceLocation textureRL = ResourceLocation.parse(texturePath);

        ModelData.Builder builder = ModelData.builder(modelId, modelRL, textureRL);

        String animPath = getString(json, "animation_path");
        if (animPath != null) {
            builder.animationPath(ResourceLocation.parse(animPath));
        }

        if (json.has("scale")) {
            builder.scale(json.get("scale").getAsFloat());
        }

        float ox = getFloat(json, "offset_x", 0.0f);
        float oy = getFloat(json, "offset_y", 0.0f);
        float oz = getFloat(json, "offset_z", 0.0f);
        if (ox != 0.0f || oy != 0.0f || oz != 0.0f) {
            builder.offset(ox, oy, oz);
        }

        return builder.build();
    }

    private static String getString(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : null;
    }

    private static float getFloat(JsonObject json, String key, float fallback) {
        return json.has(key) ? json.get(key).getAsFloat() : fallback;
    }

    private ModelConfigLoader() {}
}
