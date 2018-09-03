package ru.innopolis.stc12.io;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListProxy implements InvocationHandler {
    private final String FILE_NAME = "employees.xml";
    private MySerialization mySerialization;

    public EmployeeListProxy(MySerialization mySerialization) {
        this.mySerialization = mySerialization;
    }

    private boolean readXml() {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        List<Employee> employeeList = new ArrayList<>();
        try {
            XMLEventReader eventReader = inputFactory.createXMLEventReader(new FileInputStream(FILE_NAME));
            Employee employee = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    String elementName = event.asStartElement().getName().getLocalPart();
                    if (elementName.compareTo("employee") == 0) {
                        employee = new Employee();
                    }
                    switch (elementName) {
                        case "name":
                            event = eventReader.nextEvent();
                            employee.setName(event.asCharacters().getData());
                            break;
                        case "age":
                            event = eventReader.nextEvent();
                            employee.setAge(Integer.parseInt(event.asCharacters().getData()));
                            break;
                        case "salary":
                            event = eventReader.nextEvent();
                            employee.setSalary(Double.parseDouble(event.asCharacters().getData()));
                            break;
                        case "job":
                            event = eventReader.nextEvent();
                            employee.setJob(Job.valueOf(event.asCharacters().getData()));
                            break;
                    }
                }

                if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().compareTo("employee") == 0) {
                        employeeList.add(employee);
                    }
                }
            }
            Field field = mySerialization.getClass().getDeclaredField("employeeList");
            field.setAccessible(true);
            field.set(mySerialization, employeeList);

        } catch (XMLStreamException | FileNotFoundException | NullPointerException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private boolean writeXml() {
        try {
            Field field = mySerialization.getClass().getDeclaredField("employeeList");
            field.setAccessible(true);
            List<Employee> employeeList = (List<Employee>) field.get(mySerialization);//TODO instanceof?

            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_NAME));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");

            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            eventWriter.add(eventFactory.createStartElement("", "", "items"));
            eventWriter.add(end);

            for (Employee employee : employeeList) {
                eventWriter.add(eventFactory.createStartElement("", "", "employee"));
                eventWriter.add(end);

                createNode(eventWriter, "name", employee.getName());
                createNode(eventWriter, "age", String.valueOf(employee.getAge()));
                createNode(eventWriter, "salary", String.valueOf(employee.getSalary()));
                createNode(eventWriter, "job", employee.getJob().toString());

                eventWriter.add(eventFactory.createEndElement("", "", "employee"));
                eventWriter.add(end);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "items"));
            eventWriter.add(end);

            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (NoSuchFieldException | IllegalAccessException | FileNotFoundException | XMLStreamException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        StartElement startElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(startElement);

        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        EndElement endElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(endElement);
        eventWriter.add(end);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mySerialization.getClass().getAnnotation(UseXml.class) != null) {
            if (method.getName().compareTo("readFromFile") == 0) {
                return readXml();
            }
            if (method.getName().compareTo("saveToFile") == 0) {
                return writeXml();
            }
            return null;
        } else {
            return method.invoke(mySerialization, args);
        }
    }
}
