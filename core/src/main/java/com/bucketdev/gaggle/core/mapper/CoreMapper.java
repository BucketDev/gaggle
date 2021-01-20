package com.bucketdev.gaggle.core.mapper;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import com.bucketdev.gaggle.core.dto.CoreDTO;

import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import org.modelmapper.ModelMapper;

/**
 * @author rodrigo.loyola
 * @param <T> domain entity
 * @param <DTO> Object to be transferred
 */
public class CoreMapper<T extends CoreEntity, DTO extends CoreDTO> implements ICoreMapper<T, DTO> {

    private final ModelMapper modelMapper;

    public CoreMapper() {
        modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    /**
     * @return current modelMapper
     */
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    /**
     * @return core class
     */
    public Class<T> getCoreEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @return The dto class
     */
    public Class<DTO> getDTOClass() {
        return (Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Transforms dto object to a domain entity.
     *
     * @param dto object to transform
     * @return domain entity transformed
     */
    @Override
    @Transactional
    public T toDomain(DTO dto) {
        return null == dto ? null : modelMapper.map(dto, getCoreEntityClass());
    }

    /**
     * Transform domain entity to a dto object.
     *
     * @param coreEntity entity to transform
     * @return dto transformed
     */
    @Override
    @Transactional
    public DTO fromDomain(T coreEntity) {
        return null == coreEntity ? null : modelMapper.map(coreEntity, getDTOClass());
    }

    /**
     * Transfers the values sent from de DTO into the domain entity
     *
     * @param coreEntity target entity
     * @param source source values dto
     * @return resulted domain entity
     */
    @Override
    @Transactional
    public T setDomainValues(T coreEntity, DTO source) {
        source.setId(coreEntity.getId());
        modelMapper.map(source, coreEntity);
        return coreEntity;
    }

}
