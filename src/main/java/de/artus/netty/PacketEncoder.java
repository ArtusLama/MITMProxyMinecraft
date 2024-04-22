package de.artus.netty;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.VarIntField;
import de.artus.proxy.MinecraftVersion;
import de.artus.proxy.server.ClientConnection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Getter
    private final PacketRegistry.PacketDirection direction;

    @Getter
    private final ClientConnection connection;


    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        ByteBuf dataStream = Unpooled.buffer();

        dataStream.writeByte(PacketRegistry.getIdOfPacket(msg));
        msg.write(dataStream, getDirection(), getConnection().getVersion());

        new VarIntField(dataStream.readableBytes()).write(out);
        out.writeBytes(dataStream);




    }


}
