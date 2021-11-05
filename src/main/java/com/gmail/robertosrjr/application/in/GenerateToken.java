package com.gmail.robertosrjr.application.in;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import javax.enterprise.context.RequestScoped;
import java.util.Arrays;
import java.util.HashSet;

@RequestScoped
public class GenerateToken {

    /**
     * Generate JWT token
     */
    public String generate() {

        String token = "";
        try {

            token =
                    Jwt.issuer("https://example.com/issuer")
                            .upn("robertosrjr@gmail.com")
                            .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                            .claim(Claims.birthdate.name(), "1980-12-16")
                            .sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}
