package com.example.demo2.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents () {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Correo Usado");
        }

        studentRepository.save(student);
        
    }

    public void deleteStudent(Long studentId) {
        
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException("Estudiante con id "+ studentId+ " no existe");
        }
        studentRepository.deleteById(studentId);
    }
    @Transactional
    public void updateStudent(Long studentId, String name, String email){

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

        if(name != null && name.length()>0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
            Optional<Student> studentOptional = studentRepository.findByEmail(email); 
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email usado");
            }

            student.setEmail(email);
        }


    }

}
