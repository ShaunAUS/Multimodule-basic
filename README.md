# SKILLS


- Kotlin
- KotlinDSL
- Springboot
- SpringDataJpa
- Querydsl
- mysql
- Redis
- springSecurity

# STYLE

- ktlint

# API

### MEMBER

- POST / : api/v1/member => 회원가입
- PATCH / : api/v1/member => 회원정보 수정

### BOARD

- GET / : api/v1/board/{board-uuid} => 게시글 조회
- GET / : api/v1/board/all => 게시글 전체 조회
- GET / : api/v1/board/search?searchTitle={title} => 게시글 검색
- PATCH / : api/v1/board => 게시글 수정
- PATCH / : api/v1/board/{board-uuid} => 게시글 삭제
- POST / : api/v1/board => 게시글 작성

### AUTH

- POST / : api/v1/auth/login => 로그인
- POST / : api/v1/auth/logout => 로그아웃

### AUTH-TEST

- GET / : api/v1/member/test = > 권한 테스트

