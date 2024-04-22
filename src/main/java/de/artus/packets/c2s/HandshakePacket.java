package de.artus.packets.c2s;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.UShortField;
import de.artus.packets.fields.VarIntField;
import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.io.IOException;

@Getter
public class HandshakePacket extends Packet {

    private VarIntField protocolVersion = new VarIntField();
    private StringField serverAddress = new StringField();
    private UShortField port = new UShortField();
    private VarIntField nextState = new VarIntField();


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection dir, MinecraftVersion version) throws IOException {
        getProtocolVersion().read(stream);
        getServerAddress().read(stream);
        getPort().read(stream);
        getNextState().read(stream);

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection dir, MinecraftVersion version) throws IOException {
        getProtocolVersion().write(stream);
        getServerAddress().write(stream);
        getPort().write(stream);
        getNextState().write(stream);
    }
}
