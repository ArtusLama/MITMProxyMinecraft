package de.artus.packets;

import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class Packet {


    public abstract Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException;
    public abstract void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException;


}
