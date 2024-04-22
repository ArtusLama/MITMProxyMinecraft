package de.artus.packets.fields.custom.obj;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Property {


    @NonNull private String name;
    @NonNull private String value;
    @NonNull private Boolean isSigned;
    private Optional<String> signature = Optional.empty();


}
