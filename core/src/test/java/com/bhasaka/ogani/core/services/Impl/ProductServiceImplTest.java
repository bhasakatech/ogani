package com.bhasaka.ogani.core.services.Impl;

import com.bhasaka.ogani.core.models.productCarousel.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.sling.api.resource.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ResourceResolver resolver;

    private final ObjectMapper mapper = new ObjectMapper();

    //1. FULL JSON FLOW TEST
    @Test
    void testGetProducts_FromJson() throws Exception {

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("product-carousel.json");

        JsonNode root = mapper.readTree(is);
        String rootPath = root.get("rootPath").asText();

        Resource rootResource = mock(Resource.class);
        when(resolver.getResource(rootPath)).thenReturn(rootResource);

        List<Resource> children = new ArrayList<>();

        for (JsonNode productNode : root.get("products")) {

            Resource child = mock(Resource.class);
            Resource data = mock(Resource.class);
            ValueMap vm = mock(ValueMap.class);

            when(child.getChild("jcr:content/data/master")).thenReturn(data);
            when(data.getValueMap()).thenReturn(vm);

            when(vm.get("title", "")).thenReturn(productNode.get("title").asText());
            when(vm.get("category", "")).thenReturn(productNode.get("category").asText());
            when(vm.get("image", String.class)).thenReturn(productNode.get("image").asText());

            when(vm.get("originalPrice", Number.class))
                    .thenReturn(productNode.get("originalPrice").numberValue());

            when(vm.get("currentPrice", Number.class))
                    .thenReturn(productNode.get("currentPrice").numberValue());

            when(vm.get("discount", Number.class))
                    .thenReturn(productNode.get("discount").numberValue());

            children.add(child);
        }

        when(rootResource.getChildren()).thenReturn(children);

        List<Product> products = productService.getProducts(rootPath, resolver);

        assertEquals(2, products.size());

        Product apple = products.get(0);
        assertEquals("Apple", apple.getTitle());
        assertEquals(80, apple.getCurrentPrice());

        Product banana = products.get(1);
        assertEquals(45, banana.getCurrentPrice()); // discount applied
    }

    //2. EMPTY ROOT PATH
    @Test
    void testEmptyRootPath() {
        List<Product> result = productService.getProducts("", resolver);
        assertTrue(result.isEmpty());
    }

    //NULL RESOURCE
    @Test
    void testNullResource() {

        when(resolver.getResource("/invalid")).thenReturn(null);

        List<Product> result = productService.getProducts("/invalid", resolver);

        assertTrue(result.isEmpty());
    }

    //4. CHILD WITHOUT DATA NODE
    @Test
    void testChildWithoutDataNode() {

        Resource rootResource = mock(Resource.class);
        Resource child = mock(Resource.class);

        when(resolver.getResource("/content/products")).thenReturn(rootResource);
        when(rootResource.getChildren()).thenReturn(Collections.singletonList(child));
        when(child.getChild("jcr:content/data/master")).thenReturn(null);

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertTrue(result.isEmpty());
    }

    //IMAGE RESOLVE (external)
    @Test
    void testImageResolveExternal() {

        Resource rootResource = mock(Resource.class);
        Resource child = mock(Resource.class);
        Resource data = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);
        Resource resolved = mock(Resource.class);

        when(resolver.getResource("/content/products")).thenReturn(rootResource);
        when(rootResource.getChildren()).thenReturn(Collections.singletonList(child));

        when(child.getChild("jcr:content/data/master")).thenReturn(data);
        when(data.getValueMap()).thenReturn(vm);

        when(vm.get("title", "")).thenReturn("Test");
        when(vm.get("category", "")).thenReturn("Cat");
        when(vm.get("image", String.class)).thenReturn("/external/img");

        when(vm.get("originalPrice", Number.class)).thenReturn(100);
        when(vm.get("currentPrice", Number.class)).thenReturn(50);
        when(vm.get("discount", Number.class)).thenReturn(0);

        when(resolver.resolve("/external/img")).thenReturn(resolved);
        when(resolved.getPath()).thenReturn("/resolved/img");

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertEquals("/resolved/img", result.get(0).getImage());
    }

    //EXCEPTION BRANCH
    @Test
    void testExceptionHandling() {

        when(resolver.getResource("/content/products"))
                .thenThrow(new RuntimeException("Error"));

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProducts_NullNumberValues() {

        Resource root = mock(Resource.class);
        Resource child = mock(Resource.class);
        Resource data = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource("/content/products")).thenReturn(root);
        when(root.getChildren()).thenReturn(Collections.singletonList(child));

        when(child.getChild("jcr:content/data/master")).thenReturn(data);
        when(data.getValueMap()).thenReturn(vm);

        when(vm.get("title", "")).thenReturn("Test");
        when(vm.get("category", "")).thenReturn("Cat");
        when(vm.get("image", String.class)).thenReturn("/content/dam/test.png");

        when(vm.get("originalPrice", Number.class)).thenReturn(null);
        when(vm.get("currentPrice", Number.class)).thenReturn(null);
        when(vm.get("discount", Number.class)).thenReturn(null);

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertEquals(0.0, result.get(0).getOriginalPrice());
    }

    @Test
    void testGetProducts_NumberException() {

        Resource root = mock(Resource.class);
        Resource child = mock(Resource.class);
        Resource data = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource("/content/products")).thenReturn(root);
        when(root.getChildren()).thenReturn(Collections.singletonList(child));

        when(child.getChild("jcr:content/data/master")).thenReturn(data);
        when(data.getValueMap()).thenReturn(vm);

        when(vm.get("title", "")).thenReturn("Test");
        when(vm.get("category", "")).thenReturn("Cat");
        when(vm.get("image", String.class)).thenReturn("/content/dam/test.png");

        when(vm.get("originalPrice", Number.class)).thenThrow(new RuntimeException());

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertEquals(0.0, result.get(0).getOriginalPrice());
    }

    @Test
    void testResolveImage_NullOrEmpty() {

        Resource root = mock(Resource.class);
        Resource child = mock(Resource.class);
        Resource data = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource("/content/products")).thenReturn(root);
        when(root.getChildren()).thenReturn(Collections.singletonList(child));

        when(child.getChild("jcr:content/data/master")).thenReturn(data);
        when(data.getValueMap()).thenReturn(vm);

        when(vm.get("title", "")).thenReturn("Test");
        when(vm.get("category", "")).thenReturn("Cat");

        when(vm.get("image", String.class)).thenReturn(null);

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertEquals("", result.get(0).getImage());
    }

    @Test
    void testResolveImage_ResolverReturnsNull() {

        Resource root = mock(Resource.class);
        Resource child = mock(Resource.class);
        Resource data = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource("/content/products")).thenReturn(root);
        when(root.getChildren()).thenReturn(Collections.singletonList(child));

        when(child.getChild("jcr:content/data/master")).thenReturn(data);
        when(data.getValueMap()).thenReturn(vm);

        when(vm.get("title", "")).thenReturn("Test");
        when(vm.get("category", "")).thenReturn("Cat");
        when(vm.get("image", String.class)).thenReturn("/external/img");

        when(vm.get("originalPrice", Number.class)).thenReturn(100);
        when(vm.get("currentPrice", Number.class)).thenReturn(50);
        when(vm.get("discount", Number.class)).thenReturn(0);

        when(resolver.resolve("/external/img")).thenReturn(null);

        List<Product> result = productService.getProducts("/content/products", resolver);

        assertEquals("/external/img", result.get(0).getImage());
    }
}