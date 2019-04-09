package ru.nkm.sample.datauploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import ru.nkm.sample.datauploader.model.DataRecord;
import ru.nkm.sample.datauploader.service.DirectoryWatcher;
import ru.nkm.sample.datauploader.service.FileProcessorService;

@SpringBootApplication
@Slf4j
@EntityScan(basePackageClasses = {DataRecord.class})
public class DataUploaderApplication implements CommandLineRunner {

    private FileProcessorService fileProcessorService;
    private DirectoryWatcher directoryWatcher;

    @Autowired
    public DataUploaderApplication(FileProcessorService fileProcessorService, DirectoryWatcher directoryWatcher) {
        this.fileProcessorService = fileProcessorService;
        this.directoryWatcher = directoryWatcher;
    }

    public static void main(String[] args) {
        SpringApplication.run(DataUploaderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        fileProcessorService.processData();
        //В отдельном потоке запускаем сервис-мониторинга папки входящих файлов
        new Thread(directoryWatcher).start();
    }
}
