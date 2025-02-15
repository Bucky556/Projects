package EmployeeManagement;


import lombok.Cleanup;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) throws IOException {
        try {
            EmployeeService employeeService = new EmployeeService(new EmployeeRepository());

            System.out.println("Sorting Employees by fullName:");
            List<Employee> byFullName = employeeService.getEmployeesByFullName();
            byFullName.forEach(System.out::println);

            System.out.println("Getting average salary of employees:");
            double averageSalary = employeeService.getAverageSalary();
            System.out.println(averageSalary);

            System.out.println("Grouping by Employees' department:");
            Map<String, List<Employee>> employeesByGroupDepartment = employeeService.getEmployeesByGroupDepartment();
            employeesByGroupDepartment.forEach((k, v) -> System.out.println(k + "::" + v));///////////////////////////

            System.out.println("Filtering Employees' department:");
            List<Employee> employeeList = employeeService.filterByDepartment("Online Media");
            employeeList.forEach(System.out::println);

            List<Employee> loadedEmployees = JsonFile.loadEmployees();
            @Cleanup
            FileWriter fileWriter = new FileWriter("employeeData.txt");
            try {
                for (Employee loadedEmployee : loadedEmployees) {
                    fileWriter.write(loadedEmployee + System.lineSeparator());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Data of Employees transferred to file!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Employee> employees = JsonFile.loadEmployees();
        toFile(employees);
    }

    public static void toFile(List<Employee> employees) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sorting Employees' by fullName -> \n");
        employees.stream().sorted((k, v) -> k.fullName().compareToIgnoreCase(v.fullName())).forEach(e -> stringBuilder.append(e).append("\n"));

        double averageSalary = employees.stream().mapToDouble(Employee::salary).average().orElse(0);
        stringBuilder.append("\nGetting average salary of Employees -> ").append(averageSalary).append("\n");

        stringBuilder.append("\nGrouping by Employees' department -> ");
        Map<String, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::department));
        collect.forEach((d, e) -> stringBuilder.append(d).append("::").append(e).append("\n"));

        String department = "Online Media";
        filterByDepartment(stringBuilder,employees,department);

        writeToFile("employees_data.txt", stringBuilder.toString());
    }

    public static void writeToFile(String fileName, String content) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(content);
            System.out.println("Data transferred to file:" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void filterByDepartment(StringBuilder report, List<Employee> employees, String department) {
        report.append("\nFiltering Employees by department -> ").append(department).append("\n");

        List<Employee> filteredEmployees = employees.stream()
                .filter(e -> e.department().equalsIgnoreCase(department))
                .toList();

        if (filteredEmployees.isEmpty()) {
            report.append("No employees found in this department.\n");
        } else {
            System.out.println("Git push");
            filteredEmployees.forEach(e -> report.append(e).append("\n"));
        }
    }
}
