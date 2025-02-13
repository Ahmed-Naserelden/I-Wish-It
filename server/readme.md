# Server Communication Information

## Server Response Status
 - `true`: Successful
 - `false`: Failure (for some reason)

## API Schema Template

URL `{METHOD} {URL}`

Request Payload
  - {var1} `<{datatype}>`
    - {sub1} `<dataType>`
    - ...
  - {variable2} `<{dataType}>`
  - ...

Response Payload
  - {var1} `<{datatype}>`
    - {sub1} `<dataType>`
    - ...
  - {variable2} `<{dataType}>`
  - ...

## User Authentication

URL `POST /api/auth/signin`

Request Payload
  - signInRequest `<SignInRequest>`
    - email `<String>`
    - password `<String>`

Response Payload
  - user `<User>`
    - userId `<int>`
    - email `<String>`
    - username `<String>`
    - password `<String>`
    - dob `<String>` (Date of Birth)
    - balance `<int>`

## Create new account

URL `POST /api/auth/signup`

Request Payload
  - signUpRequest `<SignUpRequest>`
    - email `<String>`
    - username `<String>`
    - password `<String>`
    - dob `<String>`

Response Payload `<null>`

## Marketplace Items

URL `GET /api/marketplace`

Request Payload `<null>`

Response Payload
  - products `<ArrayList<Product>>`

## Friendlist

URL `GET /api/friends`

Request Payload
  - userId `<int>`

Response Payload
  - friends `<ArrayList<Friend>>`
    - userId `<int>`
    - username `<String>`
    - dob `<String>`