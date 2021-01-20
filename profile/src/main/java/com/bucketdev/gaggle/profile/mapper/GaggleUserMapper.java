package com.bucketdev.gaggle.profile.mapper;

import com.bucketdev.gaggle.core.mapper.CoreMapper;
import com.bucketdev.gaggle.profile.domain.GaggleUser;
import com.bucketdev.gaggle.profile.dto.GaggleUserDTO;
import org.springframework.stereotype.Component;

/**
 * @author rodrigo.loyola
 */
@Component
public class GaggleUserMapper extends CoreMapper<GaggleUser, GaggleUserDTO> { }
