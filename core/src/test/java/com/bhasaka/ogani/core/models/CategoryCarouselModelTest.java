package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.beans.ProductCategory;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CategoryCarouselModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CategoryCarouselModel.class);
    }

    @Test
    void test_cfPathNull() {

        context.create().resource("/content/test");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.getCategories().isEmpty());
    }

    @Test
    void test_folderMissing() {

        context.create().resource("/content/test",
                "cfPath", "/content/missing");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.getCategories().isEmpty());
    }

    @Test
    void test_emptyFolder() {

        context.create().resource("/content/categories");

        context.create().resource("/content/test",
                "cfPath", "/content/categories");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.getCategories().isEmpty());
    }

    
    @Test
    void test_missingDataNode() {

        context.create().resource("/content/categories/item1");

        context.create().resource("/content/test",
                "cfPath", "/content/categories");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.getCategories().isEmpty());
    }

    @Test
    void test_missingProperties() {

        context.create().resource("/content/categories/item1/jcr:content/data/master",
                "link", "/content/link");

        context.create().resource("/content/test",
                "cfPath", "/content/categories");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        assertTrue(model.getCategories().isEmpty());
    }

    @Test
    void test_success() {

        context.create().resource("/content/categories/item1/jcr:content/data/master",
                "title", "Apple",
                "image", "/content/dam/apple.png",
                "link", "/content/apple");

        context.create().resource("/content/categories/item2/jcr:content/data/master",
                "title", "Banana",
                "image", "/content/dam/banana.png",
                "link", "/content/banana");

        context.create().resource("/content/test",
                "cfPath", "/content/categories");

        CategoryCarouselModel model =
                context.currentResource("/content/test")
                        .adaptTo(CategoryCarouselModel.class);

        assertNotNull(model);
        List<ProductCategory> categories = model.getCategories();
        assertEquals(2, categories.size());
        assertEquals("Apple", categories.get(0).getTitle());
        assertEquals("Banana", categories.get(1).getTitle());
    }
}
