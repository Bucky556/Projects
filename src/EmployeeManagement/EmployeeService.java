package EmployeeManagement;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployeesByFullName() {
        return employeeRepository.findAll().stream()
                .filter(e -> e.fullName() != null)
                .sorted(Comparator.comparing(Employee::fullName,Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public double getAverageSalary() {
        return employeeRepository.findAll().stream().mapToDouble(Employee::salary).average().orElse(0);
    }

    public Map<String, List<Employee>> getEmployeesByGroupDepartment() {
        return employeeRepository.findAll().stream().collect(Collectors.groupingBy(Employee::department));
    }

    public List<Employee> filterByDepartment(String department) {
        return employeeRepository.findAll().stream().filter(e -> e.department().equalsIgnoreCase(department)).toList();
    }

}
