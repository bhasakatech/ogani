

document.addEventListener("DOMContentLoaded", function () {
    const qtyInput = document.getElementById("qtyInput");
    const priceElement = document.getElementById("productPrice");

    // Strip currency symbol and parse base price
    const basePrice = parseFloat(priceElement.textContent.replace(/[^\d.]/g, ""));

    function updatePrice(qty) {
        priceElement.textContent = "₹ " + (basePrice * qty).toFixed(2);
    }

    // plus button
    document.querySelector(".qty-btn.plus").addEventListener("click", function () {
        let qty = parseInt(qtyInput.value) || 1;
        qty++;
        qtyInput.value = qty;
        updatePrice(qty);
    });

    // minus button
    document.querySelector(".qty-btn.minus").addEventListener("click", function () {
        let qty = parseInt(qtyInput.value) || 1;
        if (qty > 1) {
            qty--;
            qtyInput.value = qty;
            updatePrice(qty);
        }
    });

    // manual input change
    qtyInput.addEventListener("input", function () {
        let qty = parseInt(qtyInput.value) || 1;
        if (qty < 1) qty = 1;
        qtyInput.value = qty;
        updatePrice(qty);
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const track = document.querySelector(".carousel-track");
    const thumbs = document.querySelectorAll(".carousel-track .thumb");
    const mainImg = document.querySelector(".product__main-image img");
    const titleEl = document.querySelector(".product__title");
    const priceEl = document.querySelector(".product__price");
    const descEl = document.querySelector(".desc-text");

    let currentIndex = 0;
    const AUTO_DELAY = 3000;

    function updateProduct(thumb) {
        if (mainImg) mainImg.src = thumb.getAttribute("data-image");
        if (titleEl) titleEl.textContent = thumb.getAttribute("data-title");
        if (priceEl) priceEl.textContent = "₹ " + thumb.getAttribute("data-price");
        if (descEl) descEl.innerHTML = thumb.getAttribute("data-description");

        thumbs.forEach(t => t.classList.remove("active"));
        thumb.classList.add("active");
    }

    function moveNext() {
        const slideWidth = thumbs[0].offsetWidth;
        const gap = 10; // match CSS gap
        track.style.transition = "transform 0.6s ease";
        track.style.transform = "translateX(-" + (slideWidth + gap) + "px)";

        // After transition, move first slide to the end
        setTimeout(() => {
            track.style.transition = "none";
            track.appendChild(track.firstElementChild);
            track.style.transform = "translateX(0)";
        }, 600);
    }

    // Manual click
    thumbs.forEach((thumb, index) => {
        thumb.addEventListener("click", function () {
            updateProduct(this);
            currentIndex = index;
        });
    });

    // Auto loop
    setInterval(() => {
        moveNext();
    }, AUTO_DELAY);

    // Initial
    updateProduct(thumbs[currentIndex]);
});


document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".tab-nav li").forEach(tab => {
        tab.addEventListener("click", function () {
            const parent = this.closest(".product__tabs");

            parent.querySelectorAll(".tab-nav li").forEach(li => li.classList.remove("active"));
            parent.querySelectorAll(".tab-pane").forEach(pane => pane.classList.remove("active"));

            this.classList.add("active");
            const id = this.getAttribute("data-tab");
            parent.querySelector("#" + id).classList.add("active");
        });
    });
});
