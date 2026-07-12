package scriptservice.ultra_hardcore.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import scriptservice.ultra_hardcore.classes.activePlayer;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.classes.scoreboardLines;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.convertionUtil;
import scriptservice.ultra_hardcore.utils.gameUtil;

import java.util.*;

/**
 * event usage: Global
 * description: track kills and assists for each player.
 */
public class statsTracker extends initManager implements Listener {
    public statsTracker(uhc plugin) {
        super(plugin);
    }

    // vars
    private final HashMap<UUID, HashMap<UUID, Long>> recentDamagers = new HashMap<>();
    private gameUtil gameUtil;

    @Override
    public void init(PluginManager pluginManager) {
        gameUtil = plugin.gameUtil;
        pluginManager.registerEvents(this, plugin); // register event

        final long ticks = (long) convertionUtil.minuteToTick(1);
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::cleanup, ticks, ticks);
    }

    @EventHandler()
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player victim = event.getEntity();
        if (victim == null || victim.getUniqueId() == null) {return;}

        final Optional<activePlayer> optionalActiveVictim = gameUtil.isPlayerActive(victim);

        // victim isn't active
        if (!optionalActiveVictim.isPresent()) {return;}
        final activePlayer activeVictim = optionalActiveVictim.get();

        final Player killer = victim.getKiller();
        final Optional<activePlayer> optionalActiveKiller = gameUtil.isPlayerActive(killer);

        // killer isn't active
        if (!optionalActiveKiller.isPresent()) {return;}
        final activePlayer activeKiller = optionalActiveKiller.get();

        // -- KILL
        if (! (activeKiller.getUUID().equals(activeVictim.getUUID()))) {
            // add kill to killer
            activeKiller.addKill();
            // update scoreboard
            gameUtil.updateScoreboard(activeKiller, scoreboardLines.KILLS, activeKiller.getKills());
        }

        // -- ASSISTS
        HashMap<UUID, Long> damagers = recentDamagers.get(victim.getUniqueId());
        if (damagers != null) {
            long now = System.currentTimeMillis();

            for (Map.Entry<UUID, Long> entry : damagers.entrySet()) {
                final UUID attackerUUID = entry.getKey();
                final long lastHit = entry.getValue();

                boolean isKiller = attackerUUID.equals(killer.getUniqueId());
                boolean withinWindow = (now - lastHit) <= plugin.getGameConfig().getAssistDelayWindow();

                if (!isKiller && withinWindow) {
                    // check if active
                    final Optional<activePlayer> optionalActiveAttacker = gameUtil.isPlayerActive(attackerUUID);
                    if (!optionalActiveAttacker.isPresent()) {return;}
                    final activePlayer activeAttacker = optionalActiveAttacker.get();

                    // add assist to attacker
                    activeAttacker.addAssist();
                    // update scoreboard
                    gameUtil.updateScoreboard(activeAttacker, scoreboardLines.ASSISTS, activeAttacker.getAssists());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        // not living
        if (!(event.getEntity() instanceof LivingEntity)) {return;}

        // get attacker
        final UUID victimUUID = event.getEntity().getUniqueId();
        final Entity damager = event.getDamager();
        Player attacker = null;

        if (damager instanceof Player) {
            attacker = (Player) damager;
        } else if (damager instanceof Projectile) {
            final Projectile projectile = (Projectile) damager;

            if (projectile.getShooter() instanceof Player) {
                attacker = (Player) projectile.getShooter();
            }
        }

        // attack source not from player
        if (attacker == null) { return; }

        // attacker not active
        if (!gameUtil.isPlayerActive(attacker).isPresent()) { return; }

        // suicide
        if (attacker.getUniqueId().equals(victimUUID)) { return; }


        // add to recentDamagers
        recentDamagers.computeIfAbsent(victimUUID, uuid -> new LinkedHashMap<>()).put(attacker.getUniqueId(), System.currentTimeMillis()); // cool
    }

    private void cleanup() {
        final long now = System.currentTimeMillis();
        final Iterator<HashMap.Entry<UUID, HashMap<UUID, Long>>> victimIterator = recentDamagers.entrySet().iterator();

        while (victimIterator.hasNext()) {
            Map<UUID, Long> attackers = victimIterator.next().getValue();

            attackers.values().removeIf(timestamp -> (now - timestamp) > plugin.getGameConfig().getAssistDelayWindow());
            if (attackers.isEmpty()) {
                victimIterator.remove();
            }
        }
    }
}
