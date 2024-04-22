package de.artus.netty;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.VarIntField;
import de.artus.proxy.MinecraftVersion;
import de.artus.proxy.server.ClientConnection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PacketDecoder extends ByteToMessageDecoder {


    @Getter
    private final PacketRegistry.PacketDirection direction;

    @Getter
    private final ClientConnection connection;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!ctx.channel().isActive() || !in.isReadable()) {
            return;
        }

        int packetLength = new VarIntField().read(in).getValue();
        byte packetId = in.readByte();

        log.info("Incoming packet with id: 0x" + Integer.toHexString(packetId) + " and length: " + packetLength + " [State: " + getConnection().getState() + "]");
        Packet packet = PacketRegistry.createPacket(packetId, packetLength - 1, getDirection(), getConnection().getState()); // -1 because the packet id is already read
        packet.read(in, getDirection(), getConnection().getVersion());

        log.trace(in.readableBytes() + " bytes left in buffer.");
        out.add(packet);
    }
}
