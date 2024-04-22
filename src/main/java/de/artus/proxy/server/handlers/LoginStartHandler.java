package de.artus.proxy.server.handlers;

import de.artus.packets.c2s.LoginStartPacket;
import de.artus.packets.fields.ByteArrayField;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.UUIDField;
import de.artus.packets.fields.VarIntField;
import de.artus.packets.s2c.EncryptionRequestPacket;
import de.artus.proxy.server.ClientConnection;
import de.artus.util.network.PlayerInfo;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class LoginStartHandler extends PacketHandler<LoginStartPacket> {


    @Override
    public void onPacket(LoginStartPacket packet, ClientConnection client) {

        log.trace("Received LoginStartPacket!");
        log.trace("Playername: {}", packet.getPlayername().getValue());

        client.setPlayerInfo(
                new PlayerInfo(packet.getPlayername().getValue(), packet.getPlayerUUID().map(UUIDField::getValue))
        );

        log.trace("Generating encryption request...");
        KeyPair serverKeyPair = client.getServerKeyPair();
        byte[] publicKey = serverKeyPair.getPublic().getEncoded();

        byte[] verify = new byte[4];
        ThreadLocalRandom.current().nextBytes(verify);

        EncryptionRequestPacket encryptionRequestPacket = new EncryptionRequestPacket(
                new StringField(""),
                new VarIntField(publicKey.length),
                new ByteArrayField(publicKey, publicKey.length),
                new VarIntField(verify.length),
                new ByteArrayField(verify, verify.length)
        );

        log.trace("Sending encryption request...");
        client.getChannel().writeAndFlush(encryptionRequestPacket);





    }



}
