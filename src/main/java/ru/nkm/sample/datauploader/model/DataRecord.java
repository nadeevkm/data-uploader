package ru.nkm.sample.datauploader.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * DataRecord.
 * Класс, представляющий строчку файла и запись в БД.
 *
 * @author Konstantin_Nadeev
 */
@Entity(name = "tdata")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class DataRecord {
    @Id
    Long id;
    String name;
    Double value;
}
