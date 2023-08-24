# Token Filter App
My aim in this project is to understand servlet filters and how to manage token headers in the request and response headers. I had a chance to experience Inversion Of Control principle and the implementation of it via Dependency Injection.

In this application, a simple token based authentication and authorization method is adopted. The token contains expire data and some unique random characters. There are a number of protected paths that requires a valid token to be accessed. Existence of a valid token header is examined using two separate filters: token filter and authorization filter. token filter checks if token is not expired and exists in the database. After that, authorization filter checks if the requested path or resources are allowed to be accessed by the requesting user. Furthermore, the application provides some other features such as CRUD for users and products.

The application has three main layers: repository for data access, service for business logic, and controller for handling restful HTTP requests. Other than these modules, it has config, filter, model, error, etc. modules. Shortly, the application is split into modules for the ease of development and separation of concerns.

So, _Spring Data JPA, JdbcTemplate, Lombok, Servlet Filter_ are used.

### Modules in the app:

![image-20230824171726848](https://github.com/nhtY/Token-Filter/assets/89942570/68354cdf-ebff-4da1-96f9-741ea29d6722)


### General view of the http methods

![image-20230824172134302](https://github.com/nhtY/Token-Filter/assets/89942570/cff33f55-f803-472d-aac6-89116eafdc74)


# FRONTEND

The frontend is developed using **React**. State management is done by using **Redux Tool-Kit** with **asyncThunks**. **Axios** is used for API calls.
**Bootstrap** is used to make the design a bit attractive.

## Landing page

![image-20230824170608891](https://github.com/nhtY/Token-Filter/assets/89942570/228408c5-56ac-4d83-8f2d-3d5f10a342c3)



## Login Page

![image-20230824170526848](https://github.com/nhtY/Token-Filter/assets/89942570/4c22478b-c49a-48ce-a700-15f721eb8b5f)


## Register Page

![image-20230824170642562](https://github.com/nhtY/Token-Filter/assets/89942570/3b5570e5-d3ab-4d8d-92f1-b53a424d4030)

## Profile

![image-20230824170717859](https://github.com/nhtY/Token-Filter/assets/89942570/d05a19c1-7f0d-4749-9d3f-108bd743145d)


### Profile - Edit

![image-20230824170751488](https://github.com/nhtY/Token-Filter/assets/89942570/8bbbb3c0-cf0c-4a2e-83d2-1287793e854a)

## User Operations

![image-20230824170946649](https://github.com/nhtY/Token-Filter/assets/89942570/23580668-85fa-4de9-9e1c-82874787d7ae)

**When opening user home page, if the token is expired, user is forced to log out automatically.**


![image-20230824171213557](https://github.com/nhtY/Token-Filter/assets/89942570/df753645-40d5-4ef9-9f8e-f4faf5b53a7f)

---



