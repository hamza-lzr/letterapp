package ma.ensa.lettersapp.controllers;

import ma.ensa.lettersapp.DTO.AuthRequest;
import ma.ensa.lettersapp.DTO.AuthResponse;
import ma.ensa.lettersapp.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        final String jwt = authService.login(authRequest);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        authService.register(authRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}
