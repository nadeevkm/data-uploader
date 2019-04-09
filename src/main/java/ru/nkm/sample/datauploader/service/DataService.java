package ru.nkm.sample.datauploader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.nkm.sample.datauploader.model.DataRecord;
import ru.nkm.sample.datauploader.model.dto.DataPageDto;
import ru.nkm.sample.datauploader.repository.DataRepository;

/**
 * DataService.
 *
 * @author Konstantin_Nadeev
 */
@Service
@Slf4j
public class DataService {

    private static final String SEARCH_FIELD = "name";

    DataRepository repository;

    @Autowired
    public DataService(DataRepository repository) {
        this.repository = repository;
    }

    public DataPageDto findAll(String name, Pageable pageable) {
        log.info("START findAll - name: {}, pageable: {}", name, pageable);
        Specification<DataRecord> specification = getDataRecordSpec(name);
        Page<DataRecord> dataPage = repository.findAll(specification, pageable);
        DataPageDto result = new DataPageDto(
                dataPage.getContent(),
                dataPage.getTotalElements());
        log.info("END findAll() - result = {}", result);
        return result;
    }

    /**
     * Метод для формирования спецификации поиска в таблице(like) по колонке SEARCH_FIELD
     * @param value значение для поиска
     * @return спецификация для поиска
     */
    private Specification<DataRecord> getDataRecordSpec(String value) {
        log.info("START getDataRecordSpec() - value: {}", value);
        if(value == null){
            return null;
        }
        log.info("END getDataRecordSpec()");
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(SEARCH_FIELD)),
                containsLowerCase(value)
        );
    }

    private String containsLowerCase(String searchField) {
        return "%" + searchField.toLowerCase() + "%";
    }
}
