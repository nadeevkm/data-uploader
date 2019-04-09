package ru.nkm.sample.datauploader.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nkm.sample.datauploader.model.dto.DataPageDto;
import ru.nkm.sample.datauploader.service.DataService;

/**
 * DataController.
 *
 * @author Konstantin_Nadeev
 */
@RestController
@RequestMapping("/data-records")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DataController {

    DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataPageDto findAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        log.info("START findAll - name: {}, pageable: {}", name, pageable);
        log.info("START findAll - name: {}, pageable: {}", name, pageable);
        return dataService.findAll(name, pageable);
    }
}
