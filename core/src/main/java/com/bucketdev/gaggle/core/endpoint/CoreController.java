package com.bucketdev.gaggle.core.endpoint;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import com.bucketdev.gaggle.core.dto.CoreDTO;
import com.bucketdev.gaggle.core.mapper.ICoreMapper;
import com.bucketdev.gaggle.core.service.ICoreService;
import com.bucketdev.gaggle.core.util.PaginationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Timed;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author rodrigo.loyola
 * @param <T> domain entity class
 * @param <DTO> object to be transferred class
 * @param <Key> object which works as identifier
 */
public abstract class CoreController<T extends CoreEntity, DTO extends CoreDTO, Key> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final String baseUri;
    protected final ICoreService<T, DTO, Key> service;
    protected final PagingAndSortingRepository<T, Key> repository;
    protected final ICoreMapper<T, DTO> mapper;
    @Autowired private PaginationUtil paginationUtil;

    /**
     * Constructor -> which assigns the values:
     *
     * @see #service the respective service for the current core entity.
     * @see #mapper The respective mapper of the core service.
     * @see #repository The respective repository for the current core entity.
     * @see #baseUri String required from the requestMapping on springBoot.
     * @param coreService domain service that can be overridden by any entity, and inherit all behavior and capabilities
     */
    public CoreController(ICoreService<T, DTO, Key> coreService) {
        this.baseUri = getClass().getAnnotation(RequestMapping.class).value()[0];
        this.service = coreService;
        this.repository = coreService.getRepository();
        this.mapper = coreService.getMapper();
    }

    /**
     * EP to allow an authenticated user to create an entity of T class
     *
     * @param request HttpServletRequest
     * @param dto object with the values to be persisted
     * @return dto persisted in the database
     * @throws java.net.URISyntaxException exception thrown
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @Transactional
    @ApiOperation(value = "Create a new entity")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was created successfully"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<DTO> create(
        @Valid @RequestBody @ApiParam(value = "DTO entity with the new values", required = true) DTO dto,
        HttpServletRequest request) throws URISyntaxException {
        log.debug("Request on {} to create : {}", baseUri, dto);
        if ((dto.getId() != null) && (!dto.getId().toString().isEmpty())) {
            return ResponseEntity.badRequest().header("Failure","A new entity cannot already have an ID").body(null);
        }
        try {
            DTO createdEntity = service.create(dto);
            return ResponseEntity.created( new URI(baseUri + createdEntity.getId())).body(createdEntity);
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @ApiOperation(value = "Find the entity by a given id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was found"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<DTO> get(
        @PathVariable @ApiParam(value = "Identifier of the requested entity", required = true, example = "1") Key id,
        HttpServletRequest request) {
        log.debug("Request on {} to get id : {}", baseUri, id);
        try {
            DTO dto = service.get(id);
            return dto != null ? new ResponseEntity<>(dto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @ApiOperation(value = "Find the all the entities")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was found"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<Page<DTO>> getAllPaged(
        @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
        HttpServletRequest request) {
        pageable = paginationUtil.getPageableData(request, pageable);
        log.debug("Request on {} to get page : {}", baseUri, pageable.getPageNumber());
        try {
            return new ResponseEntity<>(service.getAllPaged(pageable), HttpStatus.OK);
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @ApiOperation(value = "Find the all the entities")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was found"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<List<DTO>> getAll(
        @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
        HttpServletRequest request) {
        pageable = paginationUtil.getPageableData(request, pageable);
        log.debug("Request on {} to get page : all", baseUri);
        try {
            return new ResponseEntity<>(service.getAll(pageable.getSort()), HttpStatus.OK);
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @Transactional
    @ApiOperation(value = "Update an entity by the given id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was updated"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<DTO> update(
        @PathVariable @ApiParam(value = "Identifier of the entity to be updated", required = true, example = "1") Key id,
        @Valid @RequestBody @ApiParam(value = "Values for the entity to be updated", required = true) DTO dto,
        HttpServletRequest request)
        throws URISyntaxException {
        log.debug("Request on {} to update : {}", baseUri, dto);
        try {
            DTO updatedDTO = service.update(id, dto);
            if (null != updatedDTO) {
                return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(millis = 5000)
    @Transactional
    @ApiOperation(value = "Delete the entity by a given id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The resource was deleted"),
        @ApiResponse(code = 401, message = "Access restricted, only authenticated users are allowed"),
        @ApiResponse(code = 403, message = "Forbidden path for the current user"),
        @ApiResponse(code = 404, message = "URL requested is not available"),
        @ApiResponse(code = 422,
            message = "Un-processable entity, check the integrity and validate data consistence and values"),
        @ApiResponse(code = 500, message = "Internal server error, something went wrong executing the current request")
    })
    public ResponseEntity<DTO> delete(
        @PathVariable @ApiParam(value = "Identifier of the entity to be deleted", required = true, example = "1") Key id,
        HttpServletRequest request) {
        log.debug("Request on {} to delete : {}", baseUri, id);
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Throwable ex) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
