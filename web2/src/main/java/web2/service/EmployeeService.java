package web2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web2.dao.EmployeeDao;

@Service
public class EmployeeService {

    @Autowired
    EmployeeDao employeeDao;

    public String modifyStatement(String name) {
        return name + " is brave, he has tremendous patience";
    }

    public String getEmployeeNames() {
        return employeeDao.getEmployees();
    }
}
