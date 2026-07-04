package com.nc3d.model;

import com.google.gson.JsonObject;
import com.nc3d.NC3DConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;

public class ConfigPackResources implements PackResources {
    private static final byte[] PACK_MCMETA = (
            "{\"pack\":{\"pack_format\":15,\"description\":\"NC3D config models\"}}"
    ).getBytes(StandardCharsets.UTF_8);

    private final PackLocationInfo location;
    private final Path modelsDir;
    private final Map<String, Path> assetMap = new HashMap<>();
    private final Set<String> namespaces = new HashSet<>();

    public ConfigPackResources(PackLocationInfo location, Path configDir) {
        this.location = location;
        this.modelsDir = configDir.resolve("nc3d/models");
        this.namespaces.add(NC3DConstants.MOD_ID);
        scanForModels();
    }

    private void scanForModels() {
        if (!Files.isDirectory(modelsDir)) return;
        try (Stream<Path> dirs = Files.list(modelsDir)) {
            dirs.filter(Files::isDirectory).forEach(this::scanModelDir);
        } catch (IOException e) {
            // no models yet
        }
    }

    private void scanModelDir(Path dir) {
        String modelId = dir.getFileName().toString().toLowerCase(Locale.ROOT);
        try (Stream<Path> files = Files.list(dir)) {
            files.forEach(file -> {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(".geo.json")) {
                    assetMap.put("geo/" + modelId + ".geo.json", file);
                } else if (name.endsWith(".png")) {
                    assetMap.put("textures/" + modelId + ".png", file);
                } else if (name.endsWith(".animation.json")) {
                    assetMap.put("animations/" + modelId + ".animation.json", file);
                }
            });
        } catch (IOException e) {
            // ignore
        }
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... paths) {
        if (paths.length == 1 && "pack.mcmeta".equals(paths[0])) {
            return () -> new ByteArrayInputStream(PACK_MCMETA);
        }
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type != PackType.CLIENT_RESOURCES) return null;
        if (!NC3DConstants.MOD_ID.equals(location.getNamespace())) return null;
        Path file = assetMap.get(location.getPath());
        if (file != null && Files.isRegularFile(file)) {
            return IoSupplier.create(file);
        }
        return null;
    }

    @Override
    public void listResources(
            PackType type, String namespace, String path, ResourceOutput output) {
        if (type != PackType.CLIENT_RESOURCES) return;
        if (!NC3DConstants.MOD_ID.equals(namespace)) return;
        String prefix = path.isEmpty() ? "" : path + "/";
        for (Map.Entry<String, Path> entry : assetMap.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                ResourceLocation id = ResourceLocation.parse(
                        NC3DConstants.MOD_ID + ":" + entry.getKey());
                output.accept(id, IoSupplier.create(entry.getValue()));
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        if (type == PackType.CLIENT_RESOURCES) {
            return Collections.unmodifiableSet(namespaces);
        }
        return Collections.emptySet();
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) throws IOException {
        if ("pack".equals(serializer.getMetadataSectionName())) {
            JsonObject packObj = new JsonObject();
            packObj.addProperty("pack_format", 15);
            packObj.addProperty("description", "NC3D Models");
            return serializer.fromJson(packObj);
        }
        return null;
    }

    @Override
    public PackLocationInfo location() {
        return location;
    }

    @Override
    public void close() {}
}
