package manager.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(dtf));
            return;
        }
        jsonWriter.nullValue();
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() != null) {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
        jsonReader.nextNull();
        return null;
    }
}
