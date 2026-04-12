package scriptservice.ultra_hardcore.events;

import com.google.common.collect.Multimap;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;
import scriptservice.ultra_hardcore.classes.initManager;
import scriptservice.ultra_hardcore.uhc;
import scriptservice.ultra_hardcore.utils.playerUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.UUID;

/**
 * event usage: Global
 * description: patch damages to required one
 * credits :: <a href="https://github.com/NickNqck/UHC_Meetup/blob/main/src/main/java/fr/nicknqck/PatchCritical.java">github repo found</a>, rewritten
 */
public class entityDamageByEntityEvent extends initManager implements Listener {
    public entityDamageByEntityEvent(uhc plugin) {
        super(plugin);
    }

    // init
    private static playerUtil playerUtil;

    @Override
    public void init(PluginManager pluginManager) {
        playerUtil = plugin.playerUtil;

        pluginManager.registerEvents(this, plugin); // register event
    }

    @EventHandler(priority = EventPriority.HIGHEST) // askip c'est mieux
    public void onEvent(EntityDamageByEntityEvent event) {

        event.getDamager().sendMessage("§eoriginalDamage: " + event.getDamage());
        // TODO :: check if game started, if not, cancel event (no damage !!)
        critUtil.patch(event, plugin.getGameConfig().getCritPercentage()); // patch crit damages
        strengthUtil.patch(event, plugin.getGameConfig().getStrengthPercentage());
        event.getDamager().sendMessage("§6finalDamage: " + event.getDamage());
        event.getDamager().sendMessage("§8-----------------");
    }

    // event util class
    public static class critUtil {
        //Trouver sur https://www.spigotmc.org/threads/how-to-get-attack-damage-attributemodifier-from-an-itemstack-as-displayed-on-items-in-game.284455/
        private static double getAttackDamage(ItemStack itemStack) {
            // no item sent
            if (itemStack == null) {
                return 1.0;
            }

            // nms itemstack doesnt exist
            final net.minecraft.server.v1_8_R3.ItemStack craftItemStack = CraftItemStack.asNMSCopy(itemStack);
            if (craftItemStack == null) {
                return 1.0;
            }

            // nms.getItem doesnt exist
            if (craftItemStack.getItem() == null) {
                return 1.0;
            }

            // item not sword, tool or hoe
            Item item = craftItemStack.getItem();
            if (!(item instanceof ItemSword) && !(item instanceof ItemTool) && !(item instanceof ItemHoe)) {
                return 1.0;
            }

            // consts
            double attackDamage = 1.0;
            final UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"); // generic.attackDamage uuid

            // get attributes from item and handle new attackDamage
            Multimap<String, AttributeModifier> map = item.i();
            Collection<AttributeModifier> attributes = map.get(GenericAttributes.ATTACK_DAMAGE.getName());
            if (attributes.isEmpty()) {
                return attackDamage;
            }
            for (AttributeModifier am : attributes)
                if (am.a().toString().equalsIgnoreCase(uuid.toString()) && am.c() == 0) attackDamage += am.d();

            double y = 1;
            for (AttributeModifier am : attributes)
                if (am.a().toString().equalsIgnoreCase(uuid.toString()) && am.c() == 1) y += am.d();

            attackDamage *= y;
            for (AttributeModifier am : attributes)
                if (am.a().toString().equalsIgnoreCase(uuid.toString()) && am.c() == 2)
                    attackDamage *= (1 + am.d());

            // return it
            return attackDamage;
        }

        private static double getAttackValue(Player player) {
            // player doesnt have item in hand
            final ItemStack itemInHand = player.getItemInHand();
            if (itemInHand == null) {
                return 0;
            }

            // consts
            double attackDamage = getAttackDamage(itemInHand);

            // handle weakness
            if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                attackDamage = attackDamage - 0.5; // Si il a weakness on ajoute 0.5 coeurs. On passe la weakness en priorité
            }

            // handle strength
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                attackDamage = attackDamage * 2.3; // Si il a force on fait * 2.3 pour ses dégats
            }

            // handle sharpness
            if(!itemInHand.hasItemMeta()) {
                return attackDamage; // l'item n'a pas de meta
            }
            if (!itemInHand.getItemMeta().hasEnchants()) {
                return attackDamage; // l'item meta na pas d'enchantement
            }
            if (itemInHand.getEnchantments() == null) {
                return attackDamage; // pas d'enchantement
            }
            if (!itemInHand.containsEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL)) {
                return attackDamage; // pas de sharpness
            }

            attackDamage = attackDamage + (itemInHand.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DAMAGE_ALL) * 1.25); // on ajoute la sharpness
            return attackDamage;
        }

        private static boolean isCritical(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) {
                return false;//Le damager ne peut pas être un joueur donc pas de critical
            }
            Player p = (Player) event.getDamager();
            double attackValue = getAttackValue(p);
            DecimalFormat df = new DecimalFormat("0.00");//Les dégats de la force diffère a 0.0000001 environ.
            return !df.format(event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE)).equals(df.format(attackValue));//Si les dégats sont pas pareil on return true
        }

        public static void patch(EntityDamageByEntityEvent event, int percent) {
            // not critical
            if (!isCritical(event)) {
                return;
            }

            // consts
            double originalDamage = event.getDamage();
            double addedDamage = (1.0 + (((float) percent / 100.0)));
            // les crits ajoute 50% de degats en plus, donc on divise par 1.5 pour enlever
            // puis on multiplie les degats de base par le pourcentage voulu
            double returnedDamage = (originalDamage / 1.5) * addedDamage;
            event.setDamage(returnedDamage);
            event.getDamager().sendMessage("§bfinalCritDamage: " + returnedDamage);
        }
    }

    public static class strengthUtil {
        public static void patch(EntityDamageByEntityEvent event, int strengthPercent) {
            // doesnt have strength
            if (!((Player) event.getDamager()).hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                return;
            }

            // Pour retirer la force
            event.setDamage(event.getDamage() * plugin.getGameConfig().getStrengthMultiplier()); // j'ai fait les tests avec une épée en diams, on passe de 8.00 à 14.64 en degats
            double force = ((double) strengthPercent / 100) + 1.0;

            BigDecimal bigDecimal = new BigDecimal(event.getDamage());
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

            event.setDamage(bigDecimal.doubleValue());
            event.setDamage(event.getDamage()*force);

            event.getDamager().sendMessage("§cfinalStrengthDamage: " + event.getDamage());
        }
    }
}
