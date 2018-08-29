package ru.innopolis.stc12.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyCustomEmployeeList {

    private final String FILE_NAME = "custom_employees.txt";
    private List<Employee> employeeList = new ArrayList<>();

    public MyCustomEmployeeList() {
        readFromFile();
    }

    private boolean saveToFile() {
        if (employeeList.isEmpty()) return false;
        boolean result = false;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employeeList) {
                String saveData = employee.getName() + ',' + employee.getAge() + ',' + employee.getSalary() + ',' + employee.getJob();
                bufferedWriter.write(saveData);
                bufferedWriter.newLine();
            }
            result = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private boolean readFromFile() {
        boolean result = false;

        employeeList.clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String parts[] = line.split(",");
                Employee employee = new Employee();
                int paramIndex = 0;
                // TODO bad solution
                for (String param : parts) {
                    switch (paramIndex) {
                        case 0:
                            employee.setName(param);
                            break;
                        case 1:
                            employee.setAge(Integer.parseInt(param));
                            break;
                        case 2:
                            employee.setSalary(Double.parseDouble(param));
                            break;
                        case 3:
                            employee.setJob(Job.valueOf(param));
                            break;
                        default:
                            break;
                    }
                    paramIndex++;
                }
                employeeList.add(employee);
                result = true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public boolean save(Employee employee) {
        return saveOrUpdate(employee);
    }

    public boolean delete(Employee employee) {
        boolean result = false;

        result = employeeList.remove(employee); // TODO maybe need set when file is saving

        saveToFile();

        return result;
    }

    public Employee getByName(String name) {
        if (name == null) return null; // TODO what better, returned value or execute exception?

        Employee result = null;
        for (Employee employee : employeeList) {
            if (employee.getName().compareTo(name) == 0) {
                result = employee;
                break;
            }
        }

        return result;
    }

    public List<Employee> getByJob(Job job) {
        if (job == null) return new ArrayList<>();

        List<Employee> result = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getJob().compareTo(job) == 0) {
                result.add(employee);
            }
        }

        return result;
    }

    public boolean saveOrUpdate(Employee employee) {
        if (employee == null) return false;

        if (employee.getName() == null) return false;

        if (employee.getJob() == null) return false;

        Employee foundedEmployee = getByName(employee.getName());

        if (foundedEmployee != null && employee.getName().compareTo(foundedEmployee.getName()) == 0) {
            int index = employeeList.indexOf(foundedEmployee);
            employeeList.set(index, employee);
        } else {
            employeeList.add(employee);
        }

        boolean result = saveToFile();

        return result;
    }

    public boolean changeAllWork(Job from, Job to) {
        if (from == null || to == null) return false;

        boolean result = false;

        for (Employee employee : employeeList) {
            if (employee.getJob().compareTo(from) == 0) {
                employee.setJob(to);
                result = true;
            }
        }

        saveToFile();

        return result;
    }

    @Override
    public String toString() {
        return "MyCustomEmployeeList{" +
                "employeeList=" + employeeList +
                '}';
    }
}
