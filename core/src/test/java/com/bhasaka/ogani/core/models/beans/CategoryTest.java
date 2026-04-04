// package com.bhasaka.ogani.core.models.beans;

// import io.wcm.testing.mock.aem.junit5.AemContextExtension;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;


// import static org.junit.jupiter.api.Assertions.*;

// class CategoryTest {

//     @Test
//     void testConstructorAndGetters() {
//         Category category = new Category(
//                 "Apple",
//                 "/content/dam/apple.png",
//                 "/content/apple"
//         );

//         assertEquals("Apple", category.getTitle());
//         assertEquals("/content/dam/apple.png", category.getImage());
//         assertEquals("/content/apple", category.getLink());
//     }

//     @Test
//     void testNullValues() {
//         Category category = new Category(null, null, null);

//         assertNull(category.getTitle());
//         assertNull(category.getImage());
//         assertNull(category.getLink());
//     }

//     @Test
//     void testEmptyValues() {
//         Category category = new Category("", "", "");

//         assertEquals("", category.getTitle());
//         assertEquals("", category.getImage());
//         assertEquals("", category.getLink());
//     }
// }
