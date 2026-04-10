document.addEventListener("DOMContentLoaded", function () {

    const tabs = document.querySelectorAll(".tab-btn");
    const products = document.querySelectorAll(".product-card");

    tabs.forEach(tab => {

        tab.addEventListener("click", function () {

            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            const selected = this.getAttribute("data-category");

            products.forEach(product => {

                const categories = product.getAttribute("data-category");

                if (selected === "all" || (categories && categories.includes(selected))) {
                    product.style.display = "block";
                } else {
                    product.style.display = "none";
                }

            });

        });

    });

});