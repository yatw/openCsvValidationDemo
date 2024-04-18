package com.yatw.opencsvvalidationdemo;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class CountryVerifier implements BeanVerifier<BuildingDTO> {
    @Override
    public boolean verifyBean(BuildingDTO buildingDTO) throws CsvConstraintViolationException {
        if (buildingDTO.getCountry() == null){
            throw new CsvConstraintViolationException("Country cannot be null");
        }
        return true;
    }
}