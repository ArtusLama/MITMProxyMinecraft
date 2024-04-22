package de.artus.packets.fields;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import java.io.IOException;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class JsonTextComponentField implements FieldType<Component> {


    @Accessors(chain = true)
    private Component value;

    @Override
    public JsonTextComponentField read(ByteBuf stream) throws IOException {
        String jsonString = new StringField().read(stream).getValue();

        log.trace("Trying to deserialize TextComponent: {}", jsonString);
        setValue(GsonComponentSerializer.gson().deserialize(jsonString));
        return this;
    }

    @Override
    public JsonTextComponentField write(ByteBuf stream) throws IOException {
        String jsonString = GsonComponentSerializer.gson().serialize(getValue());
        log.trace("Writing TextComponent: {}", jsonString);
        new StringField(jsonString).write(stream);
        return this;
    }
}
