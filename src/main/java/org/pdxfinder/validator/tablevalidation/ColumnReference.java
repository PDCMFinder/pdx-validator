package org.pdxfinder.validator.tablevalidation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ColumnReference {

  private String tableName;
  private String columnName;

  private ColumnReference(String tableName, String columnName) {
    this.tableName = tableName;
    this.columnName = columnName;
  }

  public static ColumnReference of(String tableName, String columnName) {
    return new ColumnReference(tableName, columnName);
  }

  public String table() {
    return this.tableName;
  }

  public String column() {
    return this.columnName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ColumnReference that = (ColumnReference) o;

    return new EqualsBuilder()
        .append(tableName, that.tableName)
        .append(columnName, that.columnName)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(tableName).append(columnName).toHashCode();
  }

  @Override
  public String toString() {
    return String.format("%s > %s", table(), column());
  }
}
