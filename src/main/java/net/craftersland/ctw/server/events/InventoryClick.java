package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryClick implements Listener {
    private final CTW ctw;

    public InventoryClick(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onFoodChange(@NotNull FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    /**
     * Cancel removing the hat from the helmet slot
     */
    @EventHandler
    public void cancelRemoveHat(@NotNull InventoryClickEvent e) {

        if (e.getSlotType() == InventoryType.SlotType.ARMOR && e.getSlot() == 39) {
            if (e.getCurrentItem().getType().name().contains("WOOL")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final @NotNull InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        if (event.getInventory() != null) {
            if (event.getCursor().getType() != null) {
                if (event.getInventory().getTitle().matches(this.ctw.getKitHandler().getMainMenuTitle()) && p.getGameMode() == GameMode.SURVIVAL) {
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                    }
                    if (event.getSlot() == 2) {
                        event.setCancelled(true);
                        p.closeInventory();
                        this.ctw.getKitHandler().sendKitMenu(p);
                    } else if (event.getSlot() == 4) {
                        event.setCancelled(true);
                        p.closeInventory();
                        this.ctw.getKitHandler().sendTrashMenu(p);
                    } else if (event.getSlot() == 6) {
                        event.setCancelled(true);
                        p.closeInventory();
                        this.ctw.getKitHandler().sendEnchantMenu(p);
                    } else if (event.getSlot() < 9 && event.getSlot() != 2 && event.getSlot() != 4 && event.getSlot() != 6) {
                        event.setCancelled(true);
                    } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR && event.getSlot() < 27) {
                        event.setCancelled(true);
                    }
                } else if (event.getInventory().equals(this.ctw.getKitHandler().getKitsInventory(p)) && p.getGameMode() == GameMode.SURVIVAL) {
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                    }
                    if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
                    event.setCancelled(true);
                    this.ctw.getItemKitHandler().sendKit(p, event.getSlot());
                } else if (event.getInventory().equals(this.ctw.getKitHandler().getEnchantsInventory(p)) && p.getGameMode() == GameMode.SURVIVAL) {
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                    }
                    if (event.getCursor() != null && event.getCursor().getType() != Material.AIR && event.getSlot() < 27) {
                        event.setCancelled(true);
                    }
                } else if (event.getInventory().getTitle().matches(this.ctw.getJoinMenu().joinMenuTitle())) {
                    if (event.getSlot() == ctw.getConfigHandler().getInteger("Menus.Join.Items.AutoJoin.Slot")) {
                        this.ctw.getPlayerHandler().autoAddTeam(p);
                        event.setCancelled(true);
                    } else if (event.getSlot() == ctw.getConfigHandler().getInteger("Menus.Join.Items.RedTeam.Slot")) {
                        if (p.hasPermission("CTW.pickupteam")) {
                            this.ctw.getPlayerHandler().addRedTeam(p);
                        } else {
                            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.ctw.getLanguageHandler().getMessage("ChatMessages.SelectTeamNoPermission")));
                        }
                        event.setCancelled(true);
                    } else if (event.getSlot() == ctw.getConfigHandler().getInteger("Menus.Join.Items.BlueTeam.Slot")) {
                        if (p.hasPermission("CTW.pickupteam")) {
                            this.ctw.getPlayerHandler().addBlueTeam(p);
                        } else {
                            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.ctw.getLanguageHandler().getMessage("ChatMessages.SelectTeamNoPermission")));
                        }
                        event.setCancelled(true);
                    }
                    this.closeInv(p);
                } else if ((event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.WOOL && p.getGameMode() == GameMode.SURVIVAL) || (event.getCursor().getType() == Material.WOOL && p.getGameMode() == GameMode.SURVIVAL)) {
                    final ItemStack item = event.getCurrentItem();
                    if (!event.getViewers().isEmpty()) {
                        this.ctw.getTakenWools().woolTakenCheck(p, item.getData().getData());
                    }
                }
            }
        }
    }

    private void closeInv(final @NotNull Player p) {
        Bukkit.getScheduler().runTask(this.ctw, p::closeInventory);
    }
}
