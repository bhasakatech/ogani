package com.bhasaka.ogani.core.services.Impl;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bhasaka.ogani.core.config.S3Configuration;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
class S3UploadServiceImplTest {

    private S3UploadServiceImpl service;
    private AmazonS3 s3Client;

    @BeforeEach
    void setUp() throws Exception {
        service = new S3UploadServiceImpl();
        s3Client = mock(AmazonS3.class);

        setField("s3Client", s3Client);
        setField("bucketName", "test-bucket");
        setField("baseFolder", "contact-form");
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = S3UploadServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }

    @Test
    void testUploadToS3() {
        when(s3Client.doesObjectExist(anyString(), anyString()))
                .thenReturn(false);

        service.uploadToS3("{\"name\":\"Sweety\"}");

        verify(s3Client, times(1))
                .putObject(anyString(), anyString(), anyString());
    }

  @Test
void testUploadToS3WhenFileExists() throws Exception {
    when(s3Client.doesObjectExist(anyString(), anyString()))
            .thenReturn(true);

    S3Object s3Object = mock(S3Object.class);

    String existingJson = "[{\"name\":\"old\"}]";

    ByteArrayInputStream inputStream =
            new ByteArrayInputStream(existingJson.getBytes());

    S3ObjectInputStream s3InputStream =
            new S3ObjectInputStream(inputStream, null);

    when(s3Client.getObject(anyString(), anyString()))
            .thenReturn(s3Object);

    when(s3Object.getObjectContent())
            .thenReturn(s3InputStream);

    service.uploadToS3("{\"name\":\"new\"}");

    verify(s3Client, times(1))
            .putObject(anyString(), anyString(), contains("new"));
}
@Test
void testUploadToS3Failure() {
    when(s3Client.doesObjectExist(anyString(), anyString()))
            .thenThrow(new RuntimeException("S3 error"));

    assertThrows(RuntimeException.class, () ->
            service.uploadToS3("{\"name\":\"test\"}")
    );
}
@Test
void testActivate() {
    S3Configuration config = mock(S3Configuration.class);

    when(config.accessKey()).thenReturn("test-access");
    when(config.secretKey()).thenReturn("test-secret");
    when(config.bucketName()).thenReturn("test-bucket");
    when(config.region()).thenReturn("ap-south-1");
    when(config.baseFolder()).thenReturn("contact-form");

    assertDoesNotThrow(() -> service.activate(config));
}

@Test
void testUploadToS3WithEmptyExistingFile() throws Exception {
    when(s3Client.doesObjectExist(anyString(), anyString()))
            .thenReturn(true);

    S3Object s3Object = mock(S3Object.class);

    ByteArrayInputStream inputStream =
            new ByteArrayInputStream("[]".getBytes());

    S3ObjectInputStream s3InputStream =
            new S3ObjectInputStream(inputStream, null);

    when(s3Client.getObject(anyString(), anyString()))
            .thenReturn(s3Object);

    when(s3Object.getObjectContent())
            .thenReturn(s3InputStream);

    service.uploadToS3("{\"name\":\"new\"}");

    verify(s3Client).putObject(anyString(), anyString(), contains("new"));
}

@Test
void testUploadToS3ExceptionFlow() {
    when(s3Client.doesObjectExist(anyString(), anyString()))
            .thenThrow(new RuntimeException("AWS error"));

    RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.uploadToS3("{\"name\":\"test\"}")
    );

    assertTrue(ex.getMessage().contains("S3 upload failed"));
}
}