# AI Chat Server

Java의 인터페이스와 팩토리 패턴을 활용하여 여러 AI Provider(OpenAI, Ollama)를 런타임에 동적으로 교체할 수 있는 단일 스레드 소켓 기반 채팅 서버입니다.

## 프로젝트 목표

* Java의 다형성과 인터페이스를 활용한 유연한 설계
* AI Provider에 대한 의존성을 최소화
* OpenAI API와 로컬 Ollama를 동일한 인터페이스로 관리
* 소켓 통신 기반의 채팅 서버 구현

## 기술 스택

* Java 21
* Gradle
* Jackson
* java.net.Socket
* HttpClient
* OpenAI API
* Ollama API

## 프로젝트 구조

```
src/main/java
│
├── engine
│   ├── AIEngine.java
│   ├── OpenAIEngine.java
│   └── OllamaEngine.java
│
├── factory
│   └── AIEngineFactory.java
│
├── server
│   ├── SocketServer.java
│   └── SocketClient.java
│
└── Main.java
```

## 아키텍처

```
Client
    ↓
SocketServer
    ↓
AIEngine
↑        ↑
OpenAIEngine   OllamaEngine
    ↓              ↓
OpenAI API     Ollama API
```

AIEngine 인터페이스를 통해 AI Provider를 추상화하였으며, 팩토리 패턴을 이용하여 런타임에 구현체를 동적으로 변경할 수 있도록 구성하였다.

## 주요 기능

### 단일 스레드 소켓 서버

* ServerSocket을 이용한 클라이언트 연결
* 메시지 송수신
* 채팅 종료 기능

### AI Provider 동적 전환

명령어를 통해 런타임에 AI 엔진을 변경할 수 있다.

```
/openai
/ollama
```

예시

```
안녕

OpenAI : 안녕하세요.
```

```
/ollama

안녕

안녕하세요! 무엇을 도와드릴까요?
```

### OpenAI API 연동

* HttpClient를 이용한 REST API 호출
* Jackson을 이용한 JSON 직렬화 및 역직렬화
* API Key 외부 분리
* 오류 응답 처리

### Ollama API 연동

* 로컬 LLM 호출
* REST API 기반 통신
* OpenAI와 동일한 인터페이스 사용

## 설계 의도

AI Provider가 추가되더라도 기존 서버 코드를 수정하지 않고 확장할 수 있도록 구성하였다.

새로운 AI Provider를 추가할 경우

```java
public class GeminiEngine implements AIEngine {

    @Override
    public String chat(String message) {

    }

}
```

구현체와 Factory만 수정하면 된다.

SocketServer는 AIEngine 인터페이스만 의존하기 때문에 내부 구현에 영향을 받지 않는다.

## 트러블 슈팅

### 멀티라인 응답이 한 줄씩 출력되는 문제

원인

* BufferedReader.readLine()은 한 줄 단위로 데이터를 읽는다.

해결

* 응답 종료를 의미하는 `[END]` 프로토콜을 정의
* 클라이언트가 `[END]`를 만날 때까지 응답을 수신하도록 수정

---

### 문자열 파싱 방식의 한계

초기 구현

```java
indexOf()
substring()
```

문제점

* 응답 구조 변경 시 쉽게 깨짐
* 유지보수 어려움

개선

* Jackson ObjectMapper를 사용하여 JSON 파싱 수행

---

### Gradle 환경에서 한글 깨짐

원인

* JVM 기본 인코딩 문제

해결

* UTF-8 설정 적용
* IntelliJ 실행 환경 사용

---

### OpenAI API quota 부족

원인

* Billing 미설정

해결

* 오류 응답 처리 추가

## 개선점

### 1. 단일 스레드 → 멀티 스레드 구조

현재는 하나의 클라이언트만 처리할 수 있다.

향후

* Thread Pool
* ExecutorService

를 이용하여 여러 클라이언트를 동시에 처리할 수 있도록 개선할 수 있다.

---

### 2. Factory 패턴 개선

현재

```java
if(type.equals("openai"))
```

형태로 구현되어 있다.

향후

* Enum
* Map 기반 Factory

를 사용하여 확장성을 높일 수 있다.

---

### 3. Socket 통신 프로토콜 개선

현재는

```
[END]
```

문자열을 이용하여 응답 종료를 판단한다.

향후

* JSON 기반 프로토콜
* Message DTO

를 도입하여 안정성을 높일 수 있다.

---

### 4. 예외 처리 개선

현재는 문자열 형태로 오류를 반환한다.

향후

* Custom Exception
* Error Response DTO

를 통해 구조화된 오류 처리를 수행할 수 있다.

---

### 5. AI Provider 추가

현재

* OpenAI
* Ollama

를 지원한다.

향후

* Gemini
* Claude
* DeepSeek

등을 동일한 인터페이스 기반으로 쉽게 확장할 수 있다.

---

### 6. Context 관리 기능 추가

현재는 독립적인 요청만 처리한다.

향후

* 대화 기록 저장
* Sliding Window
* Token 제한 관리

기능을 추가하여 실제 LLM 채팅 서비스에 가까운 구조로 확장할 수 있다.
