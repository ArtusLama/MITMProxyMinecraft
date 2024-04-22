package de.artus.util.network;

import de.artus.packets.fields.JsonTextComponentField;
import de.artus.packets.s2c.DisconnectPacket;
import net.kyori.adventure.text.Component;

public class DisconnectPacketHelper {


    public static DisconnectPacket sendReason(String reason) {
        return new DisconnectPacket(
                new JsonTextComponentField(
                        Component.text(reason)
                )
        );
    }


}
