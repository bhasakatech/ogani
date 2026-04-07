package com.bhasaka.ogani.core.models.taggingproducts;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductListingModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setup() {
        context.load().json(
                "/ProductListingModelTest.json",
                "/content/products"
        );
    }

    @Test
    void testBasicLoad() {
        context.currentResource("/content/products");
        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertNotNull(model);
        assertTrue(model.getTotalProducts() >= 0);
        assertNotNull(model.getProducts());
    }

    @Test
    void testNullCfPath() {

        context.create().resource("/content/empty",
                "cfPath", "",
                "pageSize", 2
        );

        context.currentResource("/content/empty");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertNotNull(model);
        assertEquals(0, model.getTotalProducts());
    }

    @Test
    void testDefaultPageSize() {
        context.currentResource("/content/products");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertTrue(model.getPageSize() > 0);
    }

    @Test
    void testSortLow() {
        context.currentResource("/content/products");
        context.request().addRequestParameter("sort", "low");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertNotNull(model.getProducts());
    }

    @Test
    void testSortHigh() {
        context.currentResource("/content/products");
        context.request().addRequestParameter("sort", "high");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertNotNull(model.getProducts());
    }

    @Test
    void testInvalidPageParam() {
        context.currentResource("/content/products");
        context.request().addRequestParameter("page", "abc");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertEquals(1, model.getCurrentPage());
    }

    @Test
    void testValidPageParam() {
        context.currentResource("/content/products");
        context.request().addRequestParameter("page", "1");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertEquals(1, model.getCurrentPage());
    }

    @Test
    void testPaginationBoundary() {
        context.currentResource("/content/products");
        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertTrue(model.getTotalPages() >= 1);
        assertNotNull(model.getPageNumbers());
    }

    @Test
    void testPrevNext() {
        context.currentResource("/content/products");

        ProductListingModel model = context.request().adaptTo(ProductListingModel.class);

        assertTrue(model.getPrevPage() >= 1);
        assertTrue(model.getNextPage() >= 1);
    }
}
