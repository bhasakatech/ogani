package com.bhasaka.ogani.core.models;

import com.bhasaka.ogani.core.models.featuredproducts.ProductCFModel;
import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.jcr.Session;
import javax.jcr.RepositoryException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class FeaturedProductsModelTest {

    private final AemContext context = new AemContext();

    private QueryBuilder queryBuilder;
    private Query query;
    private SearchResult searchResult;
    private Hit hit;

    @BeforeEach
    void setUp() throws Exception {

        // Register model
        context.addModelsForClasses(FeaturedProductsModel.class, ProductCFModel.class);

        // Mock services
        queryBuilder = mock(QueryBuilder.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);
        hit = mock(Hit.class);

        context.registerService(QueryBuilder.class, queryBuilder);

        // Create CF structure
        context.create().resource("/content/dam/Ogani/content-fragments/featured/test",
                "jcr:content/contentFragment", true);

        context.create().resource("/content/dam/Ogani/content-fragments/featured/test/jcr:content/data/master",
                "title", "Apple",
                "price", "100",
                "image", "/img.png",
                "category", new String[]{"/content/cq:tags/fruits"}
        );

        // Mock Query flow
        when(queryBuilder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));
        when(hit.getPath()).thenReturn("/content/dam/Ogani/content-fragments/featured/test");

        // Mock session
        ResourceResolver resolver = context.resourceResolver();
        Session session = mock(Session.class);
        context.registerAdapter(ResourceResolver.class, Session.class, session);
    }

    @Test
    void testProductsLoadedSuccessfully() {

        Resource resource = context.create().resource("/content/test");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertNotNull(model);
        assertEquals(1, model.getProducts().size());
    }

    @Test
    void testNullQueryBuilder() {

        AemContext newContext = new AemContext();
        newContext.addModelsForClasses(FeaturedProductsModel.class);

        Resource resource = newContext.create().resource("/content/test2");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertNotNull(model);
        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testNullMasterNode() throws Exception {

        when(hit.getPath()).thenReturn("/invalid/path");

        Resource resource = context.create().resource("/content/test4");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertNotNull(model);
        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testRepositoryExceptionHandled() throws Exception {

        when(hit.getPath()).thenThrow(new RepositoryException("error"));

        Resource resource = context.create().resource("/content/test5");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertNotNull(model);
        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testGetCategories() {

        Resource resource = context.create().resource("/content/test6");

        context.create().resource("/content/test6/categoryTags/item1",
                "categoryTag", "/content/cq:tags/fruits");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertNotNull(model.getCategories());
    }

    @Test
    void testGetSectionTitleDefault() {

        Resource resource = context.create().resource("/content/test7");

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertEquals("Featured Products", model.getSectionTitle());
    }

    @Test
    void testEnableHoverActions() {

        Resource resource = context.create().resource("/content/test8",
                "enableHoverActions", true);

        FeaturedProductsModel model = resource.adaptTo(FeaturedProductsModel.class);

        assertTrue(model.isEnableHoverActions());
    }
}