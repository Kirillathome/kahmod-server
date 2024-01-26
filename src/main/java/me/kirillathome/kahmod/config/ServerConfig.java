package me.kirillathome.kahmod.config;

import me.kirillathome.kahmod.KahMod;
import net.minecraft.text.Text;

import java.util.*;

public class ServerConfig {

    // Whether the "anticheat" should be enabled
    public boolean anticheat = true;

    // The brand to display on the debug screen
    public String customBrand = "funni";

    // The Server MOTD (since it gets overwritten); use %s to set the position of the random MOTD
    public String motd = "§kaa§9 Rentner§c Server§6 Season 2§r§k aa§r \n§kaa§r %s §r§7§kaa";

    // A random String gets picked from this Array
    public List<String> motdList = new ArrayList<>();

    // The responses to the custom commands
    public Map<String, String> command_responses = new HashMap<>();

    public String getCommandResponse(String key){
        String response = command_responses.get(key);
        return Objects.requireNonNullElse(response, "Error in server config file; couldn't get command response!");
    }

    public void verifyConfig(){
        Map<String, String> map = generateCommandResponses();
        map.forEach((key, value) -> {
            if (!command_responses.containsKey(key)){
                command_responses.put(key, value);
            }
        });
        //KahMod.LOGGER.info("motdList size: %d".formatted(motdList.size()));
    }

    public Text getRandomMotd(){
        Random random = new Random();
        String s;
        if (!motdList.isEmpty()){
            s = motd.formatted(motdList.get(random.nextInt(motdList.size())));
        } else {
            s = motd;
        }
        return Text.literal(s);
    }
    public Map<String, String> generateCommandResponses(){
        Map<String, String> map = new HashMap<>();
        map.put("rentner_smp", "Der RentnerSMP von Kirill.\nMod version: %s");
        map.put("cmd.invalid_item", "Can't add NBT to air!");
        map.put("cmd.set", "Successfully set the CustomModelData of your %s to %d!");
        map.put("cmd.reset", "Successfully reset the CustomModelData of your %s!");
        map.put("blacklist.set.added", "Successfully blacklisted %s from using %s!");
        map.put("blacklist.set.removed", "Successfully allowed %s to use %s!");
        map.put("blacklist.set.already_added", "%s is already blacklisted from using %s!");
        map.put("blacklist.set.already_removed", "%s already can use %s!");
        map.put("blacklist.get.true", "%s is blacklisted from using %s!");
        map.put("blacklist.get.false", "%s is not blacklisted from using %s!");
        map.put("blacklist.get.full", "%s is blacklisted from using: %s");
        map.put("blacklist.get.full.fail", "%s is not blacklisted from using any commands.");
        map.put("status.set", "Successfully set your status to: ");
        map.put("status.reset", "Successfully reset your status.");
        map.put("status.long", "Status is too long!");
        map.put("config.reload", "Successfully reloaded the config!");
        map.put("feedback.console", "Can't execute this command from the console!");
        map.put("feedback.forbidden", "You are not allowed to use this command!");
        return map;
    }
}