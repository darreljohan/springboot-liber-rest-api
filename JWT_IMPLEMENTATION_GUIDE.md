# JWT Authentication Implementation Guide

> If you're new to Spring Security or JWT, start with `JWT_STUDENT_GUIDE.md` for a slower, beginner-friendly walkthrough.

## Overview
This guide explains how to implement JWT (JSON Web Token) authentication in your Spring Boot application. Currently, your application uses HTTP Basic Authentication. We'll upgrade it to use JWT tokens for stateless authentication.

---

## What is JWT?

JWT (JSON Web Token) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object.

### JWT Structure
A JWT consists of three parts separated by dots (.):
```
xxxxx.yyyyy.zzzzz
```

1. **Header**: Contains token type (JWT) and signing algorithm (e.g., HS256, RS256)
2. **Payload**: Contains claims (user data, expiration time, etc.)
3. **Signature**: Ensures token hasn't been tampered with

Example JWT:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

---

## Current vs JWT Authentication Flow

### Current Flow (HTTP Basic)
1. Client sends username:password in Authorization header (Base64 encoded)
2. Server validates credentials on **every request**
3. Server checks permissions and returns response

**Disadvantages:**
- Credentials sent with every request (security risk)
- Database lookup on every request (performance impact)
- No token expiration

### New Flow (JWT)
1. **Login**: Client sends credentials → Server validates → Returns JWT token
2. **Subsequent Requests**: Client sends JWT in Authorization header
3. Server validates JWT signature and extracts user info (no database lookup needed)
4. Server checks permissions and returns response

**Advantages:**
- Credentials sent only during login
- Stateless (no server-side session storage)
- Token expiration and refresh mechanism
- Better performance (no DB lookup per request)

---

## Detailed Implementation Flow

### Flow Diagram
```
┌─────────────┐                           ┌─────────────┐
│   CLIENT    │                           │   SERVER    │
└──────┬──────┘                           └──────┬──────┘
       │                                         │
       │  1. POST /auth/login                    │
       │     {username, password}                │
       ├────────────────────────────────────────>│
       │                                         │
       │                     2. Validate credentials
       │                        (AuthenticationManager)
       │                                         │
       │                     3. Generate JWT Token
       │                        (JwtService)     │
       │                                         │
       │  4. Return {token, expiresIn}           │
       │<────────────────────────────────────────┤
       │                                         │
       │  5. GET /authors (with token)           │
       │     Header: Authorization: Bearer xxx   │
       ├────────────────────────────────────────>│
       │                                         │
       │                     6. Extract JWT from header
       │                        (JwtAuthenticationFilter)
       │                                         │
       │                     7. Validate token & extract username
       │                        (JwtService)     │
       │                                         │
       │                     8. Load user details
       │                        (AuthUserDetailService)
       │                                         │
       │                     9. Set SecurityContext
       │                        (JwtAuthenticationFilter)
       │                                         │
       │                    10. Check permissions
       │                        (SecurityFilterChain)
       │                                         │
       │  11. Return response                    │
       │<────────────────────────────────────────┤
       │                                         │
```

---

## Components to Implement

### 1. **JwtService** (Core JWT Logic)
**Location**: `com.iglo.exam.liber.auth.JwtService`

**Responsibilities:**
- Generate JWT tokens
- Extract username from token
- Validate token (signature, expiration)
- Extract claims from token

**Key Methods:**
```java
- String generateToken(UserDetails userDetails)
- String extractUsername(String token)
- boolean isTokenValid(String token, UserDetails userDetails)
- Date extractExpiration(String token)
```

**Configuration Properties:**
- `jwt.secret-key`: Secret key for signing tokens (store in application.properties)
- `jwt.expiration`: Token expiration time in milliseconds (e.g., 86400000 = 24 hours)

---

### 2. **JwtAuthenticationFilter** (Request Interceptor)
**Location**: `com.iglo.exam.liber.auth.JwtAuthenticationFilter`

**Responsibilities:**
- Intercept every HTTP request
- Extract JWT from Authorization header
- Validate token using JwtService
- Load user details and set SecurityContext

**Execution Flow:**
1. Check if Authorization header exists and starts with "Bearer "
2. Extract token from header (remove "Bearer " prefix)
3. Extract username from token
4. If username exists and user not already authenticated:
   - Load UserDetails from database
   - Validate token
   - Create Authentication object
   - Set in SecurityContext
5. Continue filter chain

---

### 3. **AuthController** (Login Endpoint)
**Location**: `com.iglo.exam.liber.auth.AuthController`

**Responsibilities:**
- Handle login requests
- Authenticate user
- Return JWT token on successful authentication

**Endpoints:**
```java
POST /auth/login
Request: { "username": "user1", "password": "root" }
Response: { "token": "eyJhbGc...", "expiresIn": 86400000, "username": "user1" }
```

---

### 4. **Update AuthConfiguration** (Security Config)
**Location**: `com.iglo.exam.liber.auth.AuthConfiguration`

**Changes:**
- Remove `.httpBasic()` configuration
- Add `JwtAuthenticationFilter` before UsernamePasswordAuthenticationFilter
- Add `/auth/login` to permitAll() endpoints
- Configure AuthenticationManager bean

**Filter Order:**
```
Request → JwtAuthenticationFilter → UsernamePasswordAuthenticationFilter → Controller
```

---

## Step-by-Step Implementation

### Step 1: Add JWT Dependencies
Add to `pom.xml`:
```xml
<!-- JWT Library -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

### Step 2: Configure JWT Properties
Add to `application.properties`:
```properties
# JWT Configuration
jwt.secret-key=your-secret-key-at-least-256-bits-long-for-HS256-algorithm
jwt.expiration=86400000
# 86400000 ms = 24 hours
```

### Step 3: Create JwtService
This service handles all JWT operations (generation, validation, extraction).

### Step 4: Create JwtAuthenticationFilter
This filter intercepts requests and validates JWT tokens.

### Step 5: Create DTOs
- `AuthLoginRequest`: Login request DTO
- `AuthLoginResponse`: Login response DTO with token

### Step 6: Update AuthController
Add login endpoint that returns JWT token.

### Step 7: Update AuthConfiguration
- Register JwtAuthenticationFilter
- Configure AuthenticationManager
- Update security rules

---

## Testing the Implementation

### 1. Login Request
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "root"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000,
  "username": "user1"
}
```

### 2. Access Protected Resource
```bash
GET http://localhost:8080/authors
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Author Name",
    ...
  }
]
```

### 3. Test Token Expiration
Wait for token to expire or use expired token → Should get 401 Unauthorized

### 4. Test Invalid Token
Use modified/invalid token → Should get 401 Unauthorized

---

## Security Best Practices

1. **Secret Key**: Use a strong, random secret key (at least 256 bits for HS256)
   - Generate using: `openssl rand -base64 32`
   - Store in environment variables, not in code

2. **Token Expiration**: Set reasonable expiration time
   - Short-lived access tokens (15 minutes - 24 hours)
   - Implement refresh tokens for longer sessions

3. **HTTPS**: Always use HTTPS in production to prevent token interception

4. **Token Storage** (Client-side):
   - Store in memory (most secure, lost on page refresh)
   - LocalStorage (convenient but vulnerable to XSS)
   - HttpOnly cookies (secure, prevents XSS)

5. **Token Validation**: Always validate:
   - Signature
   - Expiration time
   - Issuer (if applicable)
   - User still exists and is active

6. **Error Handling**: Don't reveal sensitive information in error messages

---

## Common Issues and Solutions

### Issue 1: Token not extracted
**Problem**: Filter doesn't extract token from header
**Solution**: Ensure header format is exactly: `Authorization: Bearer <token>`

### Issue 2: Invalid signature
**Problem**: Token validation fails
**Solution**: 
- Ensure secret key is same for generation and validation
- Check token hasn't been modified

### Issue 3: Token expired
**Problem**: 401 Unauthorized after some time
**Solution**: 
- Implement token refresh mechanism
- Increase expiration time (for development)

### Issue 4: User not found
**Problem**: Token valid but user not loaded
**Solution**: 
- Ensure username in token matches database
- Check user is not deactivated

---

## Advanced Features (Optional)

### 1. Refresh Token
Implement long-lived refresh tokens to get new access tokens without re-login.

### 2. Token Blacklist
Store invalidated tokens (logout) in Redis or database.

### 3. Role-based Claims
Add user roles/permissions in JWT payload for faster authorization.

### 4. Multiple Token Types
- Access Token: Short-lived, for API access
- Refresh Token: Long-lived, to get new access tokens
- Remember Me Token: Very long-lived

### 5. Token Revocation
Implement mechanism to invalidate tokens before expiration.

---

## File Structure After Implementation

```
auth/
├── AuthConfiguration.java (updated)
├── AuthController.java (updated)
├── AuthUserDetailService.java (existing)
├── AuthUserDetails.java (existing)
├── JwtService.java (NEW)
├── JwtAuthenticationFilter.java (NEW)
├── dto/
│   ├── AuthLoginRequest.java (NEW)
│   └── AuthLoginResponse.java (NEW)
└── handler/
    ├── CustomAuthenticationEntryPoint.java (existing)
    └── CustomAccessDeniedHandler.java (existing)
```

---

## Summary

JWT authentication provides a modern, stateless approach to securing your Spring Boot application. The implementation involves:

1. **Client authenticates** with username/password
2. **Server generates JWT** token containing user information
3. **Client stores token** and includes it in subsequent requests
4. **Server validates token** on each request without database lookup
5. **Token expires** after configured time for security

This approach is scalable, performant, and widely used in modern web applications, especially for RESTful APIs and microservices architectures.

---

## Next Steps

1. Add JWT dependencies to pom.xml
2. Create JwtService for token operations
3. Create JwtAuthenticationFilter for request interception
4. Update AuthController with login endpoint
5. Update AuthConfiguration to use JWT filter
6. Test with Postman or similar tool
7. (Optional) Implement refresh token mechanism
8. (Optional) Add token blacklist for logout

Good luck with your implementation! 🚀
