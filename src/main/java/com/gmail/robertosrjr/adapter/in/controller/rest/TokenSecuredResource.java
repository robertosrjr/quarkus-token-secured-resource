package com.gmail.robertosrjr.adapter.in.controller.rest;

import com.gmail.robertosrjr.application.in.ApplicationIn;
import com.gmail.robertosrjr.application.in.GenerateToken;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Path("/secured")
@RequestScoped
public class TokenSecuredResource {

    private final Logger logger = Logger.getLogger(TokenSecuredResource.class);

    @Inject
    private JsonWebToken jwt;

    @Inject
    private ApplicationIn applicationIn;

    @GET
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        return getResponseString(ctx);
    }

    @GET
    @Path("roles-allowed")
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString();
    }


    @GET
    @Path("generateToken")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken() {
        return this.applicationIn.generateToken();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();

            Calendar agora = Calendar.getInstance();
            Calendar expire = Calendar.getInstance();
            expire.setTimeInMillis(Long.valueOf(jwt.getExpirationTime()*1000));

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            logger.infof("Agora: %s - Expira: %s ", df.format(agora.getTime()), df.format(expire.getTime()));
        }
        return String.format("hello + %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}
