package com.bhasaka.ogani.core.models.taggingproducts;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for Product Listing with tagging, sorting, and pagination.
 *
 * <p>This model:
 * <ul>
 *     <li>Fetches product data from Content Fragments</li>
 *     <li>Filters products based on tags</li>
 *     <li>Supports sorting (low/high price)</li>
 *     <li>Handles pagination</li>
 * </ul>
 * </p>
 */
@Model(
        adaptables = {
                Resource.class,
                SlingHttpServletRequest.class
        },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductListingModel {

    /**
     * Logger for debugging and monitoring model execution.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProductListingModel.class);

    @SlingObject
    private ResourceResolver resolver;

    @SlingObject
    private SlingHttpServletRequest request;

    /**
     * Content Fragment folder path.
     */
    @ValueMapValue
    private String cfPath;

    /**
     * Tags used for filtering products.
     */
    @ValueMapValue
    private String[] tags;

    /**
     * Number of products per page.
     */
    @ValueMapValue
    private Integer pageSize;

    /**
     * Default sorting.
     */
    @ValueMapValue
    private String defaultSorting;

    /**
     * Low to high sorting.
     */
    @ValueMapValue
    private String lowSorting;

    /**
     * High to low sorting.
     */
    @ValueMapValue
    private String highSorting;

    /**
     * Final list of products to display.
     */
    private List<TaggingProduct> products = new ArrayList<>();

    /**
     * Total number of products after filtering.
     */
    private int totalProducts;

    /**
     * Total number of pages.
     */
    private int totalPages;

    /**
     * Current page number.
     */
    private int currentPage;


    /**
     * Selected sorting.
     */
    private String selectedSort;


    /**
     * Initializes the model after injection.
     *
     * <p>Performs:
     * <ul>
     *     <li>Content Fragment reading</li>
     *     <li>Tag-based filtering</li>
     *     <li>Sorting</li>
     *     <li>Pagination calculation</li>
     * </ul>
     * </p>
     */
    @PostConstruct
    protected void init() {

        if (cfPath == null || cfPath.isEmpty()) {
            LOG.warn("ProductListingModel: cfPath is null or empty");
            return;
        }

        Resource folder = resolver.getResource(cfPath);
        if (folder == null) {
            LOG.error("No resource found at path: {}", cfPath);
            return;
        }

        List<TaggingProduct> allProducts = new ArrayList<>();

        for (Resource cf : folder.getChildren()) {

            Resource data = cf.getChild("jcr:content/data/master");
            if (data == null) {
                LOG.warn("Missing data node for fragment: {}", cf.getPath());
                continue;
            }

            ValueMap vm = data.getValueMap();

            String title = vm.get("title", "");
            String price = vm.get("price", "");
            String image = vm.get("image", "");

            String[] cfTags = vm.get("cq:tags", new String[]{});
            List<String> productTags = Arrays.asList(cfTags);


            allProducts.add(new TaggingProduct(title, price, image, productTags));

        }

        totalProducts = allProducts.size();
        LOG.info("Total products after filtering: {}", totalProducts);

        if (pageSize == null || pageSize <= 0) {
            pageSize = 6;

        }

        selectedSort = request.getParameter("sort");
        LOG.debug("Sorting parameter: {}", selectedSort);


        if (selectedSort != null) {

            if ("low".equals(selectedSort)) {
                allProducts.sort(Comparator.comparingDouble(p -> {
                    try {
                        return Double.parseDouble(p.getPrice().replaceAll("[^0-9.]", ""));
                    } catch (Exception e) {
                        return 0;
                    }
                }));
            }

            if ("high".equals(selectedSort)) {
                allProducts.sort((p1, p2) -> {
                    try {
                        double price1 = Double.parseDouble(p1.getPrice().replaceAll("[^0-9.]", ""));
                        double price2 = Double.parseDouble(p2.getPrice().replaceAll("[^0-9.]", ""));
                        return Double.compare(price2, price1);
                    } catch (Exception e) {
                        return 0;
                    }
                });
            }
        }

        currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (Exception ignored) {
                LOG.warn("Invalid page parameter: {}", pageParam);
                currentPage = 1;
            }
        }

        totalPages = (int) Math.ceil((double) totalProducts / pageSize);


        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalProducts);

        if (start < end) {
            products = allProducts.subList(start, end);
        }

        LOG.info("Pagination applied. Current page: {}, Products on page: {}", currentPage, products.size());
    }

    /**
     * Returns previous page number.
     *
     * @return previous page
     */
    public int getPrevPage() {
        return currentPage > 1 ? currentPage - 1 : 1;
    }

    /**
     * Returns next page number.
     *
     * @return next page
     */
    public int getNextPage() {
        return currentPage < totalPages ? currentPage + 1 : totalPages;
    }

    /**
     * Returns list of page numbers for pagination UI.
     *
     * @return list of page numbers
     */
    public List<Integer> getPageNumbers() {
        List<Integer> pages = new ArrayList<>();

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }
        return pages;
    }

    /**
     * Returns products for current page.
     *
     * @return list of products
     */
    public List<TaggingProduct> getProducts() {
        return products;
    }

    /**
     * @return total number of products
     */
    public int getTotalProducts() {
        return totalProducts;
    }

    /**
     * @return total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @return current page number
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return default sorting
     */
    public String getDefaultSorting() {
        return defaultSorting;
    }

    /**
     * @return low to high sorting
     */
    public String getLowSorting() {
        return lowSorting;
    }

    /**
     * @return high to low sorting
     */
    public String getHighSorting() {
        return highSorting;
    }

    public String getSelectedSort() {
        return selectedSort;
    }

}
