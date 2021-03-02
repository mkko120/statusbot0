package studio.mkko120.statusbot0.statusserver;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import studio.mkko120.statusbot0.StatusBot0;
import studio.mkko120.statusbot0.Storage;


import java.sql.*;
import java.util.logging.Level;

public class DBConnection {

    private static String url;
    private static String forwardurl;
    private static int port;
    private static int forwardport;
    private static String database;
    private static String username;
    private static String sshusername;
    private static String password;
    private static String sshpassword;
    private static boolean remote;
    private static Session session = null;

    public static void loadvars() {
        try {
            url = StatusBot0.getInstance().getConfig().getString("database.url");
            forwardurl = StatusBot0.getInstance().getConfig().getString("database.forwardurl");
            port = StatusBot0.getInstance().getConfig().getInt("database.port");
            forwardport = StatusBot0.getInstance().getConfig().getInt("database.forwardport");
            database = StatusBot0.getInstance().getConfig().getString("database.database");
            username = StatusBot0.getInstance().getConfig().getString("database.username");
            sshusername = StatusBot0.getInstance().getConfig().getString("database.sshusername");
            password = StatusBot0.getInstance().getConfig().getString("database.password");
            sshpassword = StatusBot0.getInstance().getConfig().getString("database.sshpassword");
            remote = StatusBot0.getInstance().getConfig().getBoolean("database.remote");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection localquerry() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database, username, password);
            ProxyServer.getInstance().getLogger().log(Level.INFO, "Successfully initialized database connection!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    public static Connection backupquerry() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/StatusServer", "root", "");
            ProxyServer.getInstance().getLogger().log(Level.INFO, "Successfully initialized database connection!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
        * Remote connection to database by ssh port forwarding.
        * @author mkko120
        */

    public static Connection remotequerry() {

        try {
            session = new JSch().getSession(sshusername, url, port);
            session.setPassword(sshpassword);
            session.connect();
            session.setPortForwardingL(forwardport, forwardurl, port);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + forwardurl +":" + forwardport + "/" + database, username, password);
            ProxyServer.getInstance().getLogger().log(Level.INFO, "Successfully initialized database connection!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void resultUpdate() {
        Connection con = null;
        try {
            if (remote) {
                con = remotequerry();
            } else {
                con = localquerry();
            }

            if (con == null) {
                ProxyServer.getInstance().getLogger().warning("Cannot initalize database connection, trying with default settings...");
                con = backupquerry();
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM statistics");
            if (rs == null) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING,"No connection to database (result == null)");
                return;
            }
            else if (rs.wasNull()) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING,"No data in database");
                return;
            }
            while (rs.next()) {
                StatusServer server;

                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(rs.getString("servername"));

                if (rs.getBoolean("isonline")) {
                    server = new StatusServer(serverInfo.getName(),
                            rs.getBoolean("isonline"),
                            serverInfo.getPlayers().size(),
                            rs.getInt("max"));
                } else {
                    server = new StatusServer(rs.getString("servername"),
                            rs.getBoolean("isonline"));
                }
                Storage.serverArray.add(server);
            }

            ProxyServer.getInstance().getLogger().info(Storage.serverArray.toString()+", "+rs.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
