package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisableRecipe {
    private final CTW ctw;
    private final List<Recipe> disabledRecipes;

    public DisableRecipe(final CTW ctw) {
        this.disabledRecipes = new ArrayList<Recipe>();
        this.ctw = ctw;
        this.removeRecipes();
    }

    public void reloadDisabledRecipes() {
        CTW.log.info("Reloading disabled recipes...");
        this.restoreRecipes();
        this.removeRecipes();
        CTW.log.info("Reloading disabled recipes complete!");
    }

    private void restoreRecipes() {
        if (!this.disabledRecipes.isEmpty()) {
            for (final Recipe r : this.disabledRecipes) {
                Bukkit.addRecipe(r);
            }
            this.disabledRecipes.clear();
        }
    }

    private void removeRecipes() {
        if (!this.ctw.getConfigHandler().getStringList("Settings.DisabledRecipes").isEmpty()) {
            for (final String s : this.ctw.getConfigHandler().getStringList("Settings.DisabledRecipes")) {
                try {
                    final ItemStack is = new ItemStack(Material.getMaterial(s));
                    if (is == null) {
                        continue;
                    }
                    this.removeRecipe(is);
                } catch (Exception e) {
                    CTW.log.warning("Could not disable recipe for: " + s + " .Error: " + e.getMessage() + " .Details below:");
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeRecipe(final ItemStack is) {
        final Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        while (it.hasNext()) {
            final Recipe recipe = it.next();
            if (recipe != null && recipe.getResult().isSimilar(is)) {
                this.disabledRecipes.add(recipe);
                it.remove();
            }
        }
    }
}
