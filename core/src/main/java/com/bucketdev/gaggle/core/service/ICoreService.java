package com.bucketdev.gaggle.core.service;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import com.bucketdev.gaggle.core.dto.CoreDTO;
import com.bucketdev.gaggle.core.mapper.ICoreMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author rodrigo.loyola
 * @param <T> domain entity class
 * @param <DTO> object to be transferred class
 * @param <Key> object which works as identifier
 */
public interface ICoreService<T extends CoreEntity, DTO extends CoreDTO, Key> {

    public PagingAndSortingRepository<T, Key> getRepository();

    public ICoreMapper<T, DTO> getMapper();

    public DTO create(DTO dto);

    public DTO get(Key id);

    public Page<DTO> getAllPaged(Pageable pageable);

    public List<DTO> getAll(Sort sort);

    public DTO update(Key id, DTO dto);

    public void delete(Key id);

    public void delete(Key id, boolean forceDelete);

    @Transactional
    public void onDisabled(T entity);

}
