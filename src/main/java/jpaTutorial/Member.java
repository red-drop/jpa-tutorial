package jpaTutorial;

import javax.persistence.*;
import java.util.Date;

@Entity // Entity Object Mapping @Entity(name="Member)
/*
    다른 테이블 명과 매칭 시 이름 지정 가능
    Table(name="MBR")
 */
public class Member {
    @Id // PK mapping
    private Long id;

    /*
        Add Constraint
        Column Mapping
        Create 이후에 Alter table 로 실행됨
        Auto-ddl 을 update로 설정 한 후에 제약조건을 설정하면 반영되지 않음
        @Column(name="Username" unique = true, length = 10)
     */
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String name;

    /*
        persistence.xml 에서 auto-ddl 을 update 로 설정 시 create 때 새 컬럼도 추가됨
     */
    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
