document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".filter").forEach(container => {

        const minRange = container.querySelector("#minRange");
        const maxRange = container.querySelector("#maxRange");

        const minPriceText = container.querySelector("#minPrice");
        const maxPriceText = container.querySelector("#maxPrice");

        const track = container.querySelector(".slider-track");

        const colorCheckboxes = container.querySelectorAll(".color-filter");

        // 🔥 NEW: apply dynamic colors (scoped)
        const colorDots = container.querySelectorAll(".color-dot");
        colorDots.forEach(el => {
            const color = el.dataset.color;
            if (color) {
                el.style.backgroundColor = color;
            }
        });

        if (!minRange || !maxRange || !track) return;

        let selectedColors = [];

        function updateSlider() {

            let minVal = parseInt(minRange.value);
            let maxVal = parseInt(maxRange.value);

            if (minVal > maxVal) {
                [minRange.value, maxRange.value] = [maxVal, minVal];
            }

            minPriceText.innerText = minRange.value;
            maxPriceText.innerText = maxRange.value;

            let percent1 = ((minRange.value - minRange.min) / (minRange.max - minRange.min)) * 100;
            let percent2 = ((maxRange.value - maxRange.min) / (maxRange.max - maxRange.min)) * 100;

            track.style.background =
                `linear-gradient(to right, #ddd ${percent1}%, #dd2222 ${percent1}%, #dd2222 ${percent2}%, #ddd ${percent2}%)`;

            applyFilters();
        }

        minRange.addEventListener("input", updateSlider);
        maxRange.addEventListener("input", updateSlider);

        colorCheckboxes.forEach(cb => {
            cb.addEventListener("change", () => {

                selectedColors = Array.from(colorCheckboxes)
                    .filter(c => c.checked)
                    .map(c => c.value);

                applyFilters();
            });
        });

        function applyFilters() {

            const min = parseInt(minRange.value);
            const max = parseInt(maxRange.value);

            const products = document.querySelectorAll(".product-item");

            products.forEach(product => {

                const price = parseInt(product.dataset.price) || 0;
                const color = product.dataset.color;

                let matchPrice = price >= min && price <= max;
                let matchColor = selectedColors.length === 0 || selectedColors.includes(color);

                product.style.display =
                    (matchPrice && matchColor) ? "block" : "none";
            });
        }

        updateSlider();
    });
});