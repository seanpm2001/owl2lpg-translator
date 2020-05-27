package edu.stanford.owl2lpg.exporter.csv;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;

import static com.google.common.base.Preconditions.checkNotNull;

public class CsvWriter<T> {

    @Nonnull
    private final Class<T> cls;

    @Nonnull
    private final Writer output;

    @Nonnull
    private final CsvMapper csvMapper;

    private boolean writtenHeader = false;

    private ObjectWriter objectWriter;


    public CsvWriter(@Nonnull CsvMapper csvMapper,
                     @Nonnull Class<T> cls,
                     @Nonnull Writer output) {
        this.csvMapper = checkNotNull(csvMapper);
        this.cls = checkNotNull(cls);
        this.output = checkNotNull(output);
    }

    public void write(@Nonnull T rowObject) throws IOException {
        if(writtenHeader) {
            writeRow(rowObject);
        }
        else {
            writeFirstRow(rowObject);
        }
    }

    private void writeFirstRow(@Nonnull T rowObject) throws IOException {
        objectWriter = csvMapper.writer(csvMapper.schemaFor(cls).withHeader());
        objectWriter.writeValues(output).write(rowObject);
        objectWriter = csvMapper.writer(csvMapper.schemaFor(cls));
        writtenHeader = true;
    }

    private void writeRow(@Nonnull T rowObject) throws IOException {
        objectWriter.writeValue(output, rowObject);
    }
}
