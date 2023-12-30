package atlas.atlas.Commands;

import atlas.atlas.Commands.SubCommands.Settlement.*;
import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SettlementCommandHandler implements CommandExecutor {

    public HashMap<String, SubCommand> commands;

    public SettlementCommandHandler() {
        this.commands = new HashMap<>();
        registerCommands();
    }

    public void registerCommands() {
        commands.put("disband", new SettlementDisbandCommand());
        commands.put("claim", new SettlementClaimCommand());
        commands.put("sethome", new SettlementSetHomeCommand());
        commands.put("home", new SettlementHomeCommand());
        commands.put("rename", new SettlementRenameCommand());
        commands.put("invite", new SettlementInviteCommand());
        commands.put("accept", new SettlementAcceptCommand());
        commands.put("leave", new SettlementLeaveCommand());
        commands.put("reject", new SettlementRejectCommand());
        commands.put("kick", new SettlementKickCommand());
        commands.put("list", new SettlementListCommand());
        commands.put("setleader", new SettlementSetLeaderCommand());
        commands.put("info", new SettlementInfoCommand());
        commands.put("donate", new SettlementDonateCommand());
        commands.put("resize", new SettlementResizeCommand());
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if (strings.length == 0) {
            p.sendMessage("Please use any of the subcommands.");
        }
        if (strings.length > 0) {
            for (String str : commands.keySet())
                if (str.equalsIgnoreCase(strings[0])) {
                    commands.get(str).execute(p, strings);
                }
        }
        return true;
    }
}
