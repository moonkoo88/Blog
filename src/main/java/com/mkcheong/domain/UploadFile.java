package com.mkcheong.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_upload_files")
@EqualsAndHashCode(of = "id")
@ToString
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_bno")
    private Long boardBno;

    private String fileName;

    private String saveFileName;

    private String filePath;

    private String contentType;

    private Long size;

    @CreationTimestamp
    private Timestamp regdate;

}
