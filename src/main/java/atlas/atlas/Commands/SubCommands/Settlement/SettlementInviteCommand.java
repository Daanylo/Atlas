package atlas.atlas.Commands.SubCommands.Settlement;

import atlas.atlas.Atlas;
import atlas.atlas.Managers.SettlementManager;
import atlas.atlas.Commands.SubCommands.SubCommand;
import atlas.atlas.Regions.Settlement;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class SettlementInviteCommand extends SubCommand {
    SettlementManager settlementManager = Atlas.getInstance().getSettlementManager();

    @Override
    public void execute(Player p, String[] args) {
        if (settlementManager.getSettlement(p) == null) {
            p.sendMessage("§cYou are not a member of a region.");
            return;
        }
        Settlement settlement = settlementManager.getSettlement(p);
        String settlementID = settlementManager.getSettlementID(settlement);
        if (!settlementManager.invites.containsKey(settlementID)) {
            settlementManager.invites.put(settlementID, new ArrayList<>());
        }
        if (!settlement.getLeader().equals(p.getUniqueId())) {
            p.sendMessage("§cYou are not allowed to invite members to your settlement.");
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§cPlease use §7§o/settlement invite (name)§c.");
            return;
        }
        if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
            p.sendMessage("§cPlayer §f\"" + args[1] + "\" §cis not online.");
            return;
        }
        Player invitee = Bukkit.getPlayer(args[1]);
        if (Objects.equals(settlementManager.getSettlement(invitee), settlement)) {
            p.sendMessage("§cThat player is already a member of your settlement.");
            return;
        }
        if (settlementManager.getSettlement(invitee) != null && !Objects.equals(settlementManager.getSettlement(invitee), settlement)) {
            p.sendMessage("§cThat player is already a member of a settlement.");
            return;
        }
        if (settlementManager.invites.get(settlementID).contains(invitee.getUniqueId())) {
            p.sendMessage("§cThat player was already invited.");
            return;
        }
        if (settlement.getMembers().size() > 9) {
            p.sendMessage("§cYour settlement is full.");
            return;
        }
        sendInvite(invitee, settlement);
        p.sendMessage(invitee.getName() + " §awas invited to join your settlement.");
    }

    public void sendInvite(Player player, Settlement settlement) {
        String settlementID = settlementManager.getSettlementID(settlement);
        TextComponent accept = new TextComponent(ChatColor.GREEN + ChatColor.BOLD.toString() + "Accept ");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement accept " + settlement.getName()));
        TextComponent reject = new TextComponent(ChatColor.RED + ChatColor.BOLD.toString() + "Reject");
        reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settlement reject " + settlement.getName()));
        player.sendMessage("§eYou have been invited to join " + settlement.getName() + "§e. ");
        player.spigot().sendMessage(accept, reject);

        settlementManager.invites.get(settlementID).add(player.getUniqueId());

        new BukkitRunnable() {
            final int time = 6000;
            int timeLeft = time;
            @Override
            public void run() {
                timeLeft = timeLeft - 20;
                if (timeLeft <= 0) {
                    this.cancel();
                    settlementManager.invites.get(settlementID).remove(player.getUniqueId());
                    player.sendMessage("§eThe invitation for " + settlement.getName() + " §ehas expired.");
                    Bukkit.getPlayer(settlement.getLeader()).sendMessage("§eThe invitation for §f" + player.getName() + " §ehas expired.");
                }
                if (!settlementManager.invites.get(settlementID).contains(player.getUniqueId())) {
                    this.cancel();
                }
                if (settlementManager.getSettlement(player) != null) {
                    this.cancel();
                    settlementManager.invites.get(settlementID).remove(player.getUniqueId());
                }
            }
        }.runTaskTimer(Atlas.getInstance(), 0L, 20L);
    }

    @Override
    public void help(Player p) {

    }

    @Override
    public String permission() {
        return null;
    }
}
