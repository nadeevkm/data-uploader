package ru.nkm.sample.datauploader.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.nkm.sample.datauploader.model.DataRecord;

import java.util.List;

/**
 * DataPageDto.
 *
 * @author Konstantin_Nadeev
 */
@Getter
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataPageDto {

    /**
     * Список записей.
     */
    List<DataRecord> data;

    /**
     * Общее количество записей.
     */
    long total;
}

