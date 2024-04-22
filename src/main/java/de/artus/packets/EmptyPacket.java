package de.artus.packets;

import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class EmptyPacket extends Packet {

    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {

    }
}
