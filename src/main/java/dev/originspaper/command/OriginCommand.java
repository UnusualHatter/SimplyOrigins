package dev.originspaper.command;

import dev.originspaper.OriginsPaper;
import dev.originspaper.api.Origin;
import dev.originspaper.api.PowerType;
import dev.originspaper.gui.OriginProgressGUI;
import dev.originspaper.gui.OriginSelectionGUI;
import dev.originspaper.integration.IntegrationHook;
import dev.originspaper.progression.OriginProgress;
import dev.originspaper.progression.ProgressionManager;
import dev.originspaper.registry.PlayerOriginData;
import dev.originspaper.util.PerfStats;
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
            case "debug" -> handleDebug(sender, args);
            default -> sender.sendMessage(TextUtil.msg("§cUso: /origin [set|reset|info|reload|addxp|setlevel|debug]"));
        }
        return true;
    }

    private void handleDebug(CommandSender sender, String[] args) {
        if (!sender.hasPermission("originspaper.admin")) {
            sender.sendMessage(TextUtil.msg("§cSem permissão."));
            return;
        }
        String topic = args.length >= 2 ? args[1].toLowerCase() : "";
        switch (topic) {
            case "" -> debugSummary(sender);
            case "perf" -> debugPerf(sender);
            case "integrations" -> debugIntegrations(sender);
            case "player" -> debugPlayer(sender, args);
            default -> sender.sendMessage(TextUtil.msg("§cUso: /origin debug [perf|integrations|player <jogador>]"));
        }
    }

    private void debugSummary(CommandSender sender) {
        int withOrigin = 0;
        int totalPowers = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerOriginData d = plugin.data().get(p.getUniqueId());
            if (d != null && d.hasOrigin()) {
                withOrigin++;
                totalPowers += d.getPowers().size();
            }
        }
        int activeHooks = (int) plugin.integrations().hooks().stream().filter(IntegrationHook::isActive).count();
        sender.sendMessage(TextUtil.msg("§6⬛ OriginsPaper §fv" + plugin.getPluginMeta().getVersion()));
        sender.sendMessage(TextUtil.msg("§7Tick global: §f" + plugin.tick() + "§7 (1/s desde o enable)"));
        sender.sendMessage(TextUtil.msg("§7Origens registradas: §f" + plugin.origins().all().size()));
        sender.sendMessage(TextUtil.msg("§7Jogadores online com origem: §f" + withOrigin
                + "§7/§f" + Bukkit.getOnlinePlayers().size()));
        sender.sendMessage(TextUtil.msg("§7Instâncias de poder ativas: §f" + totalPowers));
        sender.sendMessage(TextUtil.msg("§7Integrações ativas: §f" + activeHooks
                + "§7/§f" + plugin.integrations().hooks().size()
                + " §8(detalhes: /origin debug integrations)"));
    }

    private void debugPerf(CommandSender sender) {
        sender.sendMessage(TextUtil.msg("§6⬛ Desempenho §7(acumulado desde o enable)"));
        sender.sendMessage(TextUtil.msg("§7Ciclos processados: §f" + plugin.tick()));
        sender.sendMessage(TextUtil.msg("§7onTick despachados: §f" + PerfStats.onTickCalls()
                + " §8• §7pulados (cadência/event-only): §f" + PerfStats.onTickSkips()
                + " §8• §7erros: §f" + PerfStats.onTickErrors()));
        sender.sendMessage(TextUtil.msg("§7Varreduras de entidades: §f" + PerfStats.entityScans()));
        sender.sendMessage(TextUtil.msg("§7Consultas de claim: §f" + PerfStats.claimChecks()
                + " §8• §7negadas: §f" + PerfStats.claimDenials()));
    }

    private void debugIntegrations(CommandSender sender) {
        sender.sendMessage(TextUtil.msg("§6⬛ Integrações"));
        for (IntegrationHook hook : plugin.integrations().hooks()) {
            String color = hook.isActive() ? "§a" : (hook.isPresent() ? "§e" : "§8");
            sender.sendMessage(TextUtil.msg("§7- §f" + hook.name() + "§7: " + color + hook.status()));
        }
    }

    private void debugPlayer(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(TextUtil.msg("§cUso: /origin debug player <jogador>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage(TextUtil.msg("§cJogador não encontrado."));
            return;
        }
        PlayerOriginData data = plugin.data().get(target.getUniqueId());
        sender.sendMessage(TextUtil.msg("§6⬛ Debug de §f" + target.getName()));
        if (data == null || !data.hasOrigin()) {
            sender.sendMessage(TextUtil.msg("§7Sem origem selecionada."));
            return;
        }
        Origin origin = data.getOrigin();
        OriginProgress prog = data.currentProgress();
        sender.sendMessage(TextUtil.msg("§7Origem: §f" + origin.id() + " §8(" + origin.displayName() + ")"));
        if (prog != null) {
            sender.sendMessage(TextUtil.msg("§7Nível: §f" + prog.level() + " §8• §7XP: §f" + prog.xp()
                    + (prog.isMax() ? " §6(MÁX)" : "§7/§f" + ProgressionManager.xpToNext(prog.level()))));
        }
        sender.sendMessage(TextUtil.msg("§7Poderes ativos (§f" + data.getPowers().size() + "§7):"));
        for (PowerType power : data.getPowers()) {
            sender.sendMessage(TextUtil.msg("§7- §f" + power.getId()
                    + " §8[" + power.getTickCadence().name().toLowerCase() + "]"));
        }
        if (data.getCooldowns().isEmpty()) {
            sender.sendMessage(TextUtil.msg("§7Cooldowns: §fnenhum registrado"));
        } else {
            sender.sendMessage(TextUtil.msg("§7Cooldowns registrados:"));
            long now = System.currentTimeMillis();
            for (var entry : data.getCooldowns().entrySet()) {
                long secondsAgo = (now - entry.getValue()) / 1000L;
                sender.sendMessage(TextUtil.msg("§7- §f" + entry.getKey()
                        + " §8(último uso há " + secondsAgo + "s)"));
            }
        }
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
        if (amount <= 0) {
            sender.sendMessage(TextUtil.msg("§cA quantidade de XP deve ser maior que zero."));
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
        List<String> adminSubs = List.of("set", "reset", "reload", "addxp", "setlevel", "debug");
        // Admin subcommands whose second argument is a player name ("debug" takes a topic instead).
        List<String> playerArgSubs = List.of("set", "reset", "addxp", "setlevel");
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
                || (admin && playerArgSubs.contains(args[0].toLowerCase())))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    out.add(p.getName());
                }
            }
        } else if (args.length == 2 && admin && args[0].equalsIgnoreCase("debug")) {
            for (String topic : List.of("perf", "integrations", "player")) {
                if (topic.startsWith(args[1].toLowerCase())) {
                    out.add(topic);
                }
            }
        } else if (args.length == 3 && admin && args[0].equalsIgnoreCase("debug")
                && args[1].equalsIgnoreCase("player")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
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
