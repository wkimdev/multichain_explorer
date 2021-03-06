## IoT for B.C BackEnd source code
- This is BackEnd source code for IoT Solutions World Congress 2018
- 2018 바르셀로나 IoT Solutions World Congress 목업 전시용 웹디스플레이 미들웨어 소스코드 

## couchbase issue (for window os)
#### couchbase date function timezone window issue
> window에서 couchbase의 타임존 셋팅에 관한 date function을 사용할 경우, 
> non-unix계열에 대한 LoadLocation을 수행할 수 있는 ZONEINFO 환경 변수가 셋팅되어 있어야 했다.(golang설치가 필요)
>  golang을 설치함으로서 date가 조회되지 않던 현상 해결.  
  
- [참고 url](https://issues.couchbase.com/browse/MB-29814)


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
<img width="1022" alt="2019-02-21 11 14 33" src="https://user-images.githubusercontent.com/32521173/53138533-f49ae000-35c9-11e9-9ca1-9188f9228669.png">


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
