package com.es.repository;

import com.es.document.Student;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EsRepository {

    @Autowired
    private StudentEsRepository studentEsRepository;

    @Test
    public void testSave() {
        Student student = new Student();
        student.setName("Weison");
        student.setAge(10);
        student.setVersion(1L);
        student.setAddressId(11L);
        Student save = studentEsRepository.save(student);
        TestCase.assertNotNull(save);
    }


}