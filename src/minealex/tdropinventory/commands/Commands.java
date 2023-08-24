package minealex.tdropinventory.commands;

import minealex.tdropinventory.TDropInventory;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Commands implements CommandExecutor {

    private final TDropInventory plugin;

    public Commands(TDropInventory plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                return handleReloadCommand(sender);
            } else if (args[0].equalsIgnoreCase("version")) {
                return handleVersionCommand(sender);
            }
        }

        // Si se proporcionan argumentos no válidos o ninguno, muestra un mensaje de ayuda o error.
        sender.sendMessage(ChatColor.RED + "Invalid command. Correct usage: /tdi reload or /tdi version");
        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
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

    private boolean handleVersionCommand(CommandSender sender) {
        if (!sender.hasPermission("tdi.version")) {
            String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission"));
            sender.sendMessage(noPermissionMessage);
            return true;
        }

        String versionMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.version"));
        versionMessage = versionMessage.replace("%version%", plugin.getDescription().getVersion());

        sender.sendMessage(versionMessage);
        return true;
    }
}
