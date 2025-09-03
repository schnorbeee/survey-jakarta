package com.dynata.surveyhw.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.ejb.Stateless;

import java.io.InputStream;
import java.util.List;

@Stateless
public class CsvService {

    public <T> List<T> readFromCsv(InputStream file, Class<T> clazz) {
        try (InputStream inputStream = file) {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<T> iterator = mapper.readerFor(clazz)
                    .with(schema)
                    .readValues(inputStream);
            return iterator.readAll();
        } catch (Exception e) {
            throw new RuntimeException("Error at reading " + clazz + " CSV file: " + e.getMessage(), e);
        }
    }
}
