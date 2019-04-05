package ru.nkm.sample.datauploader.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.nkm.sample.datauploader.DataUploaderApplication;
import ru.nkm.sample.datauploader.model.DataRecord;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * DataDAO.
 * DAO для занесения DataRecord в БД
 *
 * @author Konstantin_Nadeev
 */
@Repository
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataDAO {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public DataDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Пакетная загрузка списка DataRecord.
     * @param dataList - список DataRecord.
     */
    public void insertBatch(List<DataRecord> dataList){
        log.info("START insertBatch, dataList.size = {}", dataList.size());

        String sql = "INSERT INTO tdata " +
                "(ID, NAME, VALUE) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DataRecord dataRecord = dataList.get(i);
                ps.setLong(1, dataRecord.getId());
                ps.setString(2, dataRecord.getName());
                ps.setDouble(3, dataRecord.getValue() );
            }
            @Override
            public int getBatchSize() {
                return dataList.size();
            }
        });
        log.info("END insertBatch");
    }
}
