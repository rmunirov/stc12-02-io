package ru.innopolis.stc12.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeList {

    private final String FILE_NAME = "employees.txt";
    private List<Employee> employeeList = new ArrayList();


    public EmployeeList() {
        init();
    }

    private void init() {
        readFromFile();
    }

    private boolean saveToFile() {
        boolean result = false;
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(employeeList);
            result = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private boolean readFromFile() {
        boolean result = false;

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employeeList = (ArrayList<Employee>) objectInputStream.readObject();
            result = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean save(Employee employee) {
        return saveOrUpdate(employee);
    }

    public boolean delete(Employee employee) {
        boolean result;
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

        boolean result;

        Employee foundedEmployee = getByName(employee.getName());

        if (foundedEmployee != null && employee.getName().compareTo(foundedEmployee.getName()) == 0) {
            int index = employeeList.indexOf(foundedEmployee);
            employeeList.set(index, employee);
        } else {
            employeeList.add(employee);
        }

        result = saveToFile();

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
        return "EmployeeList{" +
                "employeeList=" + employeeList +
                '}';
    }
}
