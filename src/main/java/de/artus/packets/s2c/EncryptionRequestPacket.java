package de.artus.packets.s2c;

import de.artus.packets.Packet;
import de.artus.packets.PacketRegistry;
import de.artus.packets.fields.ByteArrayField;
import de.artus.packets.fields.StringField;
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
public class EncryptionRequestPacket extends Packet {

    private StringField serverId = new StringField(); // appears to be empty


    private VarIntField publicKeyLength = new VarIntField();
    private ByteArrayField publicKey = new ByteArrayField();

    private VarIntField verifyTokenLength = new VarIntField();
    private ByteArrayField verifyToken = new ByteArrayField();


    @Override
    public Packet read(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getServerId().read(stream);
        getPublicKeyLength().read(stream);
        getPublicKey().setLength(getPublicKeyLength().getValue()).read(stream);
        getVerifyTokenLength().read(stream);
        getVerifyToken().setLength(getVerifyTokenLength().getValue()).read(stream);

        return this;
    }

    @Override
    public void write(ByteBuf stream, PacketRegistry.PacketDirection direction, MinecraftVersion version) throws IOException {
        getServerId().write(stream);
        getPublicKeyLength().write(stream);
        getPublicKey().write(stream);
        getVerifyTokenLength().write(stream);
        getVerifyToken().write(stream);
    }
}
