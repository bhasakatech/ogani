package com.bhasaka.ogani.core.models;

import static org.junit.jupiter.api.Assertions.*;

import com.day.cq.search.*;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Session;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link BlogPagesModel}.
 * <p>
 * This class verifies model initialization, pagination logic,
 * category loading, search and category filters using AEM Mocks
 * and Mockito for QueryBuilder interactions.
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BlogPagesModelTest {

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private Query query;

    @Mock
    private SearchResult searchResult;

    @Mock
    private Session session;

    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment by mocking QueryBuilder,
     * registering services, and preparing test resources.
     */
    @BeforeEach
    void setUp() {

        context.registerService(QueryBuilder.class, queryBuilder);
        context.registerAdapter(ResourceResolver.class, Session.class, session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getResources()).thenReturn(Collections.emptyIterator());
        when(searchResult.getTotalMatches()).thenReturn(0L);

        context.create().resource("/content/test",
                "blogParentPath", "/content/blogs",
                "itemsPerPage", 4,
                "categoryRootPath", "/content/cq:tags",
                "recentBlogPagesLimit", "3",
                "minSearchLength", 3
        );

        context.currentResource("/content/test");
        context.request().setParameterMap(new HashMap<>());
    }

    /**
     * Verifies that the model initializes successfully.
     */
    @Test
    void testModelInitialization() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }

    /**
     * Validates paginated blog list is initialized and empty
     * when no search results are returned.
     */
    @Test
    void testPaginatedBlogs() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getPaginatedBlogs());
        assertTrue(model.getPaginatedBlogs().isEmpty());
    }

    /**
     * Verifies recent blogs list is initialized.
     */
    @Test
    void testRecentBlogs() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getRecentBlogs());
    }

    /**
     * Validates category loading from tag structure.
     */
    @Test
    void testCategories() {

        context.create().resource("/content/cq:tags/test",
                "jcr:primaryType", "cq:Tag");

        context.create().resource("/content/cq:tags/test/tag1",
                "jcr:title", "Tag1");

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getCategories());
    }

    /**
     * Verifies basic pagination boundary conditions.
     */
    @Test
    void testPagination() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertTrue(model.getPrevPage() >= 1);
        assertTrue(model.getNextPage() >= 0);
    }

    /**
     * Validates previous and next page logic.
     */
    @Test
    void testPrevNextPage() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);

        assertEquals(1, model.getPrevPage());
        assertTrue(model.getNextPage() <= 1);
    }

    /**
     * Verifies model behavior when search parameter is provided.
     */
    @Test
    void testSearchParameter() {
        context.request().setParameterMap(
                Map.of("search", "blog")
        );

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }

    /**
     * Verifies model behavior when category filter is applied.
     */
    @Test
    void testCategoryFilter() {
        context.request().setParameterMap(
                Map.of("category", "test:tag")
        );

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }
}