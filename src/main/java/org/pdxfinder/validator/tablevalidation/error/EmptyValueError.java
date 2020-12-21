package org.pdxfinder.validator.tablevalidation.error;

import org.pdxfinder.validator.tablevalidation.ColumnReference;
import tech.tablesaw.api.Table;

public class EmptyValueError implements ValidationError {
  private ColumnReference nonEmptyColumn;
  private Table invalidRows;
  private String provider;

  EmptyValueError(ColumnReference nonEmptyColumn, Table invalidRows, String provider) {
    this.nonEmptyColumn = nonEmptyColumn;
    this.invalidRows = invalidRows;
    this.provider = provider;
  }

  @Override
  public String verboseMessage() {
    return String.format("%s:%n%s", message(), getInvalidRows());
  }

  @Override
  public String message() {
    return String.format(
        "Error in [%s] for provider [%s]: Missing value(s) in required column [%s]",
        nonEmptyColumn.table(), provider, nonEmptyColumn.column());
  }

  private Table getInvalidRows() {
    return invalidRows;
  }

  @Override
  public String toString() {
    return verboseMessage();
  }
}
