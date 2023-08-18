
***BASE URL*** : http://localhost:8080/
## API endpoints:
* http://localhost:8080/ : Landing page, welcome user. There 2 buttons:
  * to navigate to products for *shopping*
  * to navigate to register OR profile/user home page: if already logged in --> profile | else register page
* http://localhost:8080/api/auth/user/register Burada kullanıcı oluşturulur:
  *  request body:
  ```json
    {
        "name": "Test",
        "surname": "User",
        "username": "test1234",
        "password": "test1234"
    } 
    ```
  * error body:
  ```json
    {
      "timestamp": "2023-08-01T08:18:11.001+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/api/auth/user/register"
    }
  ```
  * success body (status code 200 OK):
  ```json
  {
    "id": 12,
    "username": "2test1234",
    "password": "2test1234",
    "name": "2Test",
    "surname": "User2",
    "role": "USER"
  }
  ```
* http://localhost:8080/api/auth/login?username=veli1234&password=veli1234 Burada girilen bilgiler eşleşirse kullanıcıya token verilir.
  * Burada username ve password bilgisi request parameter olarak yollanıyor.
  * Response error status 400 Bad Request:
    ```text
    Entered username or password is wrong.
    ```
  * success body (status 200 OK):
    ```json
    {
      "uid": 3,
      "token": "1690878767586-a9662f6f-1d26-4503-b65a-e51409a06059"
    }
    ```
* Landing Page static olacak. Bu yüzden herhangi bir api endpoint yok. url: http://localhost:8080/
* http://localhost:8080/home Kullanıcı profil sayfası: Burada kullanıcıya dair bilgiler bulunacak.
  * **Atılan request'te "my-token" header'ı bulunmalı.**
  * error (status 400 Bad Request)
  ```json
  {
    "timestamp": "2023-08-01T08:33:13.492+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Expired or invalid token.",
    "path": "/home"
  }
  ```
  * success (200 OK) Eğer kullanıcı admin ise admin dashboard'ı gösterilir değilse User profil sayfası gösterilir:
  ```json
  {
    "id": 3,
    "username": "veli1234",
    "password": null,
    "name": "Veli",
    "surname": "Demir",
    "role": "ADMIN"
   }
  ```
* http://localhost:8080/api/auth/logout?id=3 Logout için parametre olarak id kullanıcı id bilgisi verilir.
  * Burada da **"my-token"** header bilgisi gereklidir

* http://localhost:8080/api/auth/admin/find-user?username=test1234 Burada parametre olarak username verilir.
  * Buraya yalnızca **ADMIN** rolü olan kullanıcılar istek atabilir.
  * **my-token** gereklidir.
  * error-1: 
  ```json
  {
    "timestamp": "2023-08-01T08:50:05.005+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Expired or invalid token.",
    "path": "/api/auth/user/find"
  }
  ```
    * error-2:
  ```json
  {
    "timestamp": "2023-08-01T08:52:17.239+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "You are not authorized",
    "path": "/api/auth/user/find"
   }
  ```