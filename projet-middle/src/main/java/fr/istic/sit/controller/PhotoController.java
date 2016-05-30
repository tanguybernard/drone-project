package fr.istic.sit.controller;

import com.mongodb.gridfs.GridFSDBFile;
import fr.istic.sit.domain.Drone;
import fr.istic.sit.domain.Photo;
import fr.istic.sit.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FireDroneTeam
 */
@RestController
@RequestMapping("/photo")
@Api(value = "photo", description = "Photo API", produces = "application/json")
public class PhotoController {

    @Autowired
    PhotoService  service;

   /* @RequestMapping(method = RequestMethod.POST)
    public boolean insertIntervention(OAuth2Authentication authentication, @RequestBody Intervention intervention){
        return service.insert(intervention);
    }*/

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Set photo of an intervention", nickname = "PhotosOfIntervention" )
    public boolean insertPhoto(OAuth2Authentication authentication, @RequestBody Photo photo){
        service.insertPhoto(photo);
        return true;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Search photos of an intervention", nickname = "PhotosOfIntervention", response = GridFSDBFile.class )
    public List<Photo> getPhoto(OAuth2Authentication authentication, @RequestParam(value = "idIntervention" , required = false) String idIntervention){
        //return service.getPhoto(idIntervention);
        return service.getPhotos(idIntervention);
    }
}
