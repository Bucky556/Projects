package EmployeeManagement;

import java.io.IOException;
import java.util.List;

public class EmployeeRepository {
    private final List<Employee> employees;

    public EmployeeRepository() throws IOException {
        this.employees = JsonFile.loadEmployees();
    }

    public List<Employee> findAll() {
        return employees;
    }
}
