package de.artus.packets;

import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;

public class UnknownPacket extends Packet {


    @Accessors(chain = true)
    @Getter @Setter
    private int id;

    @Accessors(chain = true)
    @Getter @Setter
    private int length = 0;

    @Getter @Setter
    private byte[] data;

    @Accessors(chain = true)
    @Getter @Setter
    private PacketRegistry.PacketDirection direction;


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        setDirection(direction);

        byte[] bytes = new byte[getLength()];
        stream.readBytes(bytes);
        setData(bytes);

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        stream.writeBytes(getData());
    }
}
