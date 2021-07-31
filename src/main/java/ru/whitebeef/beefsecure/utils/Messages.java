package ru.whitebeef.beefsecure.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.whitebeef.beefsecure.BeefSecure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messages {

    private static String PREFIX = "префикс";
    private static String RELOAD_SUCCESSFUL_MESSAGE = "Сообщение 1";
    private static String RELOAD_UNSUCCESSFUL_MESSAGE = "Сообщение 2";
    private static String NOT_ENOUGH_ARGUMENTS = "Сообщение 3";

    private static String ACTIONBAR_MESSAGE = "экшнбар";
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");
    private static final List<String> versions = Arrays.asList("1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5");

    public static void registerMessages() {
        FileConfiguration config = BeefSecure.getInstance().getConfig();
        PREFIX = config.getString("Messages.PREFIX");
        RELOAD_SUCCESSFUL_MESSAGE = config.getString("Messages.RELOAD_SUCCESSFUL_MESSAGE");
        RELOAD_UNSUCCESSFUL_MESSAGE = config.getString("Messages.RELOAD_UNSUCCESSFUL_MESSAGE");
        NOT_ENOUGH_ARGUMENTS = config.getString("Messages.NOT_ENOUGH_ARGUMENTS");

        ACTIONBAR_MESSAGE = config.getString("ActionBarMessage.Message");
    }

    public static String createNotEnoughArgumentsMessage() {
        return colorize(getPrefix() + NOT_ENOUGH_ARGUMENTS);
    }

    public static String createReloadSuccessfulMessage() {
        return colorize(getPrefix() + RELOAD_SUCCESSFUL_MESSAGE);
    }

    public static String createReloadUnsuccessfulMessage() {
        return colorize(getPrefix() + RELOAD_UNSUCCESSFUL_MESSAGE);
    }

    public static String createActionBarMessage() {
        return colorize(ACTIONBAR_MESSAGE);
    }

    public static void sendActionBarMessage(Player player) {
        if (BeefSecure.isActionBarEnable())
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(createActionBarMessage()));
    }

    public static String getPrefix() {
        return PREFIX;
    }


    public static String colorize(String message) {
        String parsed = message;
        if (versions.contains(Bukkit.getServer().getVersion().split(" ")[2].replace(")", ""))) {
            Matcher matcher = HEX_PATTERN.matcher(parsed);
            while (matcher.find()) {
                String color = parsed.substring(matcher.start(), matcher.end());
                parsed = parsed.replace(color, ChatColor.of(color) + "");
                matcher = HEX_PATTERN.matcher(parsed);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', parsed);
    }


    public static ArrayList<String> colorize(List<String> text) {
        ArrayList<String> retList = new ArrayList<>();
        text.forEach(str -> retList.add(colorize(str)));
        return retList;
    }

}
