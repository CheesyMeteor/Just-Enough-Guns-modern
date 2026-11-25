package ttv.migami.jeg.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import ttv.migami.jeg.JustEnoughGuns;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.NetworkGunManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ConfigPackLoader {
    private static final String PACK_ID = "jeg_cfg_config_resources";
    private static final Path ROOT = NetworkGunManager.CONFIG_PACK_DIR;

    // PORT-VERIFY: 1.21.1 Pack.ResourcesSupplier has openPrimary/openFull (no lambda) and vanilla PathPackResources(PackLocationInfo, Path)
    private static final Pack.ResourcesSupplier CONFIG_RESOURCES = new Pack.ResourcesSupplier() {
        @Override
        public PackResources openPrimary(PackLocationInfo location) {
            return new PathPackResources(location, ROOT);
        }

        @Override
        public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
            return new PathPackResources(location, ROOT);
        }
    };

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent e) {
        if (e.getPackType() != PackType.CLIENT_RESOURCES) return;

        JustEnoughGuns.LOGGER.error("Trying to register a pack with the ID {} in the Root {}", PACK_ID, ROOT);

        ensurePackMcmeta();

        // PORT-VERIFY: Pack.readMetaAndCreate(PackLocationInfo, ResourcesSupplier, PackType, PackSelectionConfig)
        PackLocationInfo info = new PackLocationInfo(PACK_ID, Component.literal("JEG: Data-Driven Guns!"), PackSource.BUILT_IN, Optional.empty());
        Pack pack = Pack.readMetaAndCreate(
                info,
                CONFIG_RESOURCES,
                PackType.CLIENT_RESOURCES,
                new net.minecraft.server.packs.PackSelectionConfig(/*alwaysEnabled=*/true, Pack.Position.TOP, false)
        );

        if (pack != null) {
            e.addRepositorySource(finder -> finder.accept(pack));
            JustEnoughGuns.LOGGER.error("Registered a Resource Pack for {}", PACK_ID);
        } else JustEnoughGuns.LOGGER.error("Could not register the Resource Pack for {}", PACK_ID);
    }

    private static void ensurePackMcmeta() {
        Path mcmeta = ROOT.resolve("pack.mcmeta");

        if (Files.notExists(mcmeta)) {
            try {
                Files.createDirectories(ROOT);
                String json = """
                {
                  "pack": {
                    "pack_format": 34,
                    "description": "Assets for the new Data-Driven Guns!"
                  }
                }
                """;
                Files.writeString(mcmeta, json);
                JustEnoughGuns.LOGGER.info("Generated default pack.mcmeta for {}", PACK_ID);
            } catch (IOException e) {
                JustEnoughGuns.LOGGER.error("Failed to create pack.mcmeta for {}", PACK_ID, e);
            }
        }
    }

    public static void exportSampleResourcesIfMissing() {
        Path exportRoot = NetworkGunManager.CONFIG_PACK_DIR;

        Map<String, String> filesToCopy = Map.of(
                "assets/jeg/geo/vindicator_smg.geo.json", "assets/jeg/samples/geo/vindicator_smg.geo.json",
                "assets/jeg/textures/vindicator_smg.png", "assets/jeg/samples/textures/vindicator_smg.png",
                "assets/jeg/animations/vindicator_smg.animation.json", "assets/jeg/samples/animations/vindicator_smg.animation.json",

                "assets/jeg/geo/primitive_blowpipe.geo.json", "assets/jeg/samples/geo/primitive_blowpipe.geo.json",
                "assets/jeg/textures/primitive_blowpipe.png", "assets/jeg/samples/textures/primitive_blowpipe.png",
                "assets/jeg/animations/primitive_blowpipe.animation.json", "assets/jeg/samples/animations/primitive_blowpipe.animation.json",

                "assets/jeg/geo/fire_sweeper.geo.json", "assets/jeg/samples/geo/fire_sweeper.geo.json",
                "assets/jeg/textures/fire_sweeper.png", "assets/jeg/samples/textures/fire_sweeper.png",
                "assets/jeg/animations/fire_sweeper.animation.json", "assets/jeg/samples/animations/fire_sweeper.animation.json",

                "assets/jeg/lang/en_us.json", "assets/jeg/samples/lang/en_us.json"
        );

        for (var entry : filesToCopy.entrySet()) {
            Path destPath = exportRoot.resolve(entry.getKey());
            String internalPath = entry.getValue();

            if (!Files.exists(destPath)) {
                try {
                    Files.createDirectories(destPath.getParent());

                    try (InputStream in = ConfigPackLoader.class.getClassLoader().getResourceAsStream(internalPath)) {
                        if (in == null) {
                            JustEnoughGuns.LOGGER.error("Missing resource in JAR: {}", internalPath);
                            continue;
                        }

                        Files.copy(in, destPath);
                        JustEnoughGuns.LOGGER.info("Exported sample file to {}", destPath);
                    }
                } catch (IOException e) {
                    JustEnoughGuns.LOGGER.error("Failed to export sample file to {}", destPath, e);
                }
            }
        }

        Map<String, String> filesToCopy2 = Map.of(
                "assets/jeg/textures/fire_sweeper.png.mcmeta", "assets/jeg/samples/textures/fire_sweeper.png.mcmeta"
        );

        for (var entry : filesToCopy2.entrySet()) {
            Path destPath = exportRoot.resolve(entry.getKey());
            String internalPath = entry.getValue();

            if (!Files.exists(destPath)) {
                try {
                    Files.createDirectories(destPath.getParent());

                    try (InputStream in = ConfigPackLoader.class.getClassLoader().getResourceAsStream(internalPath)) {
                        if (in == null) {
                            JustEnoughGuns.LOGGER.error("Missing resource in JAR: {}", internalPath);
                            continue;
                        }

                        Files.copy(in, destPath);
                        JustEnoughGuns.LOGGER.info("Exported sample file to {}", destPath);
                    }
                } catch (IOException e) {
                    JustEnoughGuns.LOGGER.error("Failed to export sample file to {}", destPath, e);
                }
            }
        }
    }

    public static void exportSampleDataIfMissing() {
        Path exportRoot = NetworkGunManager.CONFIG_GUN_DIR.getParent().getParent();

        Map<String, String> filesToCopy = Map.of(
                "data/guns/vindicator_smg.json", "assets/jeg/samples/vindicator_smg.json",
                "data/guns/primitive_blowpipe.json", "assets/jeg/samples/primitive_blowpipe.json",
                "data/guns/fire_sweeper.json", "assets/jeg/samples/fire_sweeper.json"
        );

        for (var entry : filesToCopy.entrySet()) {
            Path destPath = exportRoot.resolve(entry.getKey());
            String internalPath = entry.getValue();

            if (!Files.exists(destPath)) {
                try {
                    Files.createDirectories(destPath.getParent());

                    try (InputStream in = ConfigPackLoader.class.getClassLoader().getResourceAsStream(internalPath)) {
                        if (in == null) {
                            JustEnoughGuns.LOGGER.error("Missing resource in JAR: {}", internalPath);
                            continue;
                        }

                        Files.copy(in, destPath);
                        JustEnoughGuns.LOGGER.info("Exported sample file to {}", destPath);
                    }
                } catch (IOException e) {
                    JustEnoughGuns.LOGGER.error("Failed to export sample file to {}", destPath, e);
                }
            }
        }
    }
}
