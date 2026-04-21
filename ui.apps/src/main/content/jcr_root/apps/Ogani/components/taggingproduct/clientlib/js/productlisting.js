window.ProductListing = {

    applySort: function (sortValue) {
        const url = new URL(window.location.href);

        if (sortValue) {
            url.searchParams.set("sort", sortValue);
        } else {
            url.searchParams.delete("sort");
        }

        url.searchParams.set("page", 1);
        window.location.href = url.toString();
    },

    setView: function (viewType, element) {

        const grid = document.querySelector(".product__grid");
        const buttons = document.querySelectorAll(".view-btn");

        // Remove active from all buttons
        buttons.forEach(btn => btn.classList.remove("active"));

        // Add active to clicked button
        if (element) {
            element.classList.add("active");
        }

        // Apply view
        if (viewType === "list") {
            grid.classList.add("list-view");
        } else {
            grid.classList.remove("list-view");
        }

        // Save in localStorage
        localStorage.setItem("productView", viewType);
    }
};


// Restore view on page load
document.addEventListener("DOMContentLoaded", function () {

    const savedView = localStorage.getItem("productView");

    if (savedView) {

        const button = document.querySelector(
            `.view-btn[data-view="${savedView}"]`
        );

        ProductListing.setView(savedView, button);
    }

});