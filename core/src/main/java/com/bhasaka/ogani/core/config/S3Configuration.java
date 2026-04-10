package com.bhasaka.ogani.core.config;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Ogani S3 Configuration"
)
public @interface S3Configuration {

    @AttributeDefinition(name = "Access Key")
    String accessKey();

    @AttributeDefinition(name = "Secret Key")
    String secretKey();

    @AttributeDefinition(name = "Bucket Name")
    String bucketName();

    @AttributeDefinition(name = "Region")
    String region() default "ap-south-1";

    @AttributeDefinition(name = "Base Folder")
    String baseFolder() default "contact-form";
}