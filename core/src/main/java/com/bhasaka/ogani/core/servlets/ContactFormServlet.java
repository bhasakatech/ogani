package com.bhasaka.ogani.core.servlets;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bhasaka.ogani.core.services.S3UploadService;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=Ogani/components/contact-form",
        "sling.servlet.methods=POST",
        "sling.servlet.extensions=json"
    }
)
public class ContactFormServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(ContactFormServlet.class);

    @Reference
    private S3UploadService s3UploadService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        if (name == null || email == null || message == null ||
            name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Please fill all fields.\"}");
            return;
        }
         
        String jsonData = String.format(
        "{\"name\":\"%s\",\"email\":\"%s\",\"message\":\"%s\"}",
        escapeJson(name),
        escapeJson(email),
        escapeJson(message)
);

        try {
           
            s3UploadService.uploadToS3(jsonData);

            log.info("Form submission uploaded successfully for user: {}", email);

            response.getWriter().write("{\"status\":\"success\",\"message\":\"Form submitted successfully\"}");

        } catch (Exception e) {
            log.error("Failed to upload form data to S3", e);
            response.setStatus(500);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Submission failed.\"}");
        }
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
}
}