package com.sg.dminus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("durability_minus.json");

    private static DegradeConfig config;

    public static void load() {

        if (Files.exists(CONFIG_PATH)) {

            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {

                config = GSON.fromJson(reader, DegradeConfig.class);

            } catch (Exception e) {

                e.printStackTrace();

                config = new DegradeConfig();
                save();
            }

        } else {

            config = new DegradeConfig();
            save();
        }
    }

    public static void save() {

        try {

            Files.createDirectories(CONFIG_PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {

                GSON.toJson(config, writer);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static DegradeConfig get() {
        return config;
    }
}
