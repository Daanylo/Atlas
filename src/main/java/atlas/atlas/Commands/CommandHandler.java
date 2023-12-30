package atlas.atlas.Commands;

import atlas.atlas.Atlas;
import atlas.atlas.Commands.SubCommands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    private HashMap<String, SubCommand> commands;

    public CommandHandler() {
        this.commands = new HashMap<>();
        registerCommands();
    }

    public void registerCommands() {
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = null;
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCommands can only be executed by a player.");
            return true;
        }
        p = (Player) sender;
        if (args.length >= 1 && args[0].equalsIgnoreCase("help")) {
            sendHelpMenu(p);
            return true;
        }
        sendHelpMenu(p);
        for (String str : commands.keySet())
            if (str.equalsIgnoreCase(args[0])) {
                if (!p.hasPermission(commands.get(str).permission())) {
                    p.sendMessage("§cInsufficient permission to execute this command.");
                    return true;
                }
                commands.get(str).execute(p, args);
                return true;
            }
        return true;
    }

    private void sendHelpMenu(Player p) {
        p.sendMessage("§a§lAvailable Commands §7(v" + Atlas.getInstance().getDescription().getVersion() + ")");
        p.sendMessage("§7> §f/kit §e<kitname>");
        p.sendMessage("§7> §f/kit list");
        if(p.hasPermission("kits.admin")) {
            p.sendMessage("§dStaff Commands:");
            p.sendMessage("§7> §f/kit create §a<kitname> §e[level] [prefix]");
            p.sendMessage("§7> §f/kit delete §a<kitname> §e[level]");
            p.sendMessage("§7> §f/kit seticon §a<kitname> §e[level]");
            p.sendMessage("§7> §f/kit setrequiredexp §a<kitname> <amount> §e[level]");
            p.sendMessage("§7> §f/kit npc §a<add/remove> §e[skullowner]");
            p.sendMessage("§7> §f/kit update §7(Use after updating)");
        }
        p.sendMessage("§7");
    }

}
