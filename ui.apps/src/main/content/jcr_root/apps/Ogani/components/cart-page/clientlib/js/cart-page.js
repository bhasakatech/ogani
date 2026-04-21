const COUPONS = {
    "SAVE10": { type: "percent", value: 10 },
    "SAVE20": { type: "percent", value: 20 },
    "FLAT50": { type: "flat", value: 50 }
};

let appliedCoupon = null;

/* ================= STORAGE HELPERS ================= */
function getCart() {
    return CartService.getCart();
}

function saveCoupon() {
    localStorage.setItem("coupon", JSON.stringify(appliedCoupon));
}

function loadCoupon() {
    try {
        appliedCoupon = JSON.parse(localStorage.getItem("coupon"));
    } catch {
        appliedCoupon = null;
    }
}

document.addEventListener("DOMContentLoaded", function () {

    /* ================= LOAD COUPON ================= */
    loadCoupon();

    /* ================= NAVIGATION ================= */
    document.querySelectorAll(".nav-btn").forEach(function (btn) {
        btn.addEventListener("click", function () {
            const href = this.getAttribute("data-href");

            if (href && href.trim() !== "") {
                const finalUrl = href.endsWith(".html") ? href : href + ".html";
                window.location.href = finalUrl;
            }
        });
    });

    /* ================= RENDER CART ================= */
    function renderCart() {
        const cart = getCart();
        const tbody = document.getElementById("cart-body");

        if (!tbody) return;

        tbody.innerHTML = "";

        if (cart.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5">Cart is empty</td></tr>`;
            updateCart();
            return;
        }

        cart.forEach(product => {
            const row = document.createElement("tr");

            row.className = "cp__cart-row";
            row.dataset.id = product.id || product.title;
            row.dataset.price = product.price;

            row.innerHTML = `
                <td class="cp__product-info">
                    <img src="${product.image}" alt="${product.title}">
                    <span>${product.title}</span>
                </td>

                <td>$${product.price}</td>

                <td>
                    <div class="cp__qty-box">
                        <button class="cp__minus">-</button>
                        <input type="text" value="${product.quantity}" class="cp__qty"/>
                        <button class="cp__plus">+</button>
                    </div>
                </td>

                <td class="cp__row-total">$${product.price}</td>

                <td>
                    <button class="cp__remove">x</button>
                </td>
            `;

            tbody.appendChild(row);
        });

        updateCart();
    }

    /* ================= UPDATE TOTAL ================= */
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

        let finalTotal = subtotal;
        let discountAmount = 0;

        /* ===== APPLY COUPON ===== */
        if (appliedCoupon) {

            if (appliedCoupon.type === "percent") {
                discountAmount = subtotal * appliedCoupon.value / 100;
            }

            if (appliedCoupon.type === "flat") {
                discountAmount = appliedCoupon.value;
            }

            finalTotal = subtotal - discountAmount;

            if (finalTotal < 0) finalTotal = 0;
        }

        const subtotalEl = document.getElementById("subtotal");
        const totalEl = document.getElementById("total");
        const discountRow = document.getElementById("discount-row");
        const discountEl = document.getElementById("discount-amount");

        if (subtotalEl) subtotalEl.innerText = "$" + subtotal.toFixed(2);
        if (totalEl) totalEl.innerText = "$" + finalTotal.toFixed(2);

        /* ===== HANDLE DISCOUNT DISPLAY ===== */
        if (appliedCoupon && discountAmount > 0) {
            if (discountRow) discountRow.style.display = "flex";
            if (discountEl) discountEl.innerText = "-$" + discountAmount.toFixed(2);
        } else {
            if (discountRow) discountRow.style.display = "none";
        }
    }

    /* ================= UPDATE QUANTITY ================= */
    function updateQuantity(id, qty) {
        let cart = getCart();

        cart = cart.map(p => {
            if (p.id === id) p.quantity = qty;
            return p;
        });

        localStorage.setItem("cart", JSON.stringify(cart));
    }

    /* ================= REMOVE ITEM ================= */
    function removeItem(id) {
        const normalizedId = id.trim().toLowerCase();

        let cart = getCart().filter(p => {
            return (p.id || "").trim().toLowerCase() !== normalizedId;
        });

        localStorage.setItem("cart", JSON.stringify(cart));
    }

    /* ================= CLICK EVENTS ================= */
    document.addEventListener("click", function (e) {
        const target = e.target;

        if (target.classList.contains("cp__plus")) {
            const row = target.closest(".cp__cart-row");
            const input = row.querySelector(".cp__qty");

            let qty = parseInt(input.value || "0", 10) + 1;
            input.value = qty;

            updateQuantity(row.dataset.id, qty);
            updateCart();
        }

        if (target.classList.contains("cp__minus")) {
            const row = target.closest(".cp__cart-row");
            const input = row.querySelector(".cp__qty");

            let qty = parseInt(input.value || "0", 10);

            if (qty > 1) {
                qty--;
                input.value = qty;

                updateQuantity(row.dataset.id, qty);
                updateCart();
            }
        }

        if (target.closest(".cp__remove")) {
            const row = target.closest(".cp__cart-row");

            if (row) {
                removeItem(row.dataset.id);
                renderCart();
            }
        }
    });

    /* ================= INPUT CHANGE ================= */
    document.addEventListener("input", function (e) {
        if (e.target && e.target.classList.contains("cp__qty")) {

            e.target.value = e.target.value.replace(/[^\d]/g, "");

            const row = e.target.closest(".cp__cart-row");
            let qty = parseInt(e.target.value || "1", 10);

            if (qty < 1) qty = 1;

            updateQuantity(row.dataset.id, qty);
            updateCart();
        }
    });

    /* ================= INPUT BLUR ================= */
    document.addEventListener("blur", function (e) {
        if (e.target && e.target.classList.contains("cp__qty")) {

            if (!e.target.value || parseInt(e.target.value, 10) < 1) {
                e.target.value = 1;
            }

            const row = e.target.closest(".cp__cart-row");
            updateQuantity(row.dataset.id, parseInt(e.target.value, 10));

            updateCart();
        }
    }, true);

    /* ================= COUPON APPLY ================= */
    const applyCouponBtn = document.getElementById("apply-coupon");

    if (applyCouponBtn) {
        applyCouponBtn.addEventListener("click", function () {

            const code = document.getElementById("coupon-code").value.trim().toUpperCase();

            if (!code) {
                alert("Please enter a coupon code.");
                return;
            }

            const coupon = COUPONS[code];

            if (!coupon) {
                alert("Invalid coupon code");
                appliedCoupon = null;
                saveCoupon();
                updateCart();
                return;
            }

            appliedCoupon = coupon;
            saveCoupon();

            alert(`Coupon applied: ${code}`);
            updateCart();
        });
    }

    /* ================= INITIAL LOAD ================= */
    renderCart();
});