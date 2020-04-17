package com.furious.util.db;

import lombok.Data;

@Data
class Column<T> {
    private String field;
    private int type;
    private T value;
}
