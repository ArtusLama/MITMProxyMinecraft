package de.artus.packets.c2s;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.ByteArrayField;
import de.artus.packets.fields.VarIntField;
import de.artus.proxy.MinecraftVersion;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionResponsePacket extends Packet {


    private VarIntField sharedSecretLength = new VarIntField();
    private ByteArrayField sharedSecret = new ByteArrayField();
    private VarIntField verifyTokenLength = new VarIntField();
    private ByteArrayField verifyToken = new ByteArrayField();


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getSharedSecretLength().read(stream);
        getSharedSecret().setLength(getSharedSecretLength().getValue()).read(stream);
        getVerifyTokenLength().read(stream);
        getVerifyToken().setLength(getVerifyTokenLength().getValue()).read(stream);

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getSharedSecretLength().write(stream);
        getSharedSecret().write(stream);
        getVerifyTokenLength().write(stream);
        getVerifyToken().write(stream);
    }
}
