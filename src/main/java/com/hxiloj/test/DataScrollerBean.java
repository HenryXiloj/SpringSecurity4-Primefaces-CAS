package com.hxiloj.test;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
@ManagedBean(name = "dataScrollerBean")
@ViewScoped
public class DataScrollerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Student> students;
    @ManagedProperty("#{studentService}")
    private StudentService studentService;
    @PostConstruct
    public void init() {
    	students = studentService.getStudents();
    }
	public List<Student> getStudents() {
		return students;
	}
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
}