package com.mkcheong.persistence;

import com.mkcheong.domain.QWebBoard;
import com.mkcheong.domain.WebBoard;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.querydsl.core.types.Predicate;

public interface WebBoardRepository extends CrudRepository<WebBoard, Long>, QuerydslPredicateExecutor<WebBoard> {

    public default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        QWebBoard board = QWebBoard.webBoard;

        // type if ~ else
        // bno > 0
        builder.and(board.bno.gt(0));

        if(type == null){
            return builder;
        }

        switch (type){
            case "t" :
                builder.and(board.title.like("%" + keyword + "%"));
                break;
            case "c" :
                builder.and(board.content.like("%" + keyword + "%"));
                break;
            case "w" :
                builder.and(board.writer.like("%" + keyword + "%"));
                break;
        }

        return builder;
    }
}
