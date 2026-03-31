package com.bhasaka.ogani.core.models.featured;

import com.day.cq.search.*;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.jcr.Session;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class FeaturedProductsModelTest {

    @InjectMocks
    private FeaturedProductsModel model;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private Query query;

    @Mock
    private SearchResult searchResult;

    @Mock
    private Hit hit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===== Helper (only for categoryItems) =====
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===== TESTS =====

    @Test
    void testInit_NullSession() {
        when(resourceResolver.adaptTo(Session.class)).thenReturn(null);

        model.init();

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testLoadProducts_Success() throws Exception {

        Session session = mock(Session.class);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));

        when(hit.getPath()).thenReturn("/test");

        Resource cf = mock(Resource.class);
        Resource master = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resourceResolver.getResource("/test")).thenReturn(cf);
        when(cf.getChild("jcr:content/data/master")).thenReturn(master);
        when(master.getValueMap()).thenReturn(vm);

        when(vm.get("title", String.class)).thenReturn("Apple");
        when(vm.get("price", String.class)).thenReturn("10");
        when(vm.get("image", String.class)).thenReturn("/img.png");
        when(vm.get("category", String[].class))
                .thenReturn(new String[]{"ogani:products/fresh-meat"});

        model.init();

        assertEquals(1, model.getProducts().size());
    }

    @Test
    void testLoadProducts_NullResource() throws Exception {

        Session session = mock(Session.class);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));

        when(hit.getPath()).thenReturn("/invalid");
        when(resourceResolver.getResource("/invalid")).thenReturn(null);

        model.init();

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testLoadProducts_NullMaster() throws Exception {

        Session session = mock(Session.class);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));

        Resource cf = mock(Resource.class);

        when(hit.getPath()).thenReturn("/test");
        when(resourceResolver.getResource("/test")).thenReturn(cf);
        when(cf.getChild("jcr:content/data/master")).thenReturn(null);

        model.init();

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testLoadProducts_MissingFields() throws Exception {

        Session session = mock(Session.class);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));

        Resource cf = mock(Resource.class);
        Resource master = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(hit.getPath()).thenReturn("/test");
        when(resourceResolver.getResource("/test")).thenReturn(cf);
        when(cf.getChild("jcr:content/data/master")).thenReturn(master);
        when(master.getValueMap()).thenReturn(vm);

        when(vm.get("title", String.class)).thenReturn(null);
        when(vm.get("price", String.class)).thenReturn(null);

        model.init();

        assertTrue(model.getProducts().isEmpty());
    }

    @Test
    void testGetCategories() {

        Resource item = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(item.getValueMap()).thenReturn(vm);
        when(vm.get("categoryTag", String.class))
                .thenReturn("ogani:products/fresh-meat");

        setPrivateField(model, "categoryItems", Collections.singletonList(item));

        List<Category> categories = model.getCategories();

        assertEquals(1, categories.size());
        assertEquals("fresh-meat", categories.get(0).getTag());
        assertEquals("Fresh Meat", categories.get(0).getName());
    }

    @Test
    void testGetCategories_Null() {
        List<Category> categories = model.getCategories();
        assertTrue(categories.isEmpty());
    }

    @Test
    void testGetSectionTitle_Default() {
        assertEquals("Featured Products", model.getSectionTitle());
    }

    @Test
    void testEnableHoverActions_Default() {
        assertFalse(model.isEnableHoverActions());
    }
}