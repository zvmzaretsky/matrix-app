package com.zvm.matrix;

import java.util.Map;

public class Matrix {

    private final Map<String, Integer[]> rows;

    public Matrix(Map<String, Integer[]> rows) {
        this.rows = rows;
    }

    public Map<String, Integer[]> getRows() {
        return rows;
    }
}
