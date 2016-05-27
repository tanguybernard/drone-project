package fr.istic.sit.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import fr.istic.sit.dao.PhotoRepository;
import fr.istic.sit.domain.Photo;
import fr.istic.sit.util.Validator;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fracma on 5/26/16.
 */

@Component
public class PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoService.class);


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PhotoRepository photoRepository;

    public void setPhoto() {
        DBObject metaData = new BasicDBObject();
        metaData.put("idIntervention", "571f7731b760adc0c3bec8fb");
        metaData.put("latitude", "48.115127");
        metaData.put("longitude", "-1.637972");
        //metaData.put("idDrone", "anything 2");

        //,

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
    }

    public List<Photo> getPhoto(String idIntervention) {
        DBObject metaData = new BasicDBObject();
        metaData.put("idIntervention", "anything 1");
        metaData.put("localisation", "anything 2");


        List<Photo> photo = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("metadata.idIntervention").is(idIntervention));
        //query.limit(1);
        //query.with(new Sort(Sort.Direction.DESC, "uploadDate"));

        //return gridFsTemplate.find(buildMetadataSearchQuery(new BasicDBObject("target_field", "abcdefg")));

        //List<GridFSDBFile> files = gridFs.find(buildMetadataSearchQuery(new BasicDBObject("target_field", "abcdefg")));

        //new InputStreamResource(gridFsFile.getInputStream())
        List<GridFSDBFile> files = gridFsTemplate.find(query);

        log.info("Photos " + files.size());

     /*   files.forEach(f -> {
            try {
                photo.add(new Photo(IOUtils.toByteArray(f.getInputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/

        log.info("Photos " + photo.size());
        return photo;
    }


    protected static void toDottedJson(Object o, String key, DBObject query) {
        if (o instanceof Map)
            for (Map.Entry<?, ?> c : ((Map<?, ?>) o).entrySet())
                toDottedJson(c.getValue(), key + "." + c.getKey().toString(),
                        query);
        else
            query.put(key, o.toString());
    }

    public static DBObject buildMetadataSearchQuery(DBObject searchQuery) {
        BasicDBObject metadatSearchQuery = new BasicDBObject();
        for (Map.Entry<?, ?> c : ((Map<?, ?>) searchQuery).entrySet())
            toDottedJson(c.getValue(), "metadata."
                            + c.getKey().toString(),
                    metadatSearchQuery);
        return metadatSearchQuery;
    }

    public List<Photo> getPhotos(String idIntervention) {
        if(!Validator.isEmpty(idIntervention))
            return photoRepository.findByIdIntervention(idIntervention);

        return photoRepository.findAll();
    }

    public Photo insertPhoto(Photo photo){
        return photoRepository.save(photo);
    }
}