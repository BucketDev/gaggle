package com.bucketdev.gaggle.core.mapper;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import com.bucketdev.gaggle.core.dto.CoreDTO;

/**
 * @author rodrigo.loyola
 * @param <T> domain entity
 * @param <DTO> object to be sent as response
 */
public interface ICoreMapper<T extends CoreEntity, DTO extends CoreDTO> {

    /**
     * Method which transforms a DTO into a domain entity version of itself
     * @param dto data transfer object
     * @return domain entity
     */
    public T toDomain(DTO dto);

    /**
     * Method which transforms a domain entity into a DTO version of itself
     * @param coreEntity domain entity
     * @return dto
     */
    public DTO fromDomain(T coreEntity);

    /**
     * Method which transfer the values sent from de DTO into the domain entity
     * @param coreEntity target entity
     * @param source source values dto
     * @return resulted entity
     */
    public T setDomainValues(T coreEntity, DTO source);

}
