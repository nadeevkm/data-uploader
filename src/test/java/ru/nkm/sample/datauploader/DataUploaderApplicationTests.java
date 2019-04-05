package ru.nkm.sample.datauploader;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nkm.sample.datauploader.config.DataUploaderTestConfiguration;
import ru.nkm.sample.datauploader.service.FileProcessorService;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataUploaderTestConfiguration.class)
@TestPropertySource(locations = "classpath:test.properties")
public class DataUploaderApplicationTests {

    @Value("${fileProcessor.inputDirectory}")
    String inputDirectory;

    @Value("${fileProcessor.processedDirectory}")
    String processedDirectory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FileProcessorService subj;

    @Autowired
    ApplicationContext ctx;

    String fileName;

    @Before
    @SneakyThrows
    public void initTestFile() {
        fileName = UUID.randomUUID() + ".csv";
        FileWriter outputfile = new FileWriter(inputDirectory + "/" + fileName);
        CSVPrinter printer = new CSVPrinter(outputfile, CSVFormat.DEFAULT);
        String[] header = {"id", "name", "value"};
        printer.printRecord(header);
        String[] data1 = {"1", "BBB", "1.613"};
        printer.printRecord(data1);
        String[] data2 = {"2", "CCC", "2.614"};
        printer.printRecord(data2);
        printer.close();
    }

    @Test
    @SneakyThrows
    public void contextLoads() {
        subj.processData();
        String sql = "SELECT count(*) FROM TDATA";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        Assert.assertEquals(2, count);
        Assert.assertTrue(Files.walk(Paths.get(processedDirectory))
                .anyMatch(p -> p.getFileName().toString().equals(fileName)));
    }
}
