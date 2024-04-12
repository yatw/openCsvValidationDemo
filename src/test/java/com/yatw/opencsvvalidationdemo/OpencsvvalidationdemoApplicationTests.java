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
		 * ,testBuilding1,
		 * 12345,testBuilding2,
		 *
		 * Sample validation check non null for every column
		 * Expectation: csvToBean.getCapturedExceptions() return all errors from the same row, for every row
		 */
		InputStream fileStream = new ClassPathResource("apr11.csv").getInputStream();
		Reader reader = new BufferedReader(new InputStreamReader(fileStream));


		HeaderColumnNameMappingStrategy<BuildingDTO> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(BuildingDTO.class);

		CsvExceptionHandler exceptionHandler = new ExceptionHandlerQueue();

		BeanVerifier<BuildingDTO> verifier = buildingDTO -> {

			try{
				if (buildingDTO.getId() == null){
					CsvConstraintViolationException e = new CsvConstraintViolationException("ID is required");
					exceptionHandler.handleException(e);
				}
				if (buildingDTO.getName() == null){
					CsvConstraintViolationException e = new CsvConstraintViolationException("Name is required");
					exceptionHandler.handleException(e);
				}
				if (buildingDTO.getCountry() == null){
					CsvConstraintViolationException e = new CsvConstraintViolationException("Country is required");
					exceptionHandler.handleException(e);
				}
			}catch (CsvException e){
				System.out.println("Error here");
			}


			return true;
		};

		CsvToBean<BuildingDTO> csvToBean = new CsvToBeanBuilder<BuildingDTO>(reader)
				.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
				.withVerifyReader(true)
				.withMappingStrategy(strategy)
				.withVerifier(verifier)
				.withExceptionHandler(new ExceptionHandlerQueue())
				.withThrowExceptions(false)
				.build();

		csvToBean.setThrowExceptions(false);
		List<BuildingDTO> buildings = csvToBean.parse();
		System.out.println("This is buildings");
		System.out.println(buildings);
		List<CsvException> exceptions = csvToBean.getCapturedExceptions();
		System.out.println("This is exceptions");
		System.out.println(exceptions);
		//This is buildings
		//[BuildingDTO(id=null, name=testBuilding1, country=null), BuildingDTO(id=null, name=testBuilding2, country=null)]
		//This is exceptions
		//[]
		reader.close();
	}

}
