package com.hxiloj.test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
@ManagedBean(name = "studentService")
@ApplicationScoped
public class StudentService implements Serializable {
	private static final long serialVersionUID = 1L;
	static List<Student> list = new ArrayList<Student>();
	static{
		for (int i= 1; i<=100; i++){
			list.add(new Student(i, "Student-"+i, "Location-"+i));
		}
	}
	public List<Student> getStudents(){
		return list;
	}
}
