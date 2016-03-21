package fr.istic.sit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.istic.sit.dao.UserRepository;
import fr.istic.sit.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fracma on 3/14/16.
 */

//@SpringBootApplication
public class SampleCnxnMongo implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SampleCnxnMongo.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (User user : repository.findAll()) {
            System.out.println(user);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByFirstname("Alice"));



        DBObject metaData = new BasicDBObject();
        metaData.put("extra1", "anything 1");
        metaData.put("extra2", "anything 2");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/Users/fracma/master2/SIT/test.jpg");
            gridFsTemplate.store(inputStream, "test.jpg", "image/jpg", metaData);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");
    }


}