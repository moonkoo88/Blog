package com.mkcheong;

import com.mkcheong.domain.UploadFile;
import com.mkcheong.domain.WebBoard;
import com.mkcheong.persistence.UploadFileRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class UploadFileRepositoryTests {
    @Autowired
    UploadFileRepository repo;

    @Test
    public void testSearchFileList(){

        List<UploadFile> uploadFileList = repo.getUploadFileOsfBoard(336L);

    }
}
