package tk.kirlian.util;

import com.nijikokun.register.payment.Method;
import org.bukkit.plugin.Plugin;
import org.bukkit.Server;

/**
 * A dummy economy plugin.
 *
 * Useful for emergencies, especially skydiving elephants.
 *
 * All the methods throw an {@link UnsupportedOperationException} if
 * the caller tries to do anything that involves modifying the account
 * e.g. adding a non-zero amount.
 */
public class DummyEconomy implements Method {
    private static DummyEconomy instance = new DummyEconomy();

    /**
     * Used to check if a player exists.
     */
    private Server server;

    private DummyEconomy() {
    }

    public static DummyEconomy getInstance() {
        return instance;
    }

    public Object getPlugin() {
        return null;
    }

    public String getName() {
        return "DummyEconomy";
    }

    public String getVersion() {
        return "0";
    }

    public String format(double amount) {
        return "nothing";
    }

    public boolean hasBanks() {
        return false;
    }

    public boolean hasBank(String bank) {
        return false;
    }

    public boolean hasAccount(String name) {
        return (server != null && server.getPlayer(name) != null);
    }

    public boolean hasBankAccount(String bank, String name) {
        return false;
    }

    public MethodAccount getAccount(String name) {
        return new DummyAccount();
    }

    public MethodBankAccount getBankAccount(String bank, String name) {
        return null;
    }

    public boolean isCompatible(Plugin plugin) {
        return false; // Should this be true?
    }

    public void setPlugin(Plugin plugin) {
        server = plugin.getServer();
    }

    public class DummyAccount implements MethodAccount {
        private String name;

        public DummyAccount() {
        }

        public double balance() {
            return 0.0;
        }

        public boolean set(double amount) {
            if(amount != 0.0) {
                throw new UnsupportedOperationException();
            } else {
                return true;
            }
        }

        public boolean add(double amount) {
            if(amount != 0.0) {
                throw new UnsupportedOperationException();
            } else {
                return true;
            }
        }

        public boolean subtract(double amount) {
            return set(amount);
        }

        public boolean multiply(double amount) {
            if(amount != 1.0 && amount != 0.0) {
                throw new UnsupportedOperationException();
            } else {
                return true;
            }
        }

        public boolean divide(double amount) {
            return multiply(amount);
        }

        public boolean hasEnough(double amount) {
            return (this.balance() >= amount);
        }

        public boolean hasOver(double amount) {
            return (this.balance() > amount);
        }

        public boolean hasUnder(double amount) {
            return (this.balance() < amount);
        }

        public boolean isNegative() {
            return (this.balance() < 0);
        }

        public boolean remove() {
            return false;
        }
    }
}