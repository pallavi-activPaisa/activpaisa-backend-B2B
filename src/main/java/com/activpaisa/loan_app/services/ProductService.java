package com.activpaisa.loan_app.services;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getColumnNames() {
        List<String> columns = new ArrayList<>();

        jdbcTemplate.query("SELECT * FROM products LIMIT 1", (ResultSetExtractor<Void>) rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnName(i));
            }
            return null;
        });

        return columns;
    }
}
