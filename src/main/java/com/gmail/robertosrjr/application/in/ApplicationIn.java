package com.gmail.robertosrjr.application.in;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ApplicationIn {

    @Inject
    private GenerateToken generateToken;

    public String generateToken() {

        return generateToken.generate();
    }
}
