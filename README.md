# black-ninja
auth server for user registration and on login provided a JWT token containing information

# Notes
1. When you are using this need to change RSA keys
2. Currently deployed at heroku https://black-ninja.herokuapp.com
3. Register user POST API: https://black-ninja.herokuapp.com/api/user with body as 
```json
  {
    "firstname":"Rekha",
    "lastname":"Menon",
    "email":"rekhaMenon91@mail.com",
    "phone":"9876890156",
    "companyName":"HI",
    "password":"password"
  }
  ```
4. Login for a user POST API:  https://black-ninja.herokuapp.com/api/user/login
```json
  {
    "email":"rekhaMenon91@mail.com",
    "password":"password",
    "companyName":"HI"
  }
  ```
  returns access token and other details :
  ```json
  {
    "accessToken": "access-token",
    "tokenType": "bearer",
    "expiresIn": 1800
  }
```
