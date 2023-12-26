package atlas.atlas.TabCompleters;

import atlas.atlas.Commands.AdminCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminTabCompleter implements TabCompleter {
    AdminCommandHandler adminCommandHandler = new AdminCommandHandler();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equals("admin") && strings.length == 1) {
            return new ArrayList<>(adminCommandHandler.commands.keySet());
        }
        if (command.getName().equals("admin") && strings.length == 2 && strings[0].equalsIgnoreCase("gold")) {
            ArrayList<String> sub = new ArrayList<>();
            sub.add("add");
            sub.add("set");
            return sub;
        }
        if (command.getName().equals("admin") && strings.length == 2 && strings[0].equalsIgnoreCase("npc")) {
            ArrayList<String> sub = new ArrayList<>();
            sub.add("sell");
            sub.add("remove");
            return sub;
        }
        return null;
    }
}
