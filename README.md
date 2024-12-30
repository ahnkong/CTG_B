Branch
- Dev (개발 작업중 push)
- Stg (운영 반영전 개발 완료건 테스트 Staging)
- Prod (운영 환경 <사용자 접속화면> Product)

Folder
- config : 프로젝트 설정 파일
- controller : API 앤드포인트 관리
- dto : 데이터 전달하기 위한 객체 저장 (Reaqust / Response) 
- entity : 데이터베이스와 연결될 JPA 엔티티
- exception : 예외 처리 클래스
- repository : 데이터베이스와 직접 소통하는 인터페이스 (JPA를 사용한 CRUD)
- service : 비즈니스 로직을 처리하는 클래스
- util : 유틸리티 클래스 (공통 사용 헬퍼 함수 또는 클래스)
