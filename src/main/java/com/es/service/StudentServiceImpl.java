package com.es.service;

import com.es.document.Student;
import com.es.repository.StudentEsRepository;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    RestTemplate restTemplate;

    @Resource(name="highLevelClient") //等同@Qualifier("highLevelClient")
    RestHighLevelClient restHighLevelClient;

    @Resource
    StudentEsRepository studentEsRepository;

    @Value("${elasticSearch.host}")
    String esUrl;


    @Override
    public List<Student> getStudents() {
        return null;
    }

    @Override
    public Student getStudent(Long studentId) {
        return null;
    }

    @Override
    public Student updateStudent(Student student) {
        return null;
    }

    @Override
    public Integer deleteStudent(Long studentId) {
        return null;
    }

    @Override
    public String getStudentByIdFromRest(Long studentId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", studentId);
        String result = restTemplate.getForObject("http://" + esUrl, String.class, params);
        return null;
    }

    @Override
    public String getStudentByIdFromJpa(Long studentId) {
        return null;
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest getRequest = new GetIndexRequest(index);
        getRequest.local(false);
        getRequest.humanReadable(true);
        return restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     * @throws IOException
     */
    public boolean delIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse deleteIndexResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        return deleteIndexResponse.isAcknowledged();
    }

}
