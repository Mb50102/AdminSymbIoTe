package hr.fer.tel.pipp.admin.services;

import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import hr.fer.tel.pipp.admin.dto.ResourceDTO;
import hr.fer.tel.pipp.admin.model.Resource;
import hr.fer.tel.pipp.admin.model.Room;
import hr.fer.tel.pipp.admin.repositories.ResourceRepository;
import hr.fer.tel.pipp.admin.repositories.RoomRepository;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RoomRepository roomRepository;


    public List<ResourceDTO> getAllResources(){
        return resourceRepository.findAll().stream().map( res -> new ResourceDTO(res)).collect(Collectors.toList());
    }

    public Function<ResourceDTO, Resource> mapper = resDTO -> {
        Resource res = new Resource();

        res.setInternalId(resDTO.getInternalId());
        res.setRoutingKey(resDTO.getRoutingKey());
        res.setResourceName(resDTO.getResourceName());
        res.setRoom(roomRepository.getOne(resDTO.getRoomName()));

        return res;
    };

    private void validate(Resource resource){
        Assert.notNull(resource.getInternalId(), "Iternal ID ne smije biti null");
        Assert.notNull(resource.getResourceName(), "Resource name ne smije biti null");
        Assert.notNull(resource.getRoom(), "Room ne smije biti prazan");

        String resourceName = resource.getResourceName();
        boolean exist = resourceRepository.existsById(resourceName);
        if(exist) throw new IllegalArgumentException("Resource '"+ resourceName + "' vec postoji");

        String internalId = resource.getInternalId();
        boolean existByInternalId =resourceRepository.existByInternalId(internalId);
        if(existByInternalId) throw new IllegalArgumentException("Resource sa InternalId: '"+ internalId + "' vec postoji");

        String routingKey= resource.getRoutingKey();
        boolean existByRoutingKey =resourceRepository.existByRoutingKey(routingKey);
        if(existByRoutingKey) throw new IllegalArgumentException("Resource sa routingKey: '"+ routingKey + "' vec postoji");


    }

    public void createResource(ResourceDTO resourceDTO){
        Resource resource = mapper.apply(resourceDTO);
        validate(resource);
        resourceRepository.save(resource);
    }

    public void updateResource(ResourceDTO resourceDTO) throws SecurityHandlerException, AAMException {
        String resourceName = resourceDTO.getResourceName();

        if( !(resourceRepository.existsById(resourceName))){
            throw new IllegalArgumentException("Takav resurs ne postoji "+resourceName);
        }

        if( !(roomRepository.existsById(resourceDTO.getRoomName()))){
            throw new IllegalArgumentException("Ta soba ne postoji " + resourceDTO.getRoomName());
        }

        Resource newResource = resourceRepository.getOne(resourceName);
        newResource.setInternalId(resourceDTO.getInternalId());
        newResource.setRoutingKey(resourceDTO.getRoutingKey());
        newResource.setRoom(roomRepository.getOne(resourceDTO.getRoomName()));
        resourceRepository.save(newResource);
    }

    public void deleteResource(String resourceName) throws SecurityHandlerException, AAMException {
        Resource resource = resourceRepository.getOne(resourceName);
        resourceRepository.delete(resource);
    }
}
