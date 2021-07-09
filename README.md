# verpic-backend

verpic의 backend 서버 레포지토리.


1. 커밋 메시지 작성 시 https://doublesprogramming.tistory.com/256 참고해서 커밋 컨벤션에 맞게 작성해주세요.

2. docker 사용법

- Dockerfile 설정 빌드 이용하지 않고, 구글에서 자바 프로젝트 이미지 처리를 위해 만든 jibDockerBuild를 사용했어요.

[설치 요소]
- java 11
- docker
- docker-compose

[도커 실행]

1. 도커 이미지 빌드: 

```
./gradlew build jibDockerBuild
```
2. 도커 컨테이너 생성 및 이미지 실행

```
docker-compose up
```


[참고]
- 도커 서비스 시작 (컨테이너가 이미 있는 상태에서)

```
docker-compose start
```
- 서비스 중지

```
docker-compose stop
```
- 도커 푸시, 풀(도커 원격 저장소에서 이미지 받아오기)
```
docker push [내이름/이미지이름]
docker pull [내이름/이미지이름]
```

[Database 접속]

1. 실행중인 컨테이너 정보 확인
```
docker ps -a
```

2. 도커 컨테이너 CLI 접속
```
docker exec -i -t [컨테이너id 또는 이름] bash 
```
3. MySQL 접속

```
mysql -u [아이디] -p [패스워드]
```




