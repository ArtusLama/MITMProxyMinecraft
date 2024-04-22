package de.artus.proxy.server.handlers;

import de.artus.packets.c2s.PingRequestPacket;
import de.artus.packets.s2c.PingResponsePacket;
import de.artus.proxy.server.ClientConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingRequestHandler extends PacketHandler<PingRequestPacket> {


    @Override
    public void onPacket(PingRequestPacket packet, ClientConnection client) {
        log.trace("Received ping request from client!");
        client.sendPacket(
                new PingResponsePacket(packet.getPayload())
        );
        client.disconnect();
    }


}
