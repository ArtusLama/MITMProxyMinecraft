package de.artus.packets.fields;


import de.artus.packets.fields.custom.PropertyField;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.lang.reflect.Array;

@NoArgsConstructor
@AllArgsConstructor
public class XArrayField <T extends FieldType<?>> implements FieldType<T[]> {


    @Accessors(chain = true)
    @Getter @Setter
    private T[] value;

    @Accessors(chain = true)
    @Getter @Setter
    private int length;



    // TODO NOT TESTED!!!
    @Override
    public FieldType<T[]> read(ByteBuf stream) throws IOException {
        // create array with length and new instance of T
        T[] array = (T[]) Array.newInstance(value.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            array[i].read(stream);
        }
        setValue(array);

        return this;
    }

    @Override
    public FieldType<T[]> write(ByteBuf stream) throws IOException {

        for (T t : getValue()) {
            t.write(stream);
        }

        return this;
    }
}
