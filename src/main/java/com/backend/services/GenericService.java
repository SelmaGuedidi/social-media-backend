package com.backend.services;

import com.backend.utils.PagingRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@AllArgsConstructor
public abstract class GenericService<T, R extends MongoRepository<T, String>> {

    protected final R repository;

    public T get(String id) throws NotFoundException{
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    protected Pageable setPageable(PagingRequest req) {
        Sort sort;
        if (StringUtils.hasText(req.getSortField())) {
            sort = Sort.by(Sort.Direction.fromString(req.getSortOrder()), req.getSortField());
        } else {
            sort = Sort.by(Sort.Direction.fromString("DESC"), "CreatedAt");
        }
        System.out.println(sort);
        return PageRequest.of(req.getPageNumber(), Math.max(req.getPageSize(),1), sort);
    }

    @Transactional
    public T create(T newDomain){
        return repository.save(newDomain);
    }


}