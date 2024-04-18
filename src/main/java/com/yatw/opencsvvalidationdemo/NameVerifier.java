package com.yatw.opencsvvalidationdemo;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class NameVerifier implements BeanVerifier<BuildingDTO> {
    @Override
    public boolean verifyBean(BuildingDTO buildingDTO) throws CsvConstraintViolationException {
        if (buildingDTO.getName() == null){
            throw new CsvConstraintViolationException("Name cannot be null");
        }
        return true;
    }
}
