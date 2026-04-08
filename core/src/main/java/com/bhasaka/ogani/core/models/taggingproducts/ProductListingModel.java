package com.bhasaka.ogani.core.models.taggingproducts;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(
        adaptables = {
                Resource.class,
                SlingHttpServletRequest.class
        },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductListingModel {

    @SlingObject
    private ResourceResolver resolver;

    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String cfPath;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private Integer pageSize;

    private List<TaggingProduct> products = new ArrayList<>();
    private int totalProducts;
    private int totalPages;
    private int currentPage;

    @PostConstruct
    protected void init() {

        if (cfPath == null || cfPath.isEmpty()) return;

        Resource folder = resolver.getResource(cfPath);
        if (folder == null) return;

        List<TaggingProduct> allProducts = new ArrayList<>();

        for (Resource cf : folder.getChildren()) {

            Resource data = cf.getChild("jcr:content/data/master");
            if (data == null) continue;

            ValueMap vm = data.getValueMap();

            String title = vm.get("title", "");
            String price = vm.get("price", "");
            String image = vm.get("image", "");

            String[] cfTags = vm.get("cq:tags", new String[]{});
            List<String> productTags = Arrays.asList(cfTags);

            // TAG FILTER
            if (tags != null && tags.length > 0) {
                boolean match = productTags.stream().anyMatch(productTag ->
                        Arrays.stream(tags).anyMatch(productTag::contains)
                );
                if (!match) continue;
            }

            allProducts.add(new TaggingProduct(title, price, image, productTags));
        }

        totalProducts = allProducts.size();

        if (pageSize == null || pageSize <= 0) {
            pageSize = 6;
        }

        String sortParam = request.getParameter("sort");

        if (sortParam != null) {

            if ("low".equals(sortParam)) {
                allProducts.sort(Comparator.comparingDouble(p -> {
                    try {
                        return Double.parseDouble(p.getPrice().replaceAll("[^0-9.]", ""));
                    } catch (Exception e) {
                        return 0;
                    }
                }));
            }

            if ("high".equals(sortParam)) {
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
    }

    // PAGINATION
    public int getPrevPage() {
        return currentPage > 1 ? currentPage - 1 : 1;
    }

    public int getNextPage() {
        return currentPage < totalPages ? currentPage + 1 : totalPages;
    }

    public List<Integer> getPageNumbers() {
        List<Integer> pages = new ArrayList<>();

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }
        return pages;
    }

    // GETTERS
    public List<TaggingProduct> getProducts() {
        return products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}