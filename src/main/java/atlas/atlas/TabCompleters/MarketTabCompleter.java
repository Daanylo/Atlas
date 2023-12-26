package atlas.atlas.TabCompleters;

import atlas.atlas.Commands.MarketCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MarketTabCompleter implements TabCompleter {
    MarketCommandHandler marketCommandHandler = new MarketCommandHandler();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equals("market") && strings.length == 1) {
            return new ArrayList<>(marketCommandHandler.commands.keySet());
        }
        return null;
    }
}
