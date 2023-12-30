package atlas.atlas.Handlers;

import atlas.atlas.Atlas;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MobSpawnerHandler implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.SPAWNER) {
            if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
                ItemStack spawnerItem = createSpawnerItem(block);

                block.getWorld().dropItemNaturally(block.getLocation(), spawnerItem);

                event.setDropItems(false);
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if (item.getType() == Material.SPAWNER) {
            EntityType spawnerType = getSpawnerType(item);

            if (spawnerType != null) {
                CreatureSpawner spawner = (CreatureSpawner) event.getBlockPlaced().getState();
                spawner.setSpawnedType(spawnerType);
                spawner.update();
            }
        }
    }

    private ItemStack createSpawnerItem(Block spawnerBlock) {
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        EntityType spawnerType = ((CreatureSpawner) spawnerBlock.getState()).getSpawnedType();
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();
        spawnerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        List<String> lore = new ArrayList<>();
        if (spawnerType != null) {
            lore.add("§6" + spawnerType.name());
        }
        spawnerMeta.setLore(lore);
        spawnerItem.setItemMeta(spawnerMeta);
        if (spawnerType != null) {
            setSpawnerType(spawnerItem, spawnerType);
        }
        return spawnerItem;
    }

    private static final NamespacedKey SPAWNER_TYPE_KEY = new NamespacedKey(Atlas.getInstance(), "spawner_type");
    public static void setSpawnerType(ItemStack spawnerItem, EntityType spawnerType) {
        ItemMeta meta = spawnerItem.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(SPAWNER_TYPE_KEY, PersistentDataType.STRING, spawnerType.name());
            spawnerItem.setItemMeta(meta);
        }
    }

    public static EntityType getSpawnerType(ItemStack spawnerItem) {
        ItemMeta meta = spawnerItem.getItemMeta();
        if (meta != null) {
            String spawnerTypeName = meta.getPersistentDataContainer().get(SPAWNER_TYPE_KEY, PersistentDataType.STRING);
            if (spawnerTypeName != null) {
                return EntityType.valueOf(spawnerTypeName);
            }
        }
        return null;
    }
}
