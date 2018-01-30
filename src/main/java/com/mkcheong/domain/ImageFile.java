package com.mkcheong.domain;

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
@Table(name = "tbl_image_files")
@EqualsAndHashCode(of = "id")
@ToString
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String saveFileName;

    private String filePath;

    private String contentType;

    private Long size;

    @CreationTimestamp
    private Timestamp regdate;

}
