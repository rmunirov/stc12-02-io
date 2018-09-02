package ru.innopolis.stc12.io;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EmployeeListProxy implements InvocationHandler {
    private final String FILE_NAME = "employees.xml";
    private MySerialization mySerialization;

    public EmployeeListProxy(MySerialization mySerialization) {
        this.mySerialization = mySerialization;
    }

    private boolean readXml() {
        System.out.println("read from xml file");
        return true;
    }

    private boolean writeXml() {
        System.out.println("write to xml file");
        return true;
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
