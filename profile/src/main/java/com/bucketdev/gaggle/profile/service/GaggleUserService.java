package com.bucketdev.gaggle.profile.service;

import com.bucketdev.gaggle.core.mapper.ICoreMapper;
import com.bucketdev.gaggle.core.service.CoreService;
import com.bucketdev.gaggle.profile.domain.GaggleUser;
import com.bucketdev.gaggle.profile.dto.GaggleUserDTO;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

/**
 * @author rodrigo.loyola
 */
@Service
public class GaggleUserService extends CoreService<GaggleUser, GaggleUserDTO, Long> {

    protected GaggleUserService(PagingAndSortingRepository<GaggleUser, Long> repository,
                                ICoreMapper<GaggleUser, GaggleUserDTO> mapper) {
        super(repository, mapper);
    }

    @Override
    public void onDisabled(GaggleUser entity) {

    }
}
