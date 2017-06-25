package semexe.tables.serialize;

import com.google.common.collect.Iterators;
import semexe.tables.StringNormalizationUtils;
import semexe.basic.LogInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Read a table in either CSV or TSV format.
 * <p>
 * For CSV, this class is just a wrapper for OpenCSV.
 * Escape sequences for CSV:
 * - \\         => \
 * - \" or ""   => "
 * Each cell can be quoted inside "...". Embed newlines must be quoted.
 * <p>
 * For TSV, each line must represent one table row (no embed newlines).
 * Escape sequences for TSV (custom):
 * - \n   => [newline]
 * - \\   => \
 * - \p   => |
 *
 * @author ppasupat
 */
public class TableReader implements Closeable, Iterable<String[]> {

    CSVParser csvReader = null;
    List<String[]> tsvData = null;
    public TableReader(String filename) throws IOException {
        switch (guessDataType(filename)) {
            case CSV:
                csvReader = new CSVParser(new FileReader(filename), CSVFormat.DEFAULT);
                break;
            case TSV:
                parseTSV(filename);
                break;
            default:
                throw new RuntimeException("Unknown data type for " + filename);
        }
    }

    public static void main(String[] args) {
        String filename = "t/csv/200-csv/0.tsv";
        LogInfo.logs("%s", filename);
        try (TableReader tableReader = new TableReader(filename)) {
            for (String[] x : tableReader) {
                LogInfo.begin_track("ROW");
                for (String y : x) LogInfo.logs("|%s|", y);
                LogInfo.end_track();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DataType guessDataType(String filename) {
        if (filename.endsWith(".csv"))
            return DataType.CSV;
        else if (filename.endsWith(".tsv"))
            return DataType.TSV;
        // Guess from the first line of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line.contains("\t"))
                return DataType.TSV;
            else if (line.contains(",") || line.startsWith("\""))
                return DataType.CSV;
        } catch (IOException e) {
            throw new RuntimeException("Unknown data type for " + filename);
        }
        return DataType.UNKNOWN;
    }

    private void parseTSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            tsvData = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t", -1);     // Include trailing spaces
                for (int i = 0; i < fields.length; i++)
                    fields[i] = StringNormalizationUtils.unescapeTSV(fields[i]);
                tsvData.add(fields);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<String[]> iterator() {
        if (csvReader != null) return Iterators.transform(
                csvReader.iterator(), x -> Stream.of(x.iterator())
                        .map(Object::toString)
                        .toArray(String[]::new)
        );
        else
            return tsvData.iterator();
    }

    @Override
    public void close() throws IOException {
        if (csvReader != null) csvReader.close();
    }

    // ============================================================
    // Test
    // ============================================================

    enum DataType {CSV, TSV, UNKNOWN}

}
