# ks-to-do-back

```sql
-- 시퀀스 생성
CREATE SEQUENCE seq_KS_TODO;
```

```sql
-- 테이블 생성
CREATE TABLE KS_TODO(
  id BIGINT NOT NULL default NEXTVAL('seq_KS_TODO'),
  todo VARCHAR
);
```

```sql
-- 테이블 삭제시 시퀀스 함께 삭제
ALTER SEQUENCE seq_KS_TODO OWNED BY KS_TODO.seq;
```

```sql
-- 값 삽입, 조회
INSERT INTO KS_TODO VALUES (default, 'aaa');
SELECT * FROM KS_TODO;
```