## IoT for B.C BackEnd source code
- This is BackEnd source code for IoT Solutions World Congress 2018
- 2018 바르셀로나 IoT Solutions World Congress 목업 전시용 웹디스플레이 미들웨어 소스코드 

## Teck Stack
- java version : 1.8
- spring boot : 1.5.10
- couchbase : Community Edition 5.0.1 build 5003
- spring boot scheduler
- swagger : 2.6.1
- sockjs-client : 1.0.2
- stomp-websocket : 2.3.3

## 프론트단 + API 호출 서버단 구조
- 전체구조(frontend + backend)
![projectarch](/path/to/img.jpg)


## localhost path setting
- application.yml  

## spring boot profile 분리
#### 개발 옵션
mvn spring-boot:run -Dspring.profiles.active=dev; 실행

#### 로컬 옵션
mvn spring-boot:run -Dspring.profiles.active=local; 실행

```
server:
  port: 8081
  address: <my_local_ip>
  
```

## locale, timezone setting
  
```
spring:
  mvc:
      #locale: ko_KR
      locale: es_ES

  jackson:
    time-zone: Europe/Madrid
    #time-zone: Asia/Seoul  

```