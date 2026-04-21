package com.bhasaka.ogani.core.servlets;

import com.bhasaka.ogani.core.services.CfSearchService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.JsonArray;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Content Fragment Search Suggestion Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=/bin/api/cfsuggestions"
})
public class CfSuggestionServlet extends SlingSafeMethodsServlet {

    @Reference
    private CfSearchService cfSearchService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        String searchTerm = request.getParameter("q");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (searchTerm == null || searchTerm.length() < 2) {
            response.getWriter().write("[]");
            return;
        }

        JsonArray suggestions = cfSearchService.getSuggestions(searchTerm, request.getResourceResolver());

        response.getWriter().write(suggestions.toString());
    }
}