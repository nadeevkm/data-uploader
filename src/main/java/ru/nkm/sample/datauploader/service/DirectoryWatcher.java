package ru.nkm.sample.datauploader.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * DirectoryWatcher.
 * Класс сервиса мониторинга папки входящих файлов.
 *
 * @author Konstantin_Nadeev
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectoryWatcher implements Runnable {

    FileProcessorService fileProcessorService;
    String inputDirectory;

    @Autowired
    public DirectoryWatcher(FileProcessorService fileProcessorService) {
        this.fileProcessorService = fileProcessorService;
        inputDirectory = fileProcessorService.getInputDirectory();
    }

    @Override
    public void run() {
        try {
            runWatchService();
        } catch (IOException e) {
            log.error("IOException during directory watching {}", e);
        }
    }

    /**
     * Запуск сервиса мониторинга для отслеживания событий, типа "создание файлов"
     * @throws IOException
     */
    private void runWatchService() throws IOException {
        log.info("START runWatchService");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(inputDirectory);
        if (!path.toFile().isDirectory()) {
            log.error("Incorrect inputDirectory");
            return;
        }
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            if (!processEvents(path, watchService)) {
                break;
            }
        }
        log.info("END runWatchService");
    }

    /**
     * Обработка событий, отлсеживаемых в папке
     * @param path отслеживаемая папка
     * @param watchService вотчер, следящий за данной папкой
     * @return false если возникла непредвиденная ситуация, true при нормальной обработке
     */
    private boolean processEvents(Path path, WatchService watchService) {
        log.info("START processEvents");
        WatchKey key;
        try {
            key = watchService.take();
        } catch (InterruptedException x) {
            log.info("END processEvents");
            return false;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            log.info("Event kind: {}. File affected: {}", kind, event.context());
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }

            Path filename = path.resolve(((WatchEvent<Path>) event).context());
            fileProcessorService.processFileSafely(filename);
        }

        log.info("END processEvents");
        return key.reset();
    }
}
