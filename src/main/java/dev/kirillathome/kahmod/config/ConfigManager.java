package dev.kirillathome.kahmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import dev.kirillathome.kahmod.Kahmod;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String configFilePath = "config/kahmod";
    private static final String serverConfigName = "server.json";
    public static ServerConfig serverConfig;
    public static void init(){
        Kahmod.LOGGER.info("Initialising ConfigManager");
        if (!getServerConfigPath().toFile().exists()){
            saveServerConfig(new ServerConfig());
            Kahmod.LOGGER.info("Server config doesn't exist, creating new!");
        }
        serverConfig = loadServerConfig();
        serverConfig.verifyConfig();
        saveServerConfig(serverConfig);
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }
    public static void saveServerConfig(ServerConfig config){
        try (FileWriter writer = new FileWriter(getServerConfigPath().toString())){
            gson.toJson(config, writer);
            Kahmod.LOGGER.info("Successfully server saved config!");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ServerConfig loadServerConfig(){
        try (FileReader reader = new FileReader(getServerConfigPath().toString())){
            return gson.fromJson(reader, ServerConfig.class);
        } catch (JsonSyntaxException | JsonIOException | IOException e){
            e.printStackTrace();
            return new ServerConfig();
        }
    }

    public static void reloadServerConfig(){
        ServerConfig new_config = loadServerConfig();
        new_config.verifyConfig();
        //Kahmod.LOGGER.info("Old config motdList: %s".formatted(serverConfig.motdList));
        //Kahmod.LOGGER.info("New config motdList: %s".formatted(new_config.motdList));
        serverConfig = new_config;
        saveServerConfig(serverConfig);
        //Kahmod.LOGGER.info("This config motdList: %s".formatted(serverConfig.motdList));
    }

    /*public static boolean isServerConfigValid(ServerConfig config){
        Kahmod.LOGGER.info(config.toString().concat(" is valid"));
        return true;
    }*/

    private static Path getServerConfigPath(){
        Path path = Paths.get(System.getProperty("user.dir"), configFilePath);
        if (!path.toFile().exists()){
            boolean b = path.toFile().mkdirs();
            Kahmod.LOGGER.info("Successfully made directories: ".concat(String.valueOf(b)));
        }
        return path.resolve(serverConfigName);
    }
}
