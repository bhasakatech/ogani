document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".nav-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const href = this.dataset.href;
            if (href) window.location.href = href;
        });
    });

    // Core cart update logic
    function updateCart() {
        let subtotal = 0;

        document.querySelectorAll(".cart-row").forEach(row => {
            const price = parseFloat(row.dataset.price) || 0;
            const qtyInput = row.querySelector(".qty");
            let qty = parseInt(qtyInput.value, 10);

            if (isNaN(qty) || qty < 1) qty = 1;
            qtyInput.value = qty;

            const total = price * qty;
            const rowTotalEl = row.querySelector(".row-total");
            if (rowTotalEl) rowTotalEl.innerText = "$" + total.toFixed(2);

            subtotal += total;
        });

        const subtotalEl = document.getElementById("subtotal");
        const totalEl = document.getElementById("total");
        if (subtotalEl) subtotalEl.innerText = "$" + subtotal.toFixed(2);
        if (totalEl) totalEl.innerText = "$" + subtotal.toFixed(2);
    }

    // Delegate clicks for plus, minus, remove
    document.addEventListener("click", function (e) {
        const target = e.target;

        if (target.classList.contains("plus")) {
            const input = target.closest(".qty-box").querySelector(".qty");
            input.value = parseInt(input.value || "0", 10) + 1;
            updateCart();
        }

        if (target.classList.contains("minus")) {
            const input = target.closest(".qty-box").querySelector(".qty");
            const current = parseInt(input.value || "0", 10);
            if (current > 1) {
                input.value = current - 1;
                updateCart();
            }
        }

        if (target.classList.contains("remove")) {
            const row = target.closest(".cart-row");
            if (row) {
                row.remove();
                updateCart();
            }
        }
    });

    // Update when user types quantity manually
    document.addEventListener("input", function (e) {
        if (e.target && e.target.classList.contains("qty")) {
            // sanitize to digits only
            e.target.value = e.target.value.replace(/[^\d]/g, "");
            // keep at least 1
            if (e.target.value === "" || parseInt(e.target.value, 10) < 1) {
                // do not force immediately; wait for blur to set to 1
            }
            updateCart();
        }
    });

    // Ensure qty is at least 1 on blur
    document.addEventListener("blur", function (e) {
        if (e.target && e.target.classList.contains("qty")) {
            if (!e.target.value || parseInt(e.target.value, 10) < 1) {
                e.target.value = 1;
            }
            updateCart();
        }
    }, true);

    // Optional: coupon apply button (keeps existing behavior; replace with real logic as needed)
    const applyCouponBtn = document.getElementById("apply-coupon");
    if (applyCouponBtn) {
        applyCouponBtn.addEventListener("click", function () {
            const code = document.getElementById("coupon-code").value.trim();
            // placeholder: you can replace this with real coupon validation
            if (code) {
                // Example: show a quick feedback (replace with your UI)
                alert("Coupon applied: " + code);
            } else {
                alert("Please enter a coupon code.");
            }
        });
    }

    // Initial calculation
    updateCart();
});