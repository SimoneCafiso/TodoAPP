package engineer.cafisodevs.todoapp.authentication.controller;

import engineer.cafisodevs.todoapp.account.EntityUser;
import engineer.cafisodevs.todoapp.authentication.components.AuthenticationUtilities;
import engineer.cafisodevs.todoapp.authentication.dto.AuthenticationRequest;
import engineer.cafisodevs.todoapp.authentication.dto.SignUPRequest;
import engineer.cafisodevs.todoapp.exceptions.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationUtilities authenticationUtilities;


    @PostMapping("/signUP")
    private ResponseEntity<Object> signUP(@RequestBody SignUPRequest signUPRequest) {
        EntityUser newUser = null;

        try {
            newUser = authenticationUtilities.signUP(signUPRequest);

            if (newUser == null) {
                throw new AuthenticationException("An error occurred while signing up");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok("Successfully SignedUP");
    }


    @PostMapping("/login")
    private ResponseEntity<Object> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationUtilities.authenticate(authenticationRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            authenticationUtilities.refreshToken(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
