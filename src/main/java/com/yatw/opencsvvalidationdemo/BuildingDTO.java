package com.yatw.opencsvvalidationdemo;


import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BuildingDTO {
    @CsvBindByName(column = "ID")
    private String id;

    @CsvBindByName(column = "Building Name")
    private String name;

    @CsvBindByName(column = "Country")
    private String country;
}
