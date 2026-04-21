document.addEventListener("DOMContentLoaded", function () {

    function getCart() {
        try {
            return JSON.parse(localStorage.getItem("cart")) || [];
        } catch {
            return [];
        }
    }

    function getCoupon() {
        try {
            return JSON.parse(localStorage.getItem("coupon"));
        } catch {
            return null;
        }
    }

    function renderCheckout() {

        const cart = getCart();
        const coupon = getCoupon();

        const container = document.getElementById("checkout-order-list");

        if (!container) return;

        container.innerHTML = "";

        if (cart.length === 0) {
            container.innerHTML = `<p>Your cart is empty</p>`;
            return;
        }

        let subtotal = 0;

        cart.forEach(product => {

            const total = product.price * product.quantity;
            subtotal += total;

            const row = document.createElement("div");
            row.className = "order-row";

            row.innerHTML = `
                <span class="product-name">${product.title}</span>
                <span class="product-total">$ ${total.toFixed(2)}</span>
            `;

            container.appendChild(row);
        });

        /* ================= APPLY COUPON ================= */

        let finalTotal = subtotal;
        let discountAmount = 0;

        if (coupon) {

            if (coupon.type === "percent") {
                discountAmount = subtotal * coupon.value / 100;
            }

            if (coupon.type === "flat") {
                discountAmount = coupon.value;
            }

            finalTotal = subtotal - discountAmount;

            if (finalTotal < 0) finalTotal = 0;
        }

        /* ================= UPDATE UI ================= */

        const subtotalEl = document.getElementById("checkout-subtotal");
        const totalEl = document.getElementById("checkout-total");
        const discountRow = document.getElementById("checkout-discount-row");
        const discountEl = document.getElementById("checkout-discount");

        if (subtotalEl) subtotalEl.innerText = "$ " + subtotal.toFixed(2);
        if (totalEl) totalEl.innerText = "$ " + finalTotal.toFixed(2);

        /* ===== SHOW DISCOUNT ===== */
        if (coupon && discountAmount > 0) {
            if (discountRow) discountRow.style.display = "flex";
            if (discountEl) discountEl.innerText = "-$ " + discountAmount.toFixed(2);
        } else {
            if (discountRow) discountRow.style.display = "none";
        }
    }

    renderCheckout();
});