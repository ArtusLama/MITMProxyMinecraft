package de.artus.packets.fields.custom;

import de.artus.packets.fields.BooleanField;
import de.artus.packets.fields.FieldType;
import de.artus.packets.fields.StringField;
import de.artus.packets.fields.custom.obj.Property;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class PropertyField implements FieldType<Property> {

    @Accessors(chain = true)
    @Getter @Setter
    private Property value;

    @Override
    public FieldType<Property> read(ByteBuf stream) throws IOException {
        setValue(new Property(
                new StringField().read(stream).getValue(),
                new StringField().read(stream).getValue(),
                new BooleanField().read(stream).getValue()
        ));
        if (getValue().getIsSigned()) {
            getValue().setSignature(Optional.of(new StringField().read(stream).getValue()));
        }

        return this;
    }

    @Override
    public FieldType<Property> write(ByteBuf stream) throws IOException {
        new StringField(getValue().getName()).write(stream);
        new StringField(getValue().getValue()).write(stream);
        new BooleanField(getValue().getIsSigned()).write(stream);
        if (getValue().getIsSigned() && getValue().getSignature().isPresent()) {
            new StringField(getValue().getSignature().get()).write(stream);
        }


        return this;
    }
}
