package minealex.tdropinventory.commands;

import minealex.tdropinventory.TDropInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Commands implements CommandExecutor {

    private final TDropInventory plugin;

    public Commands(TDropInventory plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tdi.reload")) {
            String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission"));
            sender.sendMessage(noPermissionMessage);
            return true;
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        config.options().copyDefaults(true);
        plugin.saveConfig();

        String reloadMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.reload"));
        sender.sendMessage(reloadMessage);

        return true;
    }
}
