# 프로젝트 소개

Spring Boot 2.1 기반으로 Spring Security OAuth2를 살펴보는 프로젝트입니다. Authorization Code Grant Type, Implicit Grant, Resource Owner Password Credentials Grant, Client Credentials Grant Type OAuth2 인증 방식에 대한 간단한 셈플 코드부터 OAuth2 TokenStore 저장을 mysql, redis 등 저장하는 예제들을 다룰 예정입니다. 계속 학습하면서 정리할 예정이라 심화 과정도 다룰 수 있게 될 거 같아 깃허브 Start, Watching 버튼을 누르시면 구독 신청받으실 수 있습니다. 저의 경험이 여러분에게 조금이라도 도움이 되기를 기원합니다.

# 구성
* Spring Boot 2.1.0
* Spring Security OAuth2
* Lombok
* Java8

# 목차
https://velog.io/@rohkorea86/oAuth-oAuth%EB%A5%BC-%EC%9D%B4%ED%95%B4%ED%95%98%EB%8A%94-%EC%9A%A9%EC%96%B4%EC%A0%95%EB%A6%AC
oAuth의 이해 관계자들
Resource Server(리소스 서버): 제어할 수 있는 자원을 가진 서버
     kakao, facebook, naver 회사를 의미합니다
Resource Owner(리소스 오너): 자원의 소유자( 브라우저, 로그인 자원(Resource)를 가지고 있는 사람(Owner)으로 해당 서비스를 이용하려는 유저(User)입니다.)
client(클라이언트): 리소스 서버에 접속해서 정보를 가져가는 대상 : 우리의 현재 만들고 있는 서비스(service) 혹은 어플리케이션(application)을

* [step-01 OAuth2 인증 방식 Flow 및 Sample Code](https://github.com/cheese10yun/springboot-oauth2/blob/master/docs/OAuth2-Grant.md)
* [step-02 토큰과 클라이언트 정보 RDBMS 저장](https://github.com/cheese10yun/springboot-oauth2/blob/master/docs/OAuth2-RDBMSt.md)
* [step-03 Redis를 이용한 토큰 저장 작업중...]()

**step-XX Branch 정보를 의미합니다. 보고 싶은 목차의 Branch로 checkout을 해주세요**


http://www.bubblecode.net/en/2016/01/22/understanding-oauth2/
oles
OAuth2 defines 4 roles :

Resource Owner: generally yourself.
Resource Server: server hosting protected data (for example Google hosting your profile and personal information).
Client: application requesting access to a resource server (it can be your PHP website, a Javascript application or a mobile application).
Authorization Server: 서버가 클라이언트에 액세스 토큰을 발행합니다. 이 토큰은 클라이언트가 리소스 서버를 요청하는 데 사용됩니다. 이 서버는 권한 부여 서버 (동일한 물리적 서버 및 동일한 응용 프로그램)와 동일 할 수 있으며 종종 그럴 수 있습니다.
Tokens
토큰은 권한 부여 서버에 의해 생성 된 임의의 문자열이며 클라이언트가 요청할 때 발행됩니다.
There are 2 types of token:

Access Token: 이는 타사 응용 프로그램에서 사용자 데이터에 액세스하는 것을 허용하기 때문에 가장 중요합니다. 이 토큰은 클라이언트에 의해 매개 변수 또는 요청의 헤더로 리소스 서버에 전송됩니다. 권한 부여 서버에 의해 정의 된 제한된 수명이 있습니다. 가능한 한 빨리 기밀로 유지해야하지만, 특히 클라이언트가 Javascript를 통해 리소스 서버에 요청을 보내는 웹 브라우저 인 경우 항상 가능한 것은 아닙니다.
Refresh Token: 이 토큰은 액세스 토큰과 함께 발급되지만 후자와 달리 클라이언트에서 리소스 서버로의 각 요청에서 전송되지 않습니다. 만료되었을 때 액세스 토큰을 갱신하기 위해 권한 부여 서버로 전송되는 역할을합니다. 보안상의 이유로 항상이 토큰을 얻을 수있는 것은 아닙니다. 나중에 어떤 상황에서.


curl -X POST   http://localhost:8080/oauth/token   -H 'Content-Type: application/x-www-form-urlencoded'   -d 'username=user&password=pass&grant_type=password&scope=read_profile'


 테스트 방법
3) Resource Owner Password Credentials Grant 방식
curl -X POST http://localhost:8080/oauth/token -H "Authorization: Basic Y2xpZW50OnBhc3N3b3Jk"  -H "Content-Type: application/x-www-form-urlencoded" -d "username=user&password=pass&grant_type=password&scope=read_profile"
 3.1) 리턴값으로 access token및 refresh token이 리턴된다.
 {"access_token":"3d52838f-8640-42e1-97eb-1b2e4f9002fd","token_type":"bearer","re
fresh_token":"7d38a264-b99c-495a-bb9e-c9e6bc30ad30","expires_in":2999,"scope":"r
ead_profile"}

curl -X POST \
'http://localhost:8080/oauth/check_token' \
-d 'token=a1849c64-2ca6-4d32-8f15-6cf01029dfb6'

{"access_token":"3d52838f-8640-42e1-97eb-1b2e4f9002fd","token_type":"bearer","refresh_token":"7d38a264-b99c-495a-bb9e-c9e6bc30ad30","expires_in":2790,"scope":"read_profile"}

  1) Authorization Code Grant Type
  1.1)  http://localhost:8080/oauth/authorize?client_id=client&redirect_uri=http://localhost:9000/callback&response_type=code&scope=read_profile 
 1.2) Approval, Deny로 표시됨
 1.3) 리턴값 code 리턴
  http://localhost:9000/callback?code=dbdQJN
Xy3BOA
curl -X POST http://localhost:8080/oauth/token -H "Authorization: Basic Y2xpZW50OnBhc3N3b3Jk"  -H "Content-Type: application/x-www-form-urlencoded"   -d "code=dbdQJN&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A9000%2Fcallback&scope=read_profile"
{"access_token":"3d52838f-8640-42e1-97eb-1b2e4f9002fd","token_type":"bearer","re
fresh_token":"7d38a264-b99c-495a-bb9e-c9e6bc30ad30","expires_in":1874,"scope":"r
ead_profile"}

//GET /oauth/authorize 요청은 클라이언트가 리소스 서버의 API를 사용하기 위해 사용자(리소스 소유자)에게 권한 위임 동의를 받기 위한 페이지를 출력하는 기능을 수행한다
https://jsonobject.tistory.com/372
https://jsonobject.tistory.com/374
https://jsonobject.tistory.com/373
### grant_type = 'authorization_code'
curl -X POST \
'http://localhost:8080/oauth/token' \
-U 'some_client_id:some_client_secret' \
-d 'grant_type=authorization_code' \
-d 'code=DO8jTT' \
-d 'redirect_uri=http://localhost'

### grant_type = 'client_credentials'
curl -X POST \ 'http://localhost:8080/oauth/token' \ 
-U 'some_client_id:some_client_secret' \ 
-d 'grant_type=client_credentials'

### grant_type = 'refresh_token'
curl -X POST \ 'http://localhost:8080/oauth/token' \
-U 'some_client_id:some_client_secret' \
-d 'grant_type=refresh_token' \
-d 'refresh_token=9c2d4115-ebd2-4ff5-8f69-1c2e1198cf52'


# 성공 응답
{
    "access_token": "a1849c64-2ca6-4d32-8f15-6cf01029dfb6",
    "token_type": "bearer",
    "refresh_token": "9c2d4115-ebd2-4ff5-8f69-1c2e1198cf52",
    "expires_in": 43199,
    "scope": "read:users"
}

# code 파라메터가 유효하지 않을 경우
{
    "error": "invalid_grant",
    "error_description": "Invalid authorization code: DO8jTT"
}

# redirect_uri 파라메터가 유효하지 않을 경우
{
    "error": "invalid_grant",
    "error_description": "Redirect URI mismatch."
}

# authorization_code 파라메터가 유효하지 않을 경우
{
    "error": "unsupported_grant_type",
    "error_description": "Unsupported grant type: authorization_code"
}