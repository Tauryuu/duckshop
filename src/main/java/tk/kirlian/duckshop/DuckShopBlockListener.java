package tk.kirlian.duckshop;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.logging.Logger;
import tk.kirlian.duckshop.errors.*;
import tk.kirlian.duckshop.signs.*;
import tk.kirlian.permissions.PermissionsException;

/**
 * Listens for block events -- like placing a sign.
 */
public class DuckShopBlockListener extends BlockListener {
    private final DuckShop plugin;
    private final Logger log;

    public DuckShopBlockListener(DuckShop plugin) {
        this.log = plugin.log;
        this.plugin = plugin;
    }

    @Override
    public void onSignChange(SignChangeEvent event) {
        if(event.isCancelled()) return;
        TradingSign sign = null;
        try {
            sign = new TradingSign(plugin,
                                   event.getPlayer(),
                                   event.getBlock().getLocation(),
                                   event.getLines());
        } catch(InvalidSyntaxException ex) {
            // Do nothing
        } catch(PermissionsException ex) {
            // Science fiction allusions FTW
            event.setCancelled(true);
            event.getPlayer().sendMessage("I'm sorry, " + event.getPlayer().getName() +". I'm afraid I can't do that.");
        }
        if(sign != null) {
            sign.writeToStringArray(event.getLines());
            event.getPlayer().sendMessage("Created sign successfully.");
            if(!sign.isGlobal()) {
                event.getPlayer().sendMessage("Type \"/duckshop link\" to connect this sign with a chest.");
            }
        }
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        Block block = event.getBlock();
        if(block.getState() instanceof Sign) {
            TradingSign sign = null;
            try {
                sign = new TradingSign(plugin,
                                       null,
                                       block.getLocation(),
                                       ((Sign)block.getState()).getLines());
            } catch(InvalidSyntaxException ex) {
                // Do nothing!
            } catch(PermissionsException ex) {
                // This shouldn't happen, as there shouldn't be a
                // PermissionsException until sign.destroy() below.
                throw new RuntimeException(ex);
            }
            if(sign != null) {
                try {
                    sign.destroy(event.getPlayer());
                } catch(PermissionsException ex) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("You can't break this!");
                }
            }
        }
    }
}
