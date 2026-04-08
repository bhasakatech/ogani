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

    @Test
    void testModelInitialization() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }

    @Test
    void testPaginatedBlogs() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getPaginatedBlogs());
        assertTrue(model.getPaginatedBlogs().isEmpty());
    }

    @Test
    void testRecentBlogs() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getRecentBlogs());
    }

    @Test
    void testCategories() {

        context.create().resource("/content/cq:tags/test",
                "jcr:primaryType", "cq:Tag");

        context.create().resource("/content/cq:tags/test/tag1",
                "jcr:title", "Tag1");

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model.getCategories());
    }

    @Test
    void testPagination() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertTrue(model.getPrevPage() >= 1);
        assertTrue(model.getNextPage() >= 0);
    }

    @Test
    void testPrevNextPage() {
        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);

        assertEquals(1, model.getPrevPage());
        assertTrue(model.getNextPage() <= 1);
    }

    @Test
    void testSearchParameter() {
        context.request().setParameterMap(
                Map.of("search", "blog")
        );

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }

    @Test
    void testCategoryFilter() {
        context.request().setParameterMap(
                Map.of("category", "test:tag")
        );

        BlogPagesModel model = context.request().adaptTo(BlogPagesModel.class);
        assertNotNull(model);
    }
}