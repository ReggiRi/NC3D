package com.nc3d.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelRegistry {
    private final Map<String, ModelData> models = new HashMap<>();

    public void register(ModelData data) {
        models.put(data.modelId(), data);
    }

    public Optional<ModelData> get(String modelId) {
        return Optional.ofNullable(models.get(modelId));
    }

    public boolean contains(String modelId) {
        return models.containsKey(modelId);
    }

    public Map<String, ModelData> getAll() {
        return Map.copyOf(models);
    }
}
