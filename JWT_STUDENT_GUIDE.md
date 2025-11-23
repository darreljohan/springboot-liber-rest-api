# Spring Security & JWT – A Beginner-Friendly Implementation Guide

Welcome! This guide is written for you if you're just starting with Spring Security and want to add JWT (JSON Web Token) authentication to your existing codebase. We'll go slowly, explain the fundamentals, *why* things matter, and show you exactly how each piece fits together.

---
## 1. Big Picture: What Problem Are We Solving?
Your application needs to know "Who is making this request?" and "Are they allowed to do that?". This breaks down into:
- **Authentication**: Verifying identity ("Is this really Alice?")
- **Authorization**: Checking access ("Can Alice delete an author?")

Spring Security provides the framework (filters, context, error handling). JWT provides a *stateless* way to carry identity + roles from the client to the server **without storing session data**.

---
## 2. How Spring Security Works (Fundamentals)
Spring Security puts a **filter chain** in front of every request. Important parts:

| Concept | Description |
|---------|-------------|
| Filter Chain | Ordered list of filters that process the request before it reaches your controller |
| `SecurityContext` | Holds the current authenticated user's details |
| `Authentication` | Object representing the user's identity + authorities |
| `UserDetailsService` | Your implementation that loads a user from DB given a username |
| `PasswordEncoder` | Encodes and verifies passwords (e.g. BCrypt) |
| `AuthenticationManager` | Coordinates authentication logic |
| Entry Point | Handles 401 (unauthenticated) errors |
| AccessDeniedHandler | Handles 403 (forbidden) errors |

### What Happens During a Protected Request (After Login)
1. Request enters filter chain.
2. Your custom `JwtAuthenticationFilter` runs early, looks for `Authorization: Bearer <token>`.
3. If token exists: validate signature + expiration, extract username.
4. Load user via `AuthUserDetailService` → returns `UserDetails`.
5. Build an `Authentication` object and put it into `SecurityContextHolder`.
6. Downstream filters and your controller now "see" an authenticated user.

If anything fails (no token / invalid / expired), the request proceeds unauthenticated → Spring Security will block access if endpoint requires authentication.

---
## 3. Why Not Just Use Basic Auth?
| Basic Auth | JWT |
|------------|-----|
| Sends username+password EVERY request | Sends token AFTER login only |
| Credentials can be leaked easily | Token can be revoked / rotated |
| No built-in expiration | Token has `exp` claim |
| Needs DB lookup each time | Only DB lookup on first login (can still re-check user state) |
| Poor scalability for microservices | Excellent for distributed stateless systems |

---
## 4. What Is a JWT?
A JWT is a compact string like: `header.payload.signature`

Example (formatted):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.
eyJzdWIiOiJhbGljZSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3MzIzMzU2MDAsImV4cCI6MTczMjQyMjAwMH0
.
SOME_SIGNATURE_HERE
```

### Sections
- **Header**: `{ "alg": "HS256", "typ": "JWT" }`
- **Payload (Claims)**: Subject (username), roles, issue time, expiration, etc.
- **Signature**: Verifies token wasn't tampered with.

We use HMAC (HS256) here – a shared secret. Production apps often use **RSA** (public/private key) for more flexible/secure signing.

### Important Claims
| Claim | Meaning |
|-------|---------|
| `sub` | Subject / username |
| `iat` | Issued At timestamp |
| `exp` | Expiration timestamp (token invalid after this) |
| Custom `roles` | Your app's authorities |

---
## 5. Where JWT Fits Into Your Existing Project
You already have:
- `AuthUserDetailService` – loads user from DB
- `AuthUserDetails` – wraps your `User`
- `AuthConfiguration` – configures endpoints + rules

We add:
- `JwtService` – generate & validate tokens
- `JwtAuthenticationFilter` – plug into filter chain
- DTOs (`AuthLoginRequest`, `AuthLoginResponse`)
- Login endpoint in controller
- Properties (`jwt.secret-key`, `jwt.expiration`)

---
## 6. Implementation Steps (Beginner Friendly)

### Step A: Add Maven Dependencies (pom.xml)
```xml
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
Run: `mvn clean compile` to pull them.

### Step B: Add Properties (application.properties)
```properties
jwt.secret-key=ChangeThisSecretKeyToAStrongRandomValueOfAtLeast32Chars123!
jwt.expiration=86400000  # 24h in milliseconds
```
Use a real secret in production: `openssl rand -base64 48`.

### Step C: `JwtService` (Core Token Logic)
Responsibilities: build, sign, parse, validate token.
Key ideas:
- Use the secret key to sign (HS256)
- Put roles into a claim to rehydrate authorities quickly.

Simplified snippet:
```java
String generateToken(UserDetails userDetails) {
  Date now = new Date();
  Date exp = new Date(now.getTime() + expirationMillis);
  return Jwts.builder()
    .subject(userDetails.getUsername())
    .issuedAt(now)
    .expiration(exp)
    .claim("roles", userDetails.getAuthorities().stream()
             .map(GrantedAuthority::getAuthority).toList())
    .signWith(key, Jwts.SIG.HS256)
    .compact();
}
```

### Step D: `JwtAuthenticationFilter`
Runs once per request. Looks for header:
```
Authorization: Bearer <token>
```
If valid → creates an `Authentication` and places it into the `SecurityContext`.

Pseudo-flow:
```java
if(header startsWith("Bearer ")) {
  String token = header.substring(7);
  String username = jwtService.extractUsername(token);
  if(username not null && SecurityContext empty) {
     UserDetails ud = userDetailsService.loadUserByUsername(username);
     if(jwtService.isTokenValid(token, ud)) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
     }
  }
}
continue chain
```

### Step E: DTOs for Login
```java
class AuthLoginRequest { String username; String password; }
class AuthLoginResponse { String username; String token; long expiresIn; List<String> roles; }
```

### Step F: Login Endpoint
Controller method authenticates credentials → returns token.
```java
@PostMapping("/login")
public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest req) {
  authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
  UserDetails ud = userDetailsService.loadUserByUsername(req.getUsername());
  String token = jwtService.generateToken(ud);
  return ResponseEntity.ok(AuthLoginResponse.builder()
      .username(ud.getUsername())
      .token(token)
      .expiresIn(jwtService.getExpirationMillis())
      .roles(ud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
      .build());
}
```

### Step G: Security Configuration (`AuthConfiguration`)
- Permit the login endpoint.
- Add the JWT filter BEFORE `UsernamePasswordAuthenticationFilter`.
- Remove `httpBasic()`.

Snippet:
```java
http.csrf(AbstractHttpConfigurer::disable)
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/login").permitAll()
        .requestMatchers(HttpMethod.GET, "/authors", "/authors/*").hasAnyRole("USER","ADMIN")
        .anyRequest().authenticated())
    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .exceptionHandling(ex -> ex
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler))
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

---
## 7. Passwords & Your Seed Data
Your `data.sql` uses `{noop}Password1!` style values. Since you switched to `BCryptPasswordEncoder`, those users will fail login.

Fix options:
1. Replace with BCrypt hashes. Example for `Password1!`:
   - Generate using a quick Java snippet or an online BCrypt generator.
   - Example hash: `$2a$10$Vb3QJqYq8KkHn1L6c9mtY.6kJgE2B1wHfMjKjQZQ.9GfQ3LZxkYAG`
   - Update SQL: `password = '$2a$10$Vb3QJqYq8KkHn1L6c9mtY.6kJgE2B1wHfMjKjQZQ.9GfQ3LZxkYAG'`
2. Use `DelegatingPasswordEncoder` instead:
```java
@Bean PasswordEncoder passwordEncoder() {
  return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```
Then keep `{noop}` style for dev (NOT for prod).

Recommended: Re-register users using the existing `/users` POST endpoint → passwords get encoded cleanly.

---
## 8. Testing With curl or Postman (Windows PowerShell)
Login:
```powershell
curl -Method POST http://localhost:8080/api/v1/auth/login `
  -H 'Content-Type: application/json' `
  -Body '{"username":"alice","password":"Password1!"}'
```
Response (example):
```json
{
  "username": "alice",
  "token": "<JWT>",
  "expiresIn": 86400000,
  "roles": ["ROLE_USER"]
}
```
Use token:
```powershell
$token = '<JWT>'
curl http://localhost:8080/api/v1/authors -H "Authorization: Bearer $token"
```

Expired / invalid token ⇒ 401 (handled by `CustomAuthenticationEntryPoint`).
Wrong role ⇒ 403 (handled by `CustomAccessDeniedHandler`).

---
## 9. Debugging Tips
| Symptom | Check |
|---------|-------|
| Always 401 | Is token missing / wrong header name? Should be `Authorization` |
| Token works once then fails | Clock skew? Expiration too short? System time correct? |
| Roles not applied | Did you store `roles` claim? Do they start with `ROLE_`? |
| Users with old passwords can't login | Are they BCrypt encoded now? |
| NullPointer in filter | Is `JwtAuthenticationFilter` registered as a bean (`@Component`)? |

Enable verbose logging (already partially done):
```properties
logging.level.org.springframework.security=DEBUG
```
Look at startup logs to confirm filter order.

---
## 10. Security Best Practices (Beginner Friendly)
1. **Never hardcode real secrets** – use environment variables.
2. **Rotate secrets** – if leaked, generate a new one, invalidate old tokens.
3. **Shorter access tokens + refresh tokens** – start with only access tokens; add refresh later.
4. **HTTPS only** – tokens over plain HTTP can be intercepted.
5. **Least privilege** – keep roles granular.
6. **Validate user still active** – optional: after parsing token, check user isn't deactivated.
7. **Logout / Revocation** – JWT by default is non-revocable until expiry; add a denylist if needed.

---
## 11. Common Pitfalls
| Pitfall | Why it hurts |
|---------|--------------|
| Using plain text passwords | Compromise risk |
| Very long token lifetime (days/weeks) | Hard to revoke, attack window large |
| Putting sensitive data (email, PII) in JWT | JWT payload is readable (Base64URL, not encrypted) |
| Returning token in a query param | Can end up in logs / browser history |
| Storing token in localStorage blindly | Vulnerable to XSS |

---
## 12. Optional: Refresh Token Pattern (Concept Only)
- Issue short-lived access token (e.g., 15 min) + long-lived refresh token (e.g., 7 days).
- Client stores refresh token securely (often HttpOnly cookie).
- When access token expires, client calls `/auth/refresh` with refresh token → server validates and issues a new access token.
- Keep a store of refresh tokens (DB / Redis) so they can be revoked.

Not needed for first implementation; master basics first.

---
## 13. File Overview (After Adding JWT)
```
auth/
  AuthConfiguration.java          // security rules, filter registration
  authController.java             // login endpoint + sample
  JwtService.java                 // create/parse/validate tokens
  JwtAuthenticationFilter.java    // extracts token each request
  AuthUserDetailService.java      // loads user from DB
  AuthUserDetails.java            // wraps User entity
  dto/
    AuthLoginRequest.java         // login request body
    AuthLoginResponse.java        // response with token
  handler/
    CustomAuthenticationEntryPoint.java  // sends 401 JSON
    CustomAccessDeniedHandler.java       // sends 403 JSON
```

---
## 14. Mental Model Recap
1. User sends credentials → server authenticates once.
2. Server issues signed JWT containing identity + roles + expiry.
3. Client stores token and attaches it to future requests in header.
4. Server validates signature & expiry → rebuilds Authentication.
5. Controllers trust the `SecurityContext`.
6. No server session – stateless, scalable.

---
## 15. If Something Breaks – Checklist
1. Did the login endpoint return a token? If not, check AuthenticationManager wiring.
2. Is the Authorization header EXACTLY `Bearer <token>`?
3. Does the token decode to the right `sub` (use jwt.io)?
4. Does your secret match on both generation & parsing? (Restart after changing.)
5. Are roles present? Filter expects `ROLE_` prefix in authorities.
6. Are you hitting the right URL with context-path? (`/api/v1/auth/login` vs `/auth/login`).

---
## 16. Practice Exercises (Try These!)
1. Add a new endpoint `/me` that returns current username & roles from the `SecurityContext`.
2. Restrict `/users` listing to ADMIN only.
3. Add a second claim `displayName` and show it in the login response.
4. Shorten token lifetime to 60 seconds and observe expiration behavior.
5. Add a simple in-memory blacklist for a manual logout experiment.

---
## 17. Final Advice
Learn step-by-step: *Passwords → Authentication → Authorization → Stateless tokens → Advanced features*. Don't rush into refresh tokens or key rotation until the basics feel comfortable.

You're on the right track—each piece you added (service, filter, configuration) maps to a clear responsibility. Keep responsibilities separated and code stays maintainable.

---
## 18. Quick Reference (Cheat Sheet)
| Action | Header / Command |
|--------|------------------|
| Login | `POST /api/v1/auth/login` |
| Auth header | `Authorization: Bearer <token>` |
| Decode token | Paste into https://jwt.io |
| Check roles | Inspect `roles` claim |
| Expiry issue | Compare `exp` vs current epoch |

Epoch time helper (Java): `Instant.now().getEpochSecond()`

---
### Done! You now have JWT-based auth integrated.
Feel free to extend this with refresh tokens, logout, and role hierarchies once you're comfortable.

Happy coding! 🚀

