package com.furious.util.db;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;

@Getter
class Row {
    private final Set<Column<?>> columns = new TreeSet<>(Comparator.comparing(Column::getField));
}
