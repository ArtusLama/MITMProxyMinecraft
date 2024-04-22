package de.artus.proxy;

import de.artus.proxy.client.ProxyClient;
import de.artus.proxy.server.ProxyServer;
import de.artus.util.network.SRVLookup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Proxy {

    @Getter
    private final int localPort;

    @Getter @Setter @NonNull
    private ServerAddress targetServer;

    @Getter
    private final String username;

    public Proxy(int localPort, ServerAddress targetServer, String username) {
        this.localPort = localPort;
        this.targetServer = SRVLookup.lookup(targetServer);
        this.username = username;
    }


    public Proxy start() {
        log.info("Starting proxy on port {}! Target server: {}", getLocalPort(), getTargetServer());
        log.info("localhost:{} <=> {}", getLocalPort(), getTargetServer());

        ProxyServer server = new ProxyServer(getLocalPort());
        ProxyClient client = new ProxyClient();

        server.run();



        return this;
    }
}
