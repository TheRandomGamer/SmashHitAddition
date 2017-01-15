package com.frash23.smashhit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by robin on 15/01/17.
 */
public class DamageResolver {

    private boolean USE_CRITS, OLD_CRITS;

    public DamageResolver(boolean useCrits, boolean oldCrits){
        USE_CRITS = useCrits;
        OLD_CRITS = oldCrits;
    }

    public double getDamage(Player damager, Damageable entity) {

        double damage = 0;

        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                Class<Enum> genericAttributes = (Class<Enum>) Class.forName("net.minecraft.server."+version+".GenericAttributes");
                Enum genericAttributesEnum = getInstance("ATTACK_DAMAGE",genericAttributes);

            Object handle = damager.getClass().getMethod("getHandle").invoke(damager);
            Object AttributeInstance = handle.getClass().getMethod("getAttributeInstance", Enum.class).invoke(handle,genericAttributesEnum);
            damage = (Double) AttributeInstance.getClass().getMethod("getValue").invoke(AttributeInstance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(USE_CRITS
                && !( (Entity)damager ).isOnGround()
                && damager.getVelocity().getY() < 0
                && OLD_CRITS || !damager.isSprinting()
                ) damage *= 1.5;

        return damage;
    }

    public static <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }


}
