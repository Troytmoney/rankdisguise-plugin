package com.yourname.rankdisguise;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

public class RankDisguisePlugin extends JavaPlugin {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        luckPerms = LuckPermsProvider.get();
        getLogger().info("RankDisguise plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("RankDisguise plugin disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rankdisguise")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /rankdisguise <username> <rank>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            String rank = args[1];

            if (target == null) {
                sender.sendMessage("Player not found!");
                return true;
            }

            User user = luckPerms.getUserManager().getUser(target.getUniqueId());

            if (user != null) {
                InheritanceNode disguiseNode = InheritanceNode.builder(rank).build();
                user.data().add(disguiseNode);
                luckPerms.getUserManager().saveUser(user);

                sender.sendMessage(target.getName() + " is now disguised as rank " + rank);
            } else {
                sender.sendMessage("Failed to find LuckPerms user data.");
            }

            return true;
        } else if (command.getName().equalsIgnoreCase("unrankdisguise")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /unrankdisguise <username>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("Player not found!");
                return true;
            }

            User user = luckPerms.getUserManager().getUser(target.getUniqueId());

            if (user != null) {
                user.data().clear();  // This removes all nodes, adjust this as needed
                luckPerms.getUserManager().saveUser(user);

                sender.sendMessage(target.getName() + "'s rank disguise has been removed.");
            } else {
                sender.sendMessage("Failed to find LuckPerms user data.");
            }

            return true;
        }

        return false;
    }
}
