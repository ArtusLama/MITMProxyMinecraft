package de.artus.util.network.auth;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter @Setter
@AllArgsConstructor
public class GameProfile {


    private final String id;
    private final String name;
    private final List<Property> properties;

    public UUID getUUID() {
        BigInteger bi1 = new BigInteger(getId().substring(0, 16), 16);
        BigInteger bi2 = new BigInteger(getId().substring(16, 32), 16);
        return new UUID(bi1.longValue(), bi2.longValue());
    }

    @Getter @Setter
    @AllArgsConstructor
    public static final class Property {

        private final String name;
        private final String value;
        private final String signature;
    }

    public static GameProfile fromJson(String body) {
        return new Gson().fromJson(body, GameProfile.class);
    }
}
