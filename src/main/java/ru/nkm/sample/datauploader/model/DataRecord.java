package ru.nkm.sample.datauploader.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DataRecord.
 * Класс, представляющий строчку файла.
 *
 * @author Konstantin_Nadeev
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DataRecord {
    Long id;
    String name;
    Double value;
}
