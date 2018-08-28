package ru.innopolis.stc12.io;

public class Main {
    public static void main(String[] args) {
        EmployeeList employeeList = new EmployeeList();
        Employee employee1 = new Employee("Tom", 35, 35050.5, Job.Administrator);
        Employee employee2 = new Employee("Alex", 25, 45050.5, Job.Economist);
        Employee employee3 = new Employee("Monica", 27, 25050.5, Job.Economist);
        Employee employee4 = new Employee("Rose", 29, 55050.5, Job.Programmer);
        Employee employee5 = new Employee("Jon", 31, 155050.5, Job.Director);

        employeeList.save(employee1);
        employeeList.save(employee2);
        employeeList.save(employee3);
        employeeList.save(employee4);
        employeeList.save(employee5);

        System.out.println(employeeList);

        employeeList.delete(employee1);
        System.out.println(employeeList);

        System.out.println(employeeList.getByName("Alex"));
        System.out.println(employeeList.getByName("Jon"));
        System.out.println(employeeList.getByName("Tom"));

        System.out.println(employeeList.getByJob(Job.Economist));
        System.out.println(employeeList.getByJob(Job.Programmer));
        System.out.println(employeeList.getByJob(Job.Accountant));

        System.out.println(employeeList.changeAllWork(Job.Economist, Job.Programmer));
        System.out.println(employeeList.getByJob(Job.Programmer));


    }
}
