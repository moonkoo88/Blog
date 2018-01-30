package com.mkcheong.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_members")
@EqualsAndHashCode(of = "uid")
@ToString
public class Member implements Serializable {

    @Id
    private String uid;

    private String upw;

    private String uname;

    @CreationTimestamp
    private Timestamp regdate;
    @UpdateTimestamp
    private Timestamp updatedate;

    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinColumn(name = "member")
    private List<MemberRole> roles;

}
