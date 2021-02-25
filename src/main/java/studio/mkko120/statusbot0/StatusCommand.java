package studio.mkko120.statusbot0;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import studio.mkko120.statusbot0.statusserver.DBConnection;

public class StatusCommand extends Command {

    public StatusCommand() {
        super("StatusCommand");
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
            switch (args.length) {
                case 0:
                    message.setText("");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("\n");
                    message.addExtra("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                    message.addExtra("@                                             @\n");
                    message.addExtra("@              BungeeServerStatus             @\n");
                    message.addExtra("@                  by mkko120                 @\n");
                    message.addExtra("@                     v1.0                    @\n");
                    message.addExtra("@                                             @\n");
                    message.addExtra("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
                    message.setColor(ChatColor.YELLOW);
                    sender.sendMessage(message);
                case 1:
                    if (args[0].equals("reload")) {
                        if (sender.hasPermission("status.reload")) {
                            message.setText("");
                            StatusBot0.getInstance().loadConfig();
                            DBConnection.resultUpdate();
                            message.addExtra("Successfully reloaded config!");
                            message.setColor(ChatColor.GREEN);
                            sender.sendMessage(message);
                        }
                    } else if (args[0].equals("list")) {
                        if ((sender instanceof ProxiedPlayer)){

                        } else {
                            message.addExtra("BungeeServerStatus[DB][BUNGEE] by mkko120 v1.0\n");
                            message.addExtra("Online servers: " + Storage.serverArray.size() + "\n");
                            message.addExtra("List of online servers:\n");
                            for (int i = 0; i<= Storage.serverArray.size(); i++) {
                                message.addExtra(" - " + Storage.serverArray.get(i).getName() + "\n");
                            }
                            message.addExtra("Type '/status info <server> to see more information about specific server\n");
                            sender.sendMessage(message);
                        }
                    }
                case 2:
                    if (args[1].equals("info")) {

                    }


                default:


            }
        }
    }
}
