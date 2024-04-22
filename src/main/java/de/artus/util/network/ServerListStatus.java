package de.artus.util.network;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import java.lang.reflect.Type;

@Setter
@Slf4j
@AllArgsConstructor
public class ServerListStatus {

    @Getter
    private Version version;
    @Getter private boolean enforcesSecureChat;
    @Getter private boolean previewsChat;
    @Getter private TextComponent description; // create/use TextComponents

    @Getter private Players players;
    @Getter private String favicon;



    @AllArgsConstructor
    public static class Version {
        @Getter
        private String name;
        @Getter
        private int protocol;
    }

    @AllArgsConstructor
    public static class Players {
        @Getter
        private int max;
        @Getter
        private int online;
        @Getter
        private Player[] sample;
    }
    @AllArgsConstructor
    public static class Player {
        @Getter
        private String name;
        @Getter
        private String id;
    }

    public String toJsonString() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(TextComponent.class, (JsonSerializer<Object>) (src, typeOfSrc, context) -> GsonComponentSerializer.gson().serializeToTree((TextComponent) src));

        String out = gson.create().toJson(this);
        log.trace("Created ServerListStatus string: {}", out);
        return out;
    }
    public static ServerListStatus fromJsonString(String json) {
        ServerListStatus serverListStatus = new Gson().fromJson(json, ServerListStatus.class);
        serverListStatus.description = (TextComponent) GsonComponentSerializer.gson().deserialize(json);
        log.trace("Created ServerListStatus from string: {}", json);
        return serverListStatus;
    }


}
