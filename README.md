## IoT for B.C BackEnd source code
- This is IoT for B.C BackEnd source code 


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