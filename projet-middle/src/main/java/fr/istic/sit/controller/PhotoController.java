package fr.istic.sit.controller;

import com.mongodb.gridfs.GridFSDBFile;
import fr.istic.sit.domain.Photo;
import fr.istic.sit.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Search photos of an intervention", nickname = "PhotosOfIntervention", response = GridFSDBFile.class )
    public List<Photo> getPhoto(OAuth2Authentication authentication, @RequestParam(value = "idIntervention" , required = true) String idIntervention,
                                @RequestParam(value = "latitude" , required = false) String latitude,
                                @RequestParam(value = "longitude" , required = false) String longitude){
        return service.getPhotos(idIntervention, latitude, longitude);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Photo from ID", nickname = "PhotoOfIntervention", response = InputStreamResource.class )
    @ResponseBody public ResponseEntity<InputStreamResource> getImage( @PathVariable String id) {
        GridFSDBFile gridFsFile = service.getPhotoById(id);

        return ResponseEntity.ok()
                .contentLength(gridFsFile.getLength())
                .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
                .body(new InputStreamResource(gridFsFile.getInputStream()));
    }


    @RequestMapping(value = "/intervention/{idIntervention}", method = RequestMethod.POST)
    @ApiOperation(value = "Set photo of an intervention", nickname = "NewPhotoOfIntervention" )
    public Photo insertPhotoIntervention(OAuth2Authentication authentication, @RequestParam("file") MultipartFile file, @PathVariable String idIntervention,
                                           @RequestParam (value = "latitude" , required = true) String latitude,
                                           @RequestParam (value = "longitude" , required = true) String longitude){
        if (!file.isEmpty()) {
            return service.savePhoto(idIntervention,latitude, longitude, file);
        }

       return null;
    }
}
