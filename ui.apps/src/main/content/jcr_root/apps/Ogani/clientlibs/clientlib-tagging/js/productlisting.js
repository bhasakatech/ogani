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
    }

};
