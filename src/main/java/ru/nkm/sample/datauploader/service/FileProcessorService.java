package ru.nkm.sample.datauploader.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.nkm.sample.datauploader.model.DataRecord;
import ru.nkm.sample.datauploader.repository.DataDAO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * FileProcessorService.
 * Сервис загрузки обработки и загрузки файлов в БД,
 *
 * @author Konstantin_Nadeev
 */
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileProcessorService {

    static final String ID = "id";
    static final String NAME = "name";
    static final String VALUE = "value";

    DataDAO dataDAO;

    @Value("${fileProcessor.batchSize}")
    Integer batchSize;

    @Value("${fileProcessor.inputDirectory}")
    String inputDirectory; //папка с входными данными
    String getInputDirectory() {
        return inputDirectory;
    }

    @Value("${fileProcessor.processedDirectory}")
    String processedDirectory; //папка с обработанными файлами

    @Value("${fileProcessor.errorsDirectory}")
    String errorsDirectory; //папка с файлами, обработанными с ошибкой

    @Autowired
    public FileProcessorService(DataDAO dataDAO) {
        this.dataDAO = dataDAO;
    }

    /**
     * Метод загрузки данных из папки в БД
     */
    public void processData() {
        File inputFolder = new File(inputDirectory);
        if (!inputFolder.isDirectory()) {
            log.error("Incorrect inputDirectory");
            return;
        }
        for (File file : inputFolder.listFiles()) {
            processFileSafely(file.toPath());
        }
    }

    /**
     * Метод-оберка над processFile для сокращения бойлерплейта обработки IOException
     *
     * @param pathToFile path к обрабатываемому файлу
     */
    public void processFileSafely(Path pathToFile) {
        log.info("START processFileSafely, pathToFile = {}", pathToFile);
        try {
            processFile(pathToFile);
        } catch (IOException e) {
            log.error("IOException while processing file: {}", e);
        }
        log.info("END processFileSafely, pathToFile = {}", pathToFile);
    }

    /**
     * Метод обработки файло и загрузки данных в БД.
     *
     * @param pathToFile path к файлу
     * @throws IOException обрабатывается в методе-обертке
     */
    private void processFile(Path pathToFile) throws IOException {
        log.info("START processFile, pathToFile = {}", pathToFile);
        List<DataRecord> dataRecords = new ArrayList<>(batchSize);
        Reader in = new FileReader(pathToFile.toFile());
        CSVParser records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        try {
            for (CSVRecord record : records) {
                dataRecords.add(getDataRecordFromCSVRecord(record));
                if (dataRecords.size() == batchSize || records.getCurrentLineNumber() == record.size()) {
                    dataDAO.insertBatch(dataRecords);
                    dataRecords.clear();
                }
            }
            Files.move(pathToFile, Paths.get(processedDirectory).resolve(pathToFile.getFileName()));
        } catch (DataAccessException e) {
            log.error("DataAccessException while processing file: {}", e.toString());
            Files.move(pathToFile, Paths.get(errorsDirectory).resolve(pathToFile.getFileName()));
        }
        log.info("END processFile");
    }

    /**
     * Создание DataRecord на основе CSVRecord получаемой из CSV-файла
     *
     * @param record - CSVRecord получаемый из CSV-файла
     * @return DataRecord
     */
    private DataRecord getDataRecordFromCSVRecord(CSVRecord record) {
        return new DataRecord(
                Long.parseLong(record.get(ID)),
                record.get(NAME),
                Double.parseDouble(record.get(VALUE)));
    }

}
