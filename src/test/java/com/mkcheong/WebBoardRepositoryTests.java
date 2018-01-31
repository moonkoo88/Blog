package com.mkcheong;

import com.mkcheong.domain.WebBoard;
import com.mkcheong.domain.moonkoo.tables.TblWebboards;
import com.mkcheong.persistence.WebBoardRepository;

import lombok.extern.java.Log;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.stream.IntStream;

import static com.mkcheong.domain.moonkoo.tables.TblWebboards.TBL_WEBBOARDS;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class WebBoardRepositoryTests {

    @Autowired
    WebBoardRepository repo;
    @Autowired
    DSLContext dsl;
    /*
    @Test
    public void insertBoardDummies() {
        IntStream.range(0, 300).forEach(i -> {
            WebBoard board = new WebBoard();

            board.setTitle("sample board title " + i);
            board.setContent("content sample ..." + i + " of board");
            board.setWriter("user0" + (i % 10));

            repo.save(board);
        });
    }
    */
    @Test
    public void testList1ByQueryDSL() {
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "bno");

        Page<WebBoard> result = repo.findAll(repo.makePredicate(null, null), pageable);

        log.info("page : " + result.getPageable());

        log.info("------------------------------");

        result.getContent().forEach(board -> log.info("" + board));
    }

    @Test
    public void testList1ByJooq() {
        log.info("" + dsl.select().from(TBL_WEBBOARDS).orderBy(TBL_WEBBOARDS.BNO.desc()).limit(20).fetch());
    }

    @Test
    public void testList2(){
        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "bno");

        Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);

        log.info("page : " + result.getPageable());

        log.info("------------------------------");

        result.getContent().forEach(board -> log.info("" + board));
    }

}
