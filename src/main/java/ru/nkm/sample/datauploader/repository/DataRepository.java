package ru.nkm.sample.datauploader.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nkm.sample.datauploader.model.DataRecord;

/**
 * DataRepository.
 *
 * @author Konstantin_Nadeev
 */
@Repository
public interface DataRepository extends CrudRepository<DataRecord, Long>, JpaSpecificationExecutor<DataRecord> {
}
