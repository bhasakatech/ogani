package com.bhasaka.ogani.core.servlets;

import com.bhasaka.ogani.core.services.QuoteService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Servlet endpoint that returns quote data as JSON.
 */
@Component(service = Servlet.class)
@SlingServletPaths("/bin/quote")
public class QuoteServlet extends SlingSafeMethodsServlet {

    @Reference
    private QuoteService quoteService;

    /**
     * Handles HTTP GET requests for the quote endpoint.
     *
     * @param request the current Sling request
     * @param response the Sling response used to return JSON
     * @throws ServletException if servlet processing fails
     * @throws IOException if writing the response fails
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.getWriter().write(quoteService.getResponse().isBlank() ? "[]" : quoteService.getResponse());
        } catch (IllegalStateException e) {
            throw new ServletException("Response writer is not available", e);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Unexpected error while processing quote request", e);
        }
    }
}
