package com.bucketdev.gaggle.core.service;

import com.bucketdev.gaggle.core.domain.CoreEntity;
import com.bucketdev.gaggle.core.dto.CoreDTO;
import com.bucketdev.gaggle.core.mapper.ICoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.test.annotation.Timed;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author rodrigo.loyola
 * @param <T> domain entity class
 * @param <DTO> object to be transferred class
 * @param <Key> object which works as identifier
 */
public abstract class CoreService<T extends CoreEntity, DTO extends CoreDTO, Key>
    implements ICoreService<T, DTO, Key> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final PagingAndSortingRepository<T, Key> repository;
    protected final ICoreMapper<T, DTO> mapper;

    protected CoreService(PagingAndSortingRepository<T, Key> repository, ICoreMapper<T, DTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PagingAndSortingRepository<T, Key> getRepository() {
        return repository;
    }

    @Override
    public ICoreMapper<T, DTO> getMapper() {
        return mapper;
    }

    @Override
    public DTO create(DTO dto) {
        if (dto.getId() == null || dto.getId().toString().isEmpty()) {
            T coreEntity = mapper.toDomain(dto);
            return mapper.fromDomain(repository.save(coreEntity));
        }
        return null;
    }

    @Override
    public DTO get(Key id) {
        Optional<T> optional = repository.findById(id);
        return optional.map(mapper::fromDomain).orElse(null);
    }

    @Override
    public Page<DTO> getAllPaged(Pageable pageable){
        Page<T> page = repository.findAll(pageable) ;
        List<DTO> content = page.getContent().stream().map(mapper::fromDomain).collect(Collectors.toList());
        return new PageImpl<DTO>(content, pageable, page.getTotalElements());
    }

    @Override
    public List<DTO> getAll(Sort sort) {
        return StreamSupport.stream(repository.findAll(sort).spliterator(), false)
            .map(mapper::fromDomain).collect(Collectors.toList());
    }

    @Override
    public DTO update(Key id, DTO dto) {
        Optional<T> optional = repository.findById(id);
        return optional.map(entity -> mapper.fromDomain(
            repository.save(mapper.setDomainValues(entity, dto)))).orElse(null);
    }

    @Override
    public void delete(Key id, boolean forceDelete) {
        Optional<T> optional = repository.findById(id);
        if (optional.isPresent()) {
            T entity = optional.get();
            if (forceDelete) {
                repository.deleteById(id);
            } else {
                entity.setEnabled(false);
                repository.save(entity);
                try {
                    onDisabled(entity);
                } catch (Exception ex) {
                    log.error(ex.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public void delete(Key id) { delete(id, false); }
}
