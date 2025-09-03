package com.dynata.surveyhw.configurations;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilterConfiguration implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String ALLOWED_ORIGINS = "*"; // ha kell cookie/cred, ezt eredetre kell cserélni és Allow-Credentials=true
    private static final String ALLOWED_HEADERS = "Origin, Content-Type, Accept, Authorization";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            Response.ResponseBuilder rb = Response.ok()
                    .header("Access-Control-Allow-Origin", ALLOWED_ORIGINS)
                    .header("Access-Control-Allow-Methods", ALLOWED_METHODS)
                    .header("Access-Control-Allow-Headers", ALLOWED_HEADERS)
                    .header("Access-Control-Max-Age", "86400");
            requestContext.abortWith(rb.build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", ALLOWED_ORIGINS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", ALLOWED_METHODS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        //        responseContext.getHeaders().putSingle("Access-Control-Expose-Headers", "Location"); // ha kell
        // responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true"); // csak ha NEM '*' az Origin!
    }
}
