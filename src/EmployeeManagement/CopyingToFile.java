package EmployeeManagement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CopyingToFile {
    public static void toFile(List<Employee> employees){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sorting Employees' by fullName:\n");
        employees.stream().sorted((k,v) -> k.fullName().compareToIgnoreCase(v.fullName())).forEach(e -> stringBuilder.append(e).append("\n"));

        double averageSalary = employees.stream().mapToDouble(Employee::salary).average().orElse(0);
        stringBuilder.append("\nGetting average salary of Employees").append(averageSalary).append("\n");

        stringBuilder.append("\nGrouping by Employees' department");
        Map<String, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::department));
        collect.forEach((d,e) -> stringBuilder.append(d).append("::").append(e).append("\n"));

        writeToFile("employees_data.txt",stringBuilder.toString());
    }

    public static void writeToFile(String fileName, String content){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
            bufferedWriter.write(content);
            System.out.println("Data transferred to file:" + fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
