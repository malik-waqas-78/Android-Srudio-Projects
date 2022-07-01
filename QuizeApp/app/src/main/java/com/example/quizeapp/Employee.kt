package com.example.quizeapp

class Employee {

        var empId = 0
        var empName: String? = null
        var empDepartment: String? = null

        constructor() {}
        constructor(empName: String?, empPhoneNo: String?) {
            this.empName = empName
            this.empDepartment = empPhoneNo
        }

}