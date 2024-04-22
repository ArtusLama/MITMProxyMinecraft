package de.artus.proxy.server;

import de.artus.netty.PacketDecoder;
import de.artus.netty.PacketEncoder;
import de.artus.netty.PacketListener;
import de.artus.packets.PacketRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final ProxyServer proxyServer;

    @Override
    protected void initChannel(@NotNull SocketChannel ch) throws Exception {
        log.info("New client connection!");
        getProxyServer().getClients().add(
                new ClientConnection(getProxyServer(), ch).init()
        );
    }

}
