package fr.istic.sit.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

    public String setPhoto( MultipartFile file, String idIntervention, String latitude, String longitude) {
        DBObject metaData = new BasicDBObject();
        metaData.put("idIntervention", idIntervention);
        metaData.put("latitude", latitude);
        metaData.put("longitude", longitude);

        //TODO Manejo de excepciones
        InputStream inputStream = null;
        try {
            inputStream =file.getInputStream();
            GridFSFile imgSave = gridFsTemplate.store(inputStream, file.getName(), "image/jpg", metaData);
            return imgSave.getId().toString();
        } catch (IOException  e ) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    return null;
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

        List<GridFSDBFile> files = gridFsTemplate.find(query);
        
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


    public GridFSDBFile getPhotoById(String idPhoto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(idPhoto));
        query.limit(1);
        List<GridFSDBFile> files = gridFsTemplate.find(query);

        log.info("getPhotoById " + files.size());
        return files.get(0);
    }

   public Photo savePhoto(String idIntervention, String latitude, String longitude, MultipartFile file){

       String idImage= setPhoto(file, idIntervention, latitude, longitude);
       if(idImage != null){
           Photo photo = new Photo();
           photo.setDate((new Date()).toString());
           photo.setIdIntervention(idIntervention);
           photo.setLatitude(new Double(latitude));
           photo.setLongitude(new Double(longitude));
           photo.setImageURL("http://m2gla-drone.istic.univ-rennes1.fr:8080/photo/"+idImage);
           return insertPhoto(photo);
       }
       return null;
    }
}