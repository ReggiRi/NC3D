package com.nc3d.model;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.packs.PackResources;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigPackRegistrar {
    private static final Logger LOGGER = LoggerFactory.getLogger("NC3D/ConfigPack");

    public static void register(Path configDir) {
        Minecraft mc = Minecraft.getInstance();
        PackRepositoryAccessor repo = new PackRepositoryAccessor(mc.getResourcePackRepository());
        repo.addSource(new NC3DConfigSource(configDir));
    }

    private record NC3DConfigSource(Path configDir) implements RepositorySource {
        @Override
        public void loadPacks(java.util.function.Consumer<Pack> consumer) {
            PackLocationInfo info = new PackLocationInfo(
                    "nc3d:config_models",
                    Component.literal("NC3D Models"),
                    PackSource.DEFAULT,
                    Optional.empty());

            PackSelectionConfig selection = new PackSelectionConfig(
                    true, Pack.Position.TOP, false);

            Pack.ResourcesSupplier factory = new Pack.ResourcesSupplier() {
                @Override
                public PackResources openPrimary(PackLocationInfo loc) {
                    return new ConfigPackResources(loc, configDir);
                }

                @Override
                public PackResources openFull(
                        PackLocationInfo loc, Pack.Metadata metadata) {
                    return new ConfigPackResources(loc, configDir);
                }
            };

            Pack pack = Pack.readMetaAndCreate(
                    info, factory, PackType.CLIENT_RESOURCES, selection);

            if (pack != null) {
                consumer.accept(pack);
            }
        }
    }

    private static class PackRepositoryAccessor {
        private static final Field SOURCES_FIELD = findSourcesField();

        private final Object repository;

        PackRepositoryAccessor(Object repository) {
            this.repository = repository;
        }

        void addSource(RepositorySource source) {
            try {
                @SuppressWarnings("unchecked")
                Set<RepositorySource> sources = (Set<RepositorySource>) SOURCES_FIELD.get(repository);
                sources.add(source);
                LOGGER.info("Registered NC3D config resource pack");
            } catch (Exception e) {
                LOGGER.error("Failed to register NC3D config pack", e);
            }
        }

        private static Field findSourcesField() {
            for (Field field : net.minecraft.server.packs.repository.PackRepository.class
                    .getDeclaredFields()) {
                if (Set.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            }
            throw new RuntimeException("Could not find sources field in PackRepository");
        }
    }

    private ConfigPackRegistrar() {}
}
