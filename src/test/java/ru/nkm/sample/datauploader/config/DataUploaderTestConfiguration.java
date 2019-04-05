package ru.nkm.sample.datauploader.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import ru.nkm.sample.datauploader.DataUploaderApplication;

/**
 * TestConfiguration.
 *
 * @author Konstantin_Nadeev
 */
@Configuration
@ComponentScan(
        basePackageClasses = DataUploaderApplication.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommandLineRunner.class))
@EnableAutoConfiguration
public class DataUploaderTestConfiguration {
}
