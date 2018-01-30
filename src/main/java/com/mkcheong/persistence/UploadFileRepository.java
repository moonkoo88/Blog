package com.mkcheong.persistence;

import com.mkcheong.domain.UploadFile;
import com.mkcheong.domain.WebBoard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UploadFileRepository extends CrudRepository<UploadFile, Long> {

    public UploadFile findOneById(Long Id);

    @Query("SELECT r FROM UploadFile r WHERE r.boardBno = ?1 " + " AND r.id > 0 ORDER BY r.id ASC")
    public List<UploadFile> getUploadFileOsfBoard(Long bno);

}
