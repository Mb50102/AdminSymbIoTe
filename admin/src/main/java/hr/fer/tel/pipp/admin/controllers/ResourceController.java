package hr.fer.tel.pipp.admin.controllers;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import hr.fer.tel.pipp.admin.dto.ResourceDTO;
import hr.fer.tel.pipp.admin.model.Resource;
import hr.fer.tel.pipp.admin.services.ResourceService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.server.PathParam;
import java.util.List;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/resources")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public List<ResourceDTO> getAllResources(){
        return resourceService.getAllResources();
    }

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> createResource(@RequestBody ResourceDTO resourceDTO) {
        resourceService.createResource(resourceDTO);
        return ResponseEntity.ok("Resurs " + resourceDTO.getResourceName() +" je kreiran");
    }

    @PutMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateResource(@RequestBody ResourceDTO resourceDTO) throws SecurityHandlerException, AAMException {
        resourceService.updateResource(resourceDTO);
        return ResponseEntity.ok("Resurs " + resourceDTO.getResourceName() +" je promijenjen");
    }

    @DeleteMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteResource(@PathParam("resourceName") String resourceName) throws SecurityHandlerException, AAMException {
        resourceService.deleteResource(resourceName);
        return ResponseEntity.ok("Resurs " + resourceName +" je obrisan");
    }

}
