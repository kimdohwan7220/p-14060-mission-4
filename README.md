## 📖 명언 앱

### 프로젝트 구조 
- mission1
  - controller
    - AppController.java       # 전체 앱 실행
    - QuoteController.java     # 명령어 처리 + View 연결
    - SystemController.java    # 헤더/종료 처리
  - domain
    - Quote.java               # 명언 객체 생성
    - QuoteRepository.java     # json 파일 기반 CRUD + 영속성
  - service
    - QuoteService.java        # 비즈니스 로직 처리 + 검증
  - utils
    - QuoteValidator.java      # ID 존재 여부 검증
  - view
    - InputView.java           # 사용자 입력 처리
    - OutputView.java          # 화면 출력 처리
  - Main                       # 실행 파일

- test
  - domain
    - QuoteTest.java
    - QuoteRepositoryTest.java
  - service
    - QuoteServiceTest.java
  - controller
    - QuoteControllerTest.java

### ⚙️ 기능 구현 목록
#### 🧾 입력
- 명령어를 입력받는다.
  - 지원 명령어: 등록, 목록, 삭제?id=<번호>, 수정?id=<번호>, 종료 
- 명언 등록 시 입력 받는다.
  - 명언 내용과 작가를 입력받는다.   

#### 📝 등록
- 명언 등록 시 고유 번호를 자동 생성한다.
  - 등록 시마다 번호가 증가한다. 

#### 🔍 조회
- 전체 명언을 조회할 수 있다.

#### ✏️ 수정
- ID를 기준으로 명언 내용을 수정할 수 있다.
  - `ERROR` : 존재하지 않는 ID를 입력하면 메시지를 출력한다.
  
#### ❌ 삭제
- ID를 기준으로 명언을 삭제할 수 있다.
  - `ERROR` : 존재하지 않는 ID를 입력하면 메시지를 출력한다.

#### 💾 파일 저장 (영속성)
- 등록, 수정, 삭제된 명언은 파일 형태로 저장됩니다.
  - 저장 경로 : `db/wiseSaying/`
  - 각 명언은 `{id}.json` 형태로 저장됩니다.
  - 마지막 명언 ID는 `lastId.txt`에 저장되어 다음 ID 자동 증가에 사용됩니다.
  - `빌드` 명령 실행 시 전체 목록이 `data.json` 파일로 생성/갱신됩니다.
  - [x] 프로그램을 다시 실행해도 데이터가 유지됩니다.