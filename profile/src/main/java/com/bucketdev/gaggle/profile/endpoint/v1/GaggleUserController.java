package com.bucketdev.gaggle.profile.endpoint.v1;

import com.bucketdev.gaggle.core.endpoint.CoreController;
import com.bucketdev.gaggle.core.service.ICoreService;
import com.bucketdev.gaggle.profile.domain.GaggleUser;
import com.bucketdev.gaggle.profile.dto.GaggleUserDTO;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rodrigo.loyola
 */
@RestController
@RequestMapping(value = "user")
@Api(consumes = MediaType.APPLICATION_JSON_VALUE, value = "EP to consume user resources")
public class GaggleUserController extends CoreController<GaggleUser, GaggleUserDTO, Long> {
    /**
     * Constructor -> which assigns the values:
     *
     * @param coreService domain service that can be overridden by any entity, and inherit all behavior and capabilities
     * @see #service the respective service for the current core entity.
     * @see #mapper The respective mapper of the core service.
     * @see #repository The respective repository for the current core entity.
     * @see #baseUri String required from the requestMapping on springBoot.
     */
    public GaggleUserController(ICoreService<GaggleUser, GaggleUserDTO, Long> coreService) {
        super(coreService);
    }
}
