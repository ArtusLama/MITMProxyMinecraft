package de.artus.proxy.server.handlers;

import de.artus.packets.c2s.StatusRequestPacket;
import de.artus.packets.fields.StringField;
import de.artus.packets.s2c.StatusResponsePacket;
import de.artus.proxy.server.ClientConnection;
import de.artus.util.network.ServerListStatus;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;

@Slf4j
public class StatusRequestHandler extends PacketHandler<StatusRequestPacket> {


    @Override
    public void onPacket(StatusRequestPacket packet, ClientConnection client) {
        log.trace("Received Server list ping!");


        ServerListStatus x =
                new ServerListStatus(
                        new ServerListStatus.Version(
                                "Every Version",
                                client.getVersion().getProtocolVersion()
                        ),
                        false,
                        false,
                        Component.text("Artus Proxy :D"),
                        new ServerListStatus.Players(
                                20,
                                1,
                                new ServerListStatus.Player[]{
                                        new ServerListStatus.Player("§aDeine Mom", "00000000-0000-0000-0000-000000000000")
                                }
                        ),
                        "data:image/png;base64,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"
                );



        client.sendPacket(
            new StatusResponsePacket(new StringField(x.toJsonString()))
        );


    }


}
