document.addEventListener("DOMContentLoaded", function () {

    /* ================= NAVIGATION ================= */
    document.querySelectorAll(".cp__nav-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const href = this.dataset.href;
            if (href) window.location.href = href;
        });
    });

    /* ================= CART UPDATE ================= */
    function updateCart() {
        let subtotal = 0;

        document.querySelectorAll(".cp__cart-row").forEach(row => {
            const price = parseFloat(row.dataset.price) || 0;
            const qtyInput = row.querySelector(".cp__qty");

            let qty = parseInt(qtyInput.value, 10);
            if (isNaN(qty) || qty < 1) qty = 1;

            qtyInput.value = qty;

            const total = price * qty;

            const rowTotalEl = row.querySelector(".cp__row-total");
            if (rowTotalEl) {
                rowTotalEl.innerText = "$" + total.toFixed(2);
            }

            subtotal += total;
        });

        const subtotalEl = document.getElementById("subtotal");
        const totalEl = document.getElementById("total");

        if (subtotalEl) subtotalEl.innerText = "$" + subtotal.toFixed(2);
        if (totalEl) totalEl.innerText = "$" + subtotal.toFixed(2);
    }

    /* ================= CLICK EVENTS ================= */
    document.addEventListener("click", function (e) {
        const target = e.target;

        /* PLUS */
        if (target.classList.contains("cp__plus")) {
            const input = target.closest(".cp__qty-box").querySelector(".cp__qty");
            input.value = parseInt(input.value || "0", 10) + 1;
            updateCart();
        }

        /* MINUS */
        if (target.classList.contains("cp__minus")) {
            const input = target.closest(".cp__qty-box").querySelector(".cp__qty");
            const current = parseInt(input.value || "0", 10);

            if (current > 1) {
                input.value = current - 1;
                updateCart();
            }
        }

        /* REMOVE */
        if (target.classList.contains("cp__remove")) {
            const row = target.closest(".cp__cart-row");
            if (row) {
                row.remove();
                updateCart();
            }
        }
    });

    /* ================= INPUT CHANGE ================= */
    document.addEventListener("input", function (e) {
        if (e.target && e.target.classList.contains("cp__qty")) {

            // allow only numbers
            e.target.value = e.target.value.replace(/[^\d]/g, "");

            updateCart();
        }
    });

    /* ================= INPUT BLUR FIX ================= */
    document.addEventListener("blur", function (e) {
        if (e.target && e.target.classList.contains("cp__qty")) {

            if (!e.target.value || parseInt(e.target.value, 10) < 1) {
                e.target.value = 1;
            }

            updateCart();
        }
    }, true);

    /* ================= COUPON ================= */
    const applyCouponBtn = document.getElementById("apply-coupon");

    if (applyCouponBtn) {
        applyCouponBtn.addEventListener("click", function () {
            const code = document.getElementById("coupon-code").value.trim();

            if (code) {
                alert("Coupon applied: " + code);
            } else {
                alert("Please enter a coupon code.");
            }
        });
    }

    /* ================= INITIAL LOAD ================= */
    updateCart();
});