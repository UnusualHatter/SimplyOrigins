package dev.originspaper.command;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.gui.OriginProgressGUI;
import dev.originspaper.gui.OriginSelectionGUI;
import dev.originspaper.progression.OriginProgress;
import dev.originspaper.progression.ProgressionManager;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/** {@code /origin} command: open selection, view info, or (admin) set/reset another player. */
public class OriginCommand implements CommandExecutor, TabCompleter {

    private final OriginsPaper plugin;

    public OriginCommand(OriginsPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return openSelf(sender);
        }
        switch (args[0].toLowerCase()) {
            case "set" -> handleSet(sender, args);
            case "reset" -> handleReset(sender, args);
            case "info" -> handleInfo(sender, args);
            case "reload" -> handleReload(sender);
            case "addxp" -> handleAddXp(sender, args);
            case "setlevel" -> handleSetLevel(sender, args);
            default -> sender.sendMessage(TextUtil.msg("§cUso: /origin [set|reset|info|reload|addxp|setlevel]"));
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        plugin.reload();
        sender.sendMessage(TextUtil.msg("§aOriginsPaper recarregado: config e origens atualizadas."));
    }

    private void handleAddXp(CommandSender sender, String[] args) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(TextUtil.msg("§cUso: /origin addxp <jogador> <quantidade>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextUtil.msg("§cQuantidade inválida: §f" + args[2]));
            return;
        }
        plugin.progression().awardXp(target, amount);
        sender.sendMessage(TextUtil.msg("§aConcedido §f" + amount + " XP§a a §f" + target.getName() + "§a."));
    }

    private void handleSetLevel(CommandSender sender, String[] args) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(TextUtil.msg("§cUso: /origin setlevel <jogador> <nível>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
            return;
        }
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextUtil.msg("§cNível inválido: §f" + args[2]));
            return;
        }
        plugin.progression().setLevel(target, level);
        sender.sendMessage(TextUtil.msg("§aNível de §f" + target.getName() + "§a definido para §f"
                + Math.max(1, Math.min(OriginProgress.MAX_LEVEL, level)) + "§a."));
    }

    private boolean openSelf(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextUtil.msg("§cApenas jogadores podem abrir a seleção."));
            return true;
        }
        PlayerOriginData data = plugin.data().get(player.getUniqueId());
        if (data != null && data.hasOrigin()) {
            OriginProgressGUI.open(player); // já tem origem → tela "Minha Origem" (nível/desbloqueios)
            return true;
        }
        OriginSelectionGUI.open(player);
        return true;
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(TextUtil.msg("§cUso: /origin set <jogador> <origem>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
            return;
        }
        Origin origin = plugin.origins().get(args[2]);
        if (origin == null) {
            sender.sendMessage(TextUtil.msg("§cOrigem desconhecida: §f" + args[2]));
            return;
        }
        plugin.data().setOrigin(target, origin);
        target.sendMessage(TextUtil.msg("§6Sua origem agora é §f" + origin.displayName() + "§6."));
        sender.sendMessage(TextUtil.msg("§aDefinido §f" + target.getName() + "§a como §f" + origin.displayName() + "§a."));
    }

    private void handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(TextUtil.msg("§cUso: /origin reset <jogador>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
            return;
        }
        plugin.data().reset(target);
        sender.sendMessage(TextUtil.msg("§aOrigin de §f" + target.getName() + "§a removida."));
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (target.isOnline()) {
                OriginSelectionGUI.open(target);
            }
        }, 5L);
    }

    private void handleInfo(CommandSender sender, String[] args) {
        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
                return;
            }
        } else if (sender instanceof Player p) {
            target = p;
        } else {
            sender.sendMessage(TextUtil.msg("§cUso: /origin info <jogador>"));
            return;
        }
        PlayerOriginData data = plugin.data().get(target.getUniqueId());
        if (data == null || !data.hasOrigin()) {
            sender.sendMessage(TextUtil.msg("§7" + target.getName() + " ainda não escolheu uma origem."));
            return;
        }
        Origin origin = data.getOrigin();
        sender.sendMessage(TextUtil.msg("§6⬛ Origem de §f" + target.getName() + "§6: §e" + origin.displayName()));
        OriginProgress prog = data.currentProgress();
        if (prog != null) {
            if (prog.isMax()) {
                sender.sendMessage(TextUtil.msg("§7Nível: §6" + prog.level() + " §6(MÁX)"));
            } else {
                sender.sendMessage(TextUtil.msg("§7Nível: §e" + prog.level() + " §8• §e"
                        + prog.xp() + "§7/§e" + ProgressionManager.xpToNext(prog.level()) + " §7XP"));
            }
        }
        sender.sendMessage(TextUtil.msg("§8§m                                        "));
        for (Origin.PowerInfo info : origin.infos()) {
            sender.sendMessage(TextUtil.msg("§e⚡ " + info.name()));
            for (String line : info.lore()) {
                sender.sendMessage(TextUtil.msg("  §7" + line));
            }
        }
        sender.sendMessage(TextUtil.msg("§8§m                                        "));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        boolean admin = sender.hasPermission("originspaper.admin");
        // "info" is the only subcommand non-admins may use; the rest are admin-only and stay hidden.
        List<String> adminSubs = List.of("set", "reset", "reload", "addxp", "setlevel");
        List<String> out = new ArrayList<>();
        if (args.length == 1) {
            if ("info".startsWith(args[0].toLowerCase())) {
                out.add("info");
            }
            if (admin) {
                for (String sub : adminSubs) {
                    if (sub.startsWith(args[0].toLowerCase())) {
                        out.add(sub);
                    }
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("info")
                || (admin && adminSubs.contains(args[0].toLowerCase())))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    out.add(p.getName());
                }
            }
        } else if (args.length == 3 && admin && args[0].equalsIgnoreCase("set")) {
            for (Origin origin : plugin.origins().all()) {
                if (origin.id().startsWith(args[2].toLowerCase())) {
                    out.add(origin.id());
                }
            }
        }
        return out;
    }
}
