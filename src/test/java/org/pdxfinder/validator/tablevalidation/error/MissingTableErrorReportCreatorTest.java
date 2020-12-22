package org.pdxfinder.validator.tablevalidation.error;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.pdxfinder.validator.tablevalidation.TableSetSpecification;
import tech.tablesaw.api.Table;

public class MissingTableErrorReportCreatorTest {

  private Map<String, Table> completeTableSet = new HashMap<>();
  private Map<String, Table> incompleteTableSet = new HashMap<>();
  private final String TABLE_1 = "table_1.tsv";
  private final String PROVIDER = "PROVIDER-BC";

  private Set<String> minimalRequiredTable = Stream.of(TABLE_1).collect(Collectors.toSet());

  private TableSetSpecification requireTable =
      TableSetSpecification.create().setProvider(PROVIDER).addRequiredTables(minimalRequiredTable);

  private Map<String, Table> makeCompleteTableSet() {
    Map<String, Table> completeFileSet = new HashMap<>();
    minimalRequiredTable.forEach(s -> completeFileSet.put(s, Table.create()));
    return completeFileSet;
  }

  private Map<String, Table> makeIncompleteTableSet() {
    Map<String, Table> incompleteFileSet = new HashMap<>(completeTableSet);
    incompleteFileSet.remove(TABLE_1);
    return incompleteFileSet;
  }

  private MissingTableErrorCreator missingTableErrorCreator = new MissingTableErrorCreator();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    completeTableSet = makeCompleteTableSet();
    incompleteTableSet = makeIncompleteTableSet();
  }

  @Test
  public void passesValidation_givenEmptyFileSet_failsValidation() {
    Map<String, Table> emptyHashMap = new HashMap<>();
    assertThat(
        missingTableErrorCreator.generateErrors(emptyHashMap, requireTable).isEmpty(), is(false));
  }

  @Test
  public void passesValidation_givenEmptyFileSetAndEmptySpecification_passesValidation() {
    Map<String, Table> emptyHashMap = new HashMap<>();
    TableSetSpecification emptyTableSetSpecification = TableSetSpecification.create();
    assertThat(
        missingTableErrorCreator.generateErrors(emptyHashMap, emptyTableSetSpecification).isEmpty(),
        is(true));
  }

  @Test
  public void passesValidation_givenIncompleteFileSet_failsValidation() {
    List<ValidationError> expected =
        Collections.singletonList(missingTableErrorCreator.create(TABLE_1, PROVIDER));
    assertEquals(
        expected.toString(),
        missingTableErrorCreator.generateErrors(incompleteTableSet, requireTable).toString());
  }

  @Test
  public void passesValidation_givenCompleteFileSet_passesValidation() {
    assertThat(
        missingTableErrorCreator.generateErrors(completeTableSet, requireTable).isEmpty(),
        is(true));
  }

  @Test
  public void passesValidation_givenExtraFileInFileSet_passesValidation() {
    Map<String, Table> completeFileSetPlusOne = new HashMap<>(completeTableSet);
    completeFileSetPlusOne.put("extra-table.tsv", Table.create());
    assertThat(
        missingTableErrorCreator.generateErrors(completeFileSetPlusOne, requireTable).isEmpty(),
        is(true));
  }

  @Test
  public void validate_givenCompleteFileSet_producesEmptyErrorList() {
    assertThat(
        missingTableErrorCreator.generateErrors(completeTableSet, requireTable).isEmpty(),
        is(true));
  }

  @Test
  public void validate_givenIncompleteFileSet_addsErrorWithCorrectContextToErrorList() {
    List<ValidationError> expected =
        Collections.singletonList(missingTableErrorCreator.create(TABLE_1, PROVIDER));
    assertEquals(
        expected.toString(),
        missingTableErrorCreator.generateErrors(incompleteTableSet, requireTable).toString());
  }
}
