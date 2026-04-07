   
package com.bhasaka.ogani.core.servlets;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhasaka.ogani.core.services.S3UploadService;

class ContactFormServletTest {

    private ContactFormServlet servlet;
    private S3UploadService s3UploadService;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new ContactFormServlet();

        s3UploadService = mock(S3UploadService.class);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        // inject mocked service
        Field field = ContactFormServlet.class.getDeclaredField("s3UploadService");
        field.setAccessible(true);
        field.set(servlet, s3UploadService);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(request.getParameter("name")).thenReturn("Sweety");
        when(request.getParameter("email")).thenReturn("sweety@gmail.com");
        when(request.getParameter("message")).thenReturn("Hello");

        servlet.doPost(request, response);

        verify(s3UploadService, times(1)).uploadToS3(anyString());

        String output = responseWriter.toString();
        assertTrue(output.contains("success"));
    }

    @Test
    void testDoPostValidationFailure() throws Exception {
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("email")).thenReturn("abc@gmail.com");
        when(request.getParameter("message")).thenReturn("Hello");

        servlet.doPost(request, response);

        verify(response).setStatus(400);

        String output = responseWriter.toString();
        assertTrue(output.contains("Please fill all fields"));
    }

    @Test
    void testDoPostServiceFailure() throws Exception {
        when(request.getParameter("name")).thenReturn("Sweety");
        when(request.getParameter("email")).thenReturn("sweety@gmail.com");
        when(request.getParameter("message")).thenReturn("Hello");

        doThrow(new RuntimeException("S3 failed"))
                .when(s3UploadService)
                .uploadToS3(anyString());

        servlet.doPost(request, response);

        verify(response).setStatus(500);

        String output = responseWriter.toString();
        assertTrue(output.contains("Submission failed"));
    }
}