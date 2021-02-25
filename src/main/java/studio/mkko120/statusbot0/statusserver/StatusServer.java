package studio.mkko120.statusbot0.statusserver;

public class StatusServer {

    private String name;
    private boolean isOnline;
    private int online;
    private int max;

    public StatusServer(String name, boolean isOnline){
        this.name = name;
        this.isOnline = isOnline;
    }

    public StatusServer(String name, boolean isOnline, int online, int max) {
        this.name = name;
        this.isOnline = isOnline;
        this.online = online;
        this.max = max;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    public int getOnline(){
        return this.online;
    }

    public void setOnline(int online){
        this.online = online;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
