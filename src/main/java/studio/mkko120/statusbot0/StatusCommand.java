package studio.mkko120.statusbot0;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import studio.mkko120.statusbot0.statusserver.DBConnection;
import studio.mkko120.statusbot0.statusserver.StatusServer;

public class StatusCommand extends Command {

    public StatusCommand() {
        super("status");
    }

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args   arguments used to invoke this command
     */

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("status.serverstatus")) {
            TextComponent message = new TextComponent();
            try {
                if (args.length <= 0 || args[0] == null) {
                    StatusBot0.getInstance().getLogger().info("array empty");
                    message.setText("");
                  /*  message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");*/
                    message.addExtra("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                    message.addExtra("@                                             @\n");
                    message.addExtra("@              BungeeServerStatus             @\n");
                    message.addExtra("@                  by mkko120                 @\n");
                    message.addExtra("@                     v1.0                    @\n");
                    message.addExtra("@                                             @\n");
                    message.addExtra("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                    message.setColor(ChatColor.YELLOW);
                    sender.sendMessage(message);
                } else if (args.length == 1) {
                    if (args[0].equals("reload")) {
                        if (sender.hasPermission("status.reload")) {
                            message.setText("");
                            StatusBot0.getInstance().loadConfig();
                            DBConnection.resultUpdate();
                            message.addExtra("Successfully reloaded plugin!");
                            message.setColor(ChatColor.GREEN);
                            sender.sendMessage(message);
                        }
                    } else if (args[0].equals("list")) {
                        if ((sender instanceof ProxiedPlayer)) {

                        } else {
                            message.setText("");
                            message.addExtra("BungeeServerStatus[DB][BUNGEE] by mkko120 v1.0\n");
                            message.addExtra("Online servers: " + Storage.serverArray.size() + "\n");
                            message.addExtra("List of online servers:\n");
                            if (Storage.serverArray.isEmpty()) {
                                message.addExtra("No servers online!");
                            } else {
                                for (int i = 0; i < Storage.serverArray.size(); i++) {
                                    message.addExtra(" - " + Storage.serverArray.get(i).getName() + "\n");
                                }
                                message.addExtra("Type '/status info <server> to see more information about specific server");
                            }
                            sender.sendMessage(message);
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equals("info")) {
                        for (int i = 0; i < Storage.serverArray.size(); i++) {
                            StatusServer server = Storage.serverArray.get(i);
                            if (server.getName().equals(args[1])) {
                                message.setText("");
                                message.addExtra("Information about server: " + server.getName());
                                message.addExtra("Server online? " + server.isOnline());
                                message.addExtra("Online players: " + server.getOnline());
                                message.addExtra("Max players: " + server.getMax());
                                sender.sendMessage(message);
                            }
                        }
                    }
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                message.setText("");
                  /*  message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");*/
                message.addExtra("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                message.addExtra("@                                             @\n");
                message.addExtra("@              BungeeServerStatus             @\n");
                message.addExtra("@                  by mkko120                 @\n");
                message.addExtra("@                     v1.0                    @\n");
                message.addExtra("@                                             @\n");
                message.addExtra("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                message.setColor(ChatColor.YELLOW);
                sender.sendMessage(message);
            }
        }
    }
}
