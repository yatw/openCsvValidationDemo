package com.yatw.opencsvvalidationdemo;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.bean.exceptionhandler.ExceptionHandlerQueue;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.List;

@SpringBootTest
class OpencsvvalidationdemoApplicationTests {

	@Test
	void mytest() throws IOException {
		/**
		 * Sample file
		 * ID,Building Name,Country
		 * 12345,,
		 * 12345,testBuilding2,USA
		 *
		 * Sample validation check non null for every column
		 * Expectation: csvToBean.getCapturedExceptions() return all errors from the same row, for every row
		 */
		InputStream fileStream = new ClassPathResource("apr11.csv").getInputStream();
		Reader reader = new BufferedReader(new InputStreamReader(fileStream));


		HeaderColumnNameMappingStrategy<BuildingDTO> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(BuildingDTO.class);


		CsvToBean<BuildingDTO> csvToBean = new CsvToBeanBuilder<BuildingDTO>(reader)
				.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
				.withMappingStrategy(strategy)
				.withVerifier(new CountryVerifier())
				.withVerifier(new NameVerifier())
				.withVerifier(null)
				.withThrowExceptions(false)
				.build();

		List<BuildingDTO> buildings = csvToBean.parse();
		System.out.println("This is buildings");
		System.out.println(buildings);
		List<CsvException> exceptions = csvToBean.getCapturedExceptions();
		System.out.println("This is exceptions");
		System.out.println(exceptions);
//		This is buildings
//				[BuildingDTO(id=null, name=testBuilding2, country=USA)]
//		This is exceptions
//				[com.opencsv.exceptions.CsvConstraintViolationException: Country cannot be null]
		reader.close();
	}

}
