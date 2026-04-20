package com.bhasaka.ogani.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for FilterModel.
 *
 * This class validates the behavior of FilterModel using AEM Mocks and Mockito.
 *
 * Coverage includes:
 * - Property injection using ValueMapValue
 * - Initialization logic in PostConstruct
 * - TagManager interaction and tag hierarchy processing
 * - Edge cases such as null values and empty inputs
 *
 * Tools used:
 * - AemContext for mocking Sling and JCR
 * - Mockito for mocking TagManager and Tag objects
 *
 * Goal:
 * Achieve high branch and method coverage by testing all possible execution paths.
 */
@ExtendWith(AemContextExtension.class)
class FilterModelTest {

    /**
     * AEM context used to simulate Sling environment and resources.
     */
    private final AemContext context = new AemContext();

    /**
     * ResourceResolver used in tests.
     */
    private ResourceResolver resolver;

    /**
     * Setup method to register model class before each test.
     */
    @BeforeEach
    void setup() {
        context.addModelsForClasses(FilterModel.class);
        resolver = context.resourceResolver();
    }

    /**
     * Tests basic property injection for minPrice, maxPrice, colors, and sizes.
     *
     * Verifies that:
     * - Values are correctly injected from resource
     * - Multi-value properties are properly mapped to List
     */
    @Test
    void testBasicProperties() {

        context.create().resource("/content/filter",
                "minPrice", 10,
                "maxPrice", 100,
                "colors", new String[]{"red", "blue"},
                "sizes", new String[]{"S", "M"}
        );

        FilterModel model = context.resourceResolver()
                .getResource("/content/filter")
                .adaptTo(FilterModel.class);

        assertNotNull(model);

        assertEquals(10, model.getMinPrice());
        assertEquals(100, model.getMaxPrice());

        assertEquals(2, model.getColors().size());
        assertTrue(model.getColors().contains("red"));
        assertTrue(model.getColors().contains("blue"));

        assertEquals(2, model.getSizes().size());
        assertTrue(model.getSizes().contains("S"));
        assertTrue(model.getSizes().contains("M"));
    }

    /**
     * Tests behavior when tagRootPath is blank.
     *
     * Verifies that:
     * - Initialization logic skips processing
     * - departmentTags remains empty
     */
    @Test
    void testInit_BlankTagPath() {

        context.create().resource("/content/filter",
                "tagRootPath", ""
        );

        FilterModel model = context.resourceResolver()
                .getResource("/content/filter")
                .adaptTo(FilterModel.class);

        assertTrue(model.getDepartmentTags().isEmpty());
    }

    /**
     * Tests scenario where TagManager is null.
     *
     * Verifies that:
     * - No exception is thrown
     * - departmentTags remains empty
     */
    @Test
    void testInit_TagManagerNull() {

        context.create().resource("/content/filter",
                "tagRootPath", "/content/tags"
        );

        Resource resource = context.resourceResolver().getResource("/content/filter");

        ResourceResolver spyResolver = spy(context.resourceResolver());
        when(spyResolver.adaptTo(TagManager.class)).thenReturn(null);

        FilterModel model = resource.adaptTo(FilterModel.class);

        setField(model, "resourceResolver", spyResolver);

        model.init();

        assertTrue(model.getDepartmentTags().isEmpty());
    }

    /**
     * Tests scenario where root tag cannot be resolved.
     *
     * Verifies that:
     * - TagManager returns null for root tag
     * - departmentTags remains empty
     */
    @Test
    void testInit_RootTagNull() {

        context.create().resource("/content/filter",
                "tagRootPath", "/content/tags"
        );

        Resource resource = context.resourceResolver().getResource("/content/filter");

        TagManager tagManager = mock(TagManager.class);
        when(tagManager.resolve("/content/tags")).thenReturn(null);

        ResourceResolver spyResolver = spy(context.resourceResolver());
        when(spyResolver.adaptTo(TagManager.class)).thenReturn(tagManager);

        FilterModel model = resource.adaptTo(FilterModel.class);
        setField(model, "resourceResolver", spyResolver);

        model.init();

        assertTrue(model.getDepartmentTags().isEmpty());
    }

    /**
     * Tests scenario where root tag has no children.
     *
     * Verifies that:
     * - Iterator is empty
     * - departmentTags remains empty
     */
    @Test
    void testInit_NoChildTags() {

        context.create().resource("/content/filter",
                "tagRootPath", "/content/tags"
        );

        TagManager tagManager = mock(TagManager.class);
        Tag rootTag = mock(Tag.class);

        when(tagManager.resolve("/content/tags")).thenReturn(rootTag);
        when(rootTag.listChildren()).thenReturn(Collections.emptyIterator());

        ResourceResolver spyResolver = spy(context.resourceResolver());
        when(spyResolver.adaptTo(TagManager.class)).thenReturn(tagManager);

        FilterModel model = context.resourceResolver()
                .getResource("/content/filter")
                .adaptTo(FilterModel.class);

        setField(model, "resourceResolver", spyResolver);

        model.init();

        assertTrue(model.getDepartmentTags().isEmpty());
    }

    /**
     * Tests successful scenario where child tags are present.
     *
     * Verifies that:
     * - Child tags are iterated
     * - Titles are extracted correctly
     * - departmentTags contains expected values
     */
    @Test
    void testInit_WithChildTags() {

        context.create().resource("/content/filter",
                "tagRootPath", "/content/tags"
        );

        TagManager tagManager = mock(TagManager.class);
        Tag rootTag = mock(Tag.class);
        Tag child1 = mock(Tag.class);
        Tag child2 = mock(Tag.class);

        when(child1.getTitle()).thenReturn("Men");
        when(child2.getTitle()).thenReturn("Women");

        Iterator<Tag> iterator = Arrays.asList(child1, child2).iterator();

        when(rootTag.listChildren()).thenReturn(iterator);
        when(tagManager.resolve("/content/tags")).thenReturn(rootTag);

        ResourceResolver spyResolver = spy(context.resourceResolver());
        when(spyResolver.adaptTo(TagManager.class)).thenReturn(tagManager);

        FilterModel model = context.resourceResolver()
                .getResource("/content/filter")
                .adaptTo(FilterModel.class);

        setField(model, "resourceResolver", spyResolver);

        model.init();

        List<String> tags = model.getDepartmentTags();

        assertEquals(2, tags.size());
        assertTrue(tags.contains("Men"));
        assertTrue(tags.contains("Women"));
    }

    /**
     * Utility method to set private fields using reflection.
     *
     * Used to inject mocked dependencies like ResourceResolver
     * when AEM Mock does not automatically inject them.
     *
     * @param target object where field needs to be set
     * @param fieldName name of the field
     * @param value value to inject
     */
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}