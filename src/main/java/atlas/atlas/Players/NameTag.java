package atlas.atlas.Players;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Regions.Settlement;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class NameTag {

    PacketContainer packet;
    InternalStructure structure;
    Player player;
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    public NameTag(Player player) {
        this.player = player;
        packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        packet.getIntegers().write(0, 0);
        packet.getStrings().write(0, name);
        packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(player.getName()));
        structure = packet.getOptionalStructures().readSafely(0).get();
        structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, ChatColor.WHITE);
    }

    public NameTag setPrefix(String prefix) {
        structure.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', prefix)));
        return this;
    }

    public NameTag color(ChatColor color) {
        structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                .write(0, color);
        return this;
    }

    public NameTag setSuffix(String suffix) {
        structure.getChatComponents().write(2, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', suffix)));
        return this;
    }

    public void update(Collection<? extends Player> players) {
        if (players.isEmpty()) return;
        structure.getIntegers().write(0, 3);
        packet.getOptionalStructures().write(0, Optional.of(structure));
        for (Player p : players) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            } catch (Exception e) {
                throw new RuntimeException("Cannot send packet " + packet, e);
            }
        }
    }

    public void update() {
        this.update(Bukkit.getOnlinePlayers());
    }

    public void nameTagUpdater(Player p) {
        NameTag nametag = new NameTag(p);
        new BukkitRunnable() {
            @Override
            public void run () {
                if (!Bukkit.getOnlinePlayers().contains(p)) {
                    this.cancel();
                }
                String prefix;
                Settlement settlement = settlementManager.getSettlement(p);
                if (settlement != null) {
                    prefix = "§f[§r" + settlement.getName() + "§f]§r ";
                } else {
                    prefix = "";
                }
                nametag.setPrefix(prefix);
                nametag.update();
            }
        }.runTaskTimer(Atlas.getInstance(), 0L,20L);
    }

}
