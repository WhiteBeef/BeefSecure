package ru.whitebeef.beefsecure.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import ru.whitebeef.beefsecure.BeefSecure;
import ru.whitebeef.beefsecure.utils.Messages;

import java.util.ArrayList;
import java.util.List;

public class SecureCommandExecutor implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Messages.createNotEnoughArgumentsMessage());
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (BeefSecure.getInstance().reload())
                sender.sendMessage(Messages.createReloadSuccessfulMessage());
            else
                sender.sendMessage(Messages.createReloadUnsuccessfulMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> retArr = new ArrayList<>();
        retArr.add("reload");
        return retArr;
    }
}
