package de.artus.proxy.server.handlers;

import de.artus.packets.c2s.EncryptionResponsePacket;
import de.artus.proxy.server.ClientConnection;
import de.artus.util.network.EncryptionUtils;
import de.artus.util.network.auth.MojangSessionHelper;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;


@Slf4j
public class EncryptionResponseHandler extends PacketHandler<EncryptionResponsePacket> {
    @Override
    public void onPacket(EncryptionResponsePacket packet, ClientConnection client) {

        log.trace("Received EncryptionResponsePacket!");


        // Leaving out these whole checks, because I'm too lazy to implement them :D
        try {

            KeyPair serverKeyPair = client.getServerKeyPair();

            byte[] decryptedSharedSecret = EncryptionUtils.decryptRsa(serverKeyPair, packet.getSharedSecret().getValue());
            String serverId = EncryptionUtils.generateServerId(decryptedSharedSecret, serverKeyPair.getPublic());

            MojangSessionHelper.authPlayer(client, client.getPlayerInfo().getUsername(), serverId, (gameProfile) -> {
                client.setGameProfile(gameProfile);
                client.enableEncryption();
                client.triggerLoginSuccess();
            });


        } catch (Exception e) {
            log.error("Failed to enable encryption!", e);
            client.disconnect("Failed to enable encryption!");
        }

    }
}
