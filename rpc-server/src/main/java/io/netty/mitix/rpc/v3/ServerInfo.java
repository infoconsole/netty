package io.netty.mitix.rpc.v3;

/**
 * @author oldflame-jm
 * @create 2018/12
 * @since
 */
public class ServerInfo {
    private String host;
    private String port;

    public void setHost(String host) {
        this.host = host;
    }


    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "{" +
                "host:'" + host + ",port:'" + port + "}";
    }
}
