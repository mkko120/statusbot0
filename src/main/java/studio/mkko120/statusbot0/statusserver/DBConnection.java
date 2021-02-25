package studio.mkko120.statusbot0.statusserver;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import studio.mkko120.statusbot0.Storage;


import java.sql.*;
import java.util.logging.Level;

import static studio.mkko120.statusbot0.StatusBot0.getInstance;

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

    public static void loadvars() {
        try {
            url = getInstance().getConfig().getString("database.url");
            forwardurl = getInstance().getConfig().getString("database.forwardurl");
            port = getInstance().getConfig().getInt("database.port");
            forwardport = getInstance().getConfig().getInt("database.forwardport");
            database = getInstance().getConfig().getString("database.database");
            username = getInstance().getConfig().getString("database.username");
            sshusername = getInstance().getConfig().getString("database.sshusername");
            password = getInstance().getConfig().getString("database.password");
            sshpassword = getInstance().getConfig().getString("database.sshpassword");
            remote = getInstance().getConfig().getBoolean("database.remote");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet localquerry(String querry) {
        Connection con = null;

        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database, username, password);
            Statement stm = con.createStatement();
            rs = stm.executeQuery(querry);
            ProxyServer.getInstance().getLogger().log(Level.INFO, "Successfully initialized database connection!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    /**
        * Remote connection to database by ssh port forwarding.
        * @author mkko120
        */

    public static ResultSet remotequerry(String querry) {
        Session session = null;
        try {
            Connection con = null;
            session = new JSch().getSession(sshusername, url, port);
            session.setPassword(sshpassword);
            session.connect();
            session.setPortForwardingL(forwardport, forwardurl, port);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        Connection con = null;

        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + forwardurl +":" + forwardport + "/" + database, username, password);
            Statement stm = con.createStatement();
            rs = stm.executeQuery(querry);
            ProxyServer.getInstance().getLogger().log(Level.INFO, "Successfully initialized database connection!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
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
        return rs;
    }

    public static void resultUpdate() {
        try {

            ResultSet rs;
            if (remote) {
                rs = remotequerry("SELECT * FROM statistics");
            } else {
                rs = localquerry("SELECT * FROM statistics");
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
