package minealex.tdropinventory;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import minealex.tdropinventory.commands.Commands;

import java.util.List;

public class TDropInventory extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private List<String> allowedWorlds;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        allowedWorlds = config.getStringList("worlds");
        getCommand("tdi").setExecutor(new Commands(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String worldName = event.getBlock().getWorld().getName();

        if (!allowedWorlds.contains(worldName)) {
            return; // No proceder si el mundo no está permitido
        }

        Material blockType = event.getBlock().getType();
        String dropTypeStr = config.getString("drops." + blockType.name().toLowerCase()); // Convertir a minúsculas

        if (dropTypeStr != null) {
            Material dropType = Material.matchMaterial(dropTypeStr.toUpperCase()); // Utiliza Material.matchMaterial para obtener el Material

            if (dropType != null) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getPlayer().getInventory().addItem(new ItemStack(dropType));
            } else {
                getLogger().warning("Material " + dropTypeStr + " no encontrado. Asegúrate de que sea válido.");
            }
        }
    }
}
