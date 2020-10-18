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


