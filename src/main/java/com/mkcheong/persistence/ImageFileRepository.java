package com.mkcheong.persistence;

import com.mkcheong.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    public ImageFile findOneById(Long Id);
}
