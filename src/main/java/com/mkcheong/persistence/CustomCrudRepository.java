package com.mkcheong.persistence;

import com.mkcheong.domain.WebBoard;
import org.springframework.data.repository.CrudRepository;

public interface CustomCrudRepository extends CrudRepository<WebBoard, Long>, CustomWebBoard {
}
