package de.artus.proxy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class ProxyServer {

    @Getter
    private final int port;

    @Getter
    private List<ClientConnection> clients = new ArrayList<>();

    @Getter @Setter
    private EventLoopGroup workerGroup;


    public void run() {


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        setWorkerGroup(new NioEventLoopGroup());
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .channelFactory(NioServerSocketChannel::new)
                    .group(bossGroup, getWorkerGroup())
                    .childHandler(new ClientChannelInitializer(this))
                    .localAddress(getPort());

            ChannelFuture f = b.bind().addListener(
                    (ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("Server successfully started on port " + getPort());
                        } else {
                            log.error("Server failed to start on port " + getPort());
                        }
                    }
            ).sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


}
