package EmployeeManagement;

import java.time.LocalDate;

public record Employee(int id, int age, String fullName, String department, LocalDate hireDate, double salary) {
}
