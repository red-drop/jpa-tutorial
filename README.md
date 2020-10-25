# JPA ( Java Persistence Api )
- Java에서 RDB를 쉽게 이용하기 위한 ORM Inferface
- Hibernate 등의 JPA 구현체를 이용해서 사용 할 수 있다.
- DB에 따른 SQL을 자동 생성하기 때문에 독립적으로 사용할 수 있다.

# 영속성 컨텍스트 ( Persistence Api )
- 엔티티를 저장하는 환경
- 엔티티 매니저를 통해 접근
```
# J2SE 환경
      (1)-------------------(1)
EntityManager ---> Persistence Context

```
```
# J2EE, Spring 등 컨테이너 환경
  
     (N)----------------------(1)
EntityManager -- 
                \
EntityManager ---+--> Persistence Context
                /
EntityManager --
```

## 영속성 컨텍스트의 생명주기
- 비영속 (new/transient)
   - Model model = new Model() 
- 영속 (managed)
  - find 
  - persist
- 준영속 (detached)
  - detach
  - clear
- 삭제 (removed)
  - close
  
## 영속성 컨텍스트의 이점
- 1차 캐시 
- 동일성 보장 (엔티티를 컬렉션 처럼 사용할 수 있음)
- 트랙잭션을 지원하는 쓰기 지연 
- 변경감지 
- 지연로딩

## Flush
`em.flush();` 
- 즉시 DB에 Commit 할 수 있음
- 영속성 컨텍스트를 비우지 않음 
- 거의 쓸 이유가 없음 
- Commit, AUTO 두 가지의 모드가 존재 

# Entity Mapping
## Object <--> Entity
- `@entity` 가 붙은 클래스는 Jpa가 관리
- 기본 생성자 필수
- final class, enum, interface, inner 클래스는 사용할 수 없다
- column 에는 final 을 사용할 수 없다.

## Schema auto create (migration)
- `persistence.xml` 에서 설정
- 여러가지 옵션이 존재
```
create (테이블 생성, 제약조건 설정 시 create 후에 alter 실행)
create-drop (종료 시 테이블 drop)
validate (entity validation 만 실행)
update (Object에 새 멤버 추가 시 테이블에도 반영, 제약조건은 업데이트 되지 않음)
none
```
- delete 는 존재하지 않음

# Field Mapping
- 각 필드는 여러가지 타입과 매핑될 수 있음 (enum 등)
- 비즈니스 상 제약조건에 따라 설정 가능
```java
@Column // 컬럼
@Temporal // 날짜 타입
@Enumerated // enum
@Lob // blob clob 맵핑
@Transient // db와 맵핑하지 않음
```
- 속성 값 
```
name: 컬럼명
insertable, updateable: default true 
nullbable: false 시 not null 설정 
unique: field보다는 @table 의 UniqueConstraints로 사용하는 경우가 많음 
length: String 타입에 사용하는 문자열 길이 제약 
columnDefinition: DB컬럼 정보르 직접 줄 수 있음 (columnDefinition ="varchar(10) defulat `EMPTY`")
precision, cale: BigDecimal, BigInteger 타입에서 사용. 
  - precision: 소수점을 포함한 전체 자릿수, double, float타입에는 적용되지 않음.
  - sale: 소수의 자릿수
```

## EnumType
EnumType 을 정해서 사용해야 함
- ORDINAL(default): enum 순서를 DB에 저장 (integer 타입)
- STRING: enum 이름을 DB에 저장 (string 타입)

## Temporal
- 날짜 타입을 매핑할 때 사용
- 자바 8 이상에서는 필요 없음
    - LocalDate
    - LocalDateTime
    
## Lob
- 지정 가능한 속성이 없음
- 필드 타입이 문자면 CLOB, 나머지(바이트 등) 이면 BLOB

## Primary Key Mapping 
- 직접 할당: @id만 사용
- 자동 생성 (@GeneratedValue)
  - Identity: DB에 위임
    - 기본 키 생성을 DB에 위임 
    - JPA는 commit 시점에 insert query 를 실행 
    - JPA의 영속 컨텍스트에서는 id를 이용해서 레코드를 관리하나, Identity의 경우에는 쿼리를 날린 후에 ID가 생성된다.
    - persist 에서 Insert Query를 날리고 JPA에서 ID를 긁어온다.
    - Buffer Write를 사용할 수 없다.
  - SEQUENCE: DB 시퀀스 오브젝트 사용 (@SequenceGenerator)
    - 먼저 Sequence 에서 다음 값을 가져온 후 persist 에서 영속성 컨텍스트에 넣는다.
    - allocationSize의 기본값은 50인데, 50개를 미리 메모리에 세팅 해두고 꺼내쓰게 된다.

### @GeneratedValue
```java
/*
    AUTO - DB에 따라 자동 선택
    IDENTITY - MYSQL (Auto increment)
    SEQUENCE - ORACLE (Default HibernateSequence, 이름 지정 필요)
    TABLE - 키 생성 전용 테이블을 만들어서 시퀀스를 흉내내는 전략, 성능이슈 
*/

@Entity
@SequanceGenerator(
    name="TableName_SEQ_GENERATOR",
    sequenceName = "TableName_SEQ",
    initialValue = 1, allocationSize =1)
); 
public class Model {
@Id
/*
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TableName_SEQ_GENERATOR");
    @GeneratedValue(strategy = GenerationType.AUTO);
*/
private Long id;
}
```

## Association Mapping
- 한쪽에서만 DML을 사용할 수 있도록 설정
- 연관 관계의 주인에서만 DML 을 사용한다.
- 연관 관계의 주인은 왜래키를 가진 쪽에 설정
- 필요시 편리 메소드를 만들어서 사용한다.

### Many To One 
```java
/*
  +--------+ N    1 +------+
  | member | -----> | team |
  +--------+        +------+
*/
class Member {
    @ManyToOne(fetch = FetchType.LAZY || FetchType.EAGER) // EAGER is default
    @JoinColumn(name="team_id")
    private Team team;
}

class Team {    
    // team 이라는 값으로 맵핑 되었다. 
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();     
}
```

### One to One
- 왜래키를 가진 쪽이 연관관계의 주인
```java
/*
  +--------+ 1    1 +--------+
  | member | -----> | locker |
  +--------+        +--------+
*/
class Member {
    @OneToOne
    @JoinColumn(name = "locker_id") 
    private Locker locker;
}

class Locker {
    private Long id;
}
```

### Many To Many
- 테이블 설계 상에는 중간 테이블이 있어야 함
- 중간 테이블을 엔티티로 승격
- join_table 옵션 지정
```java
/*
  +--------+ N    N +---------+
  | member | -----> | product |
  +--------+        +---------+
*/
class Member {
    @OneToMany(mappedBy = "member") 
    private List<MemberProduct> memberProducts = new ArrayList<>();
}

class MemberProduct {
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

class Product {    
    @OneToMany(mappedBy = "product") 
    private List<MemberProduct> memberProducts = new ArrayList<>();     
}
```