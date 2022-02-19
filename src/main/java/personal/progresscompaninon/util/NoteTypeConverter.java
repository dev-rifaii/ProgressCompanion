package personal.progresscompaninon.util;

import personal.progresscompaninon.model.NoteType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;
@Converter(autoApply = true)

public class NoteTypeConverter implements AttributeConverter<NoteType,String> {
    @Override
    public  String convertToDatabaseColumn(NoteType noteType) {
        if (noteType == null) {
            return null;
        }
        return noteType.getCode();
    }

    @Override
    public NoteType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(NoteType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
