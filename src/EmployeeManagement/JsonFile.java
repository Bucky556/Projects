package EmployeeManagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class JsonFile {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Register the adapter
            .create();
    private static final Path path = Path.of("resources/employees.json");

    public static List<Employee> loadEmployees() throws IOException {
        String jsonData = Files.readString(path);
        Type type = new TypeToken<List<Employee>>() {}.getType();
        return gson.fromJson(jsonData, type);
    }
}
