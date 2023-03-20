package me.Julia0.PassOp;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, Runnable {
    HashMap<Player, Integer> login = new HashMap<Player, Integer>();

    Main helper = this;

    public void onEnable() {
        System.out.println("[PassOp] All ok! ^_^");
        this.login.clear();
        saveDefaultConfig();
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p.isOp() || p.hasPermission("pass.op"))
                this.login.put(p, Integer.valueOf(0));
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, this.helper, 40L, 40L);
        Bukkit.getPluginManager().registerEvents(this, (Plugin)this.helper);
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks((Plugin)this);
        this.login.clear();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onBreak(BlockBreakEvent event) {
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlace(BlockPlaceEvent event) {
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void OnJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("pass.op"))
            this.login.put(event.getPlayer(), Integer.valueOf(0));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void OnLEave(PlayerQuitEvent event) {
        if (this.login.containsKey(event.getPlayer()))
            this.login.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void OnKick(PlayerKickEvent event) {
        if (this.login.containsKey(event.getPlayer()))
            this.login.remove(event.getPlayer());
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void Command(PlayerCommandPreprocessEvent event) {
        for (Player p2: Bukkit.getOnlinePlayers()) {
            if (p2.isOp() || p2.hasPermission("pass.op")) {

        if (event.getMessage().toLowerCase().startsWith("/pass")) {
            event.setCancelled(true);
            String[] mess2 = event.getMessage().toLowerCase().split(" ");
            if (mess2.length >= 1 &&
                    mess2[1].equals(getConfig().getString("pass")))
                this.login.put(event.getPlayer(), Integer.valueOf(1));
            p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 10);
        }
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            p2.sendMessage("§fНеверный §cпароль");
            event.setCancelled(true);
    }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onMove(PlayerMoveEvent event) {
        if (this.login.containsKey(event.getPlayer()) && ((Integer)this.login.get(event.getPlayer())).intValue() == 0)
            event.setCancelled(true);
    }


    public void run() {
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p.isOp() || p.hasPermission("pass.op")) {
                if (!this.login.containsKey(p))
                    this.login.put(p, Integer.valueOf(0));
                if (((Integer)this.login.get(p)).intValue() == 0)
                    p.sendMessage("§fУ вас §cOP§f ведите пароль §7- §b/pass [Пароль]");
            }
        }
    }
}
