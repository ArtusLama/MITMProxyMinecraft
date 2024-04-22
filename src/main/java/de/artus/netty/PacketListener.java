package de.artus.netty;

import de.artus.packets.Packet;
import de.artus.proxy.server.ClientConnection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
@RequiredArgsConstructor
public class PacketListener extends ChannelInboundHandlerAdapter {

    @Getter
    private final ClientConnection connection;


    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof Packet packet) {
            getConnection().onPacketReceived(packet);
            ctx.newSucceededFuture();
        } else {
            log.warn("Received unknown message: " + msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Exception caught in PacketListener", cause);
        log.warn("Closing connection...");
        getConnection().disconnect();
    }
}
