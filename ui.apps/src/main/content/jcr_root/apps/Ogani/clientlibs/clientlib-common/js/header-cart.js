function updateHeaderCounts() {

    const wishlistCount = WishlistService.count();
    const cartCount = CartService.getCart().length;

    /* Desktop */
    const wishlistEl = document.getElementById("wishlist-count");
    const cartEl = document.getElementById("cart-count");

    if (wishlistEl) wishlistEl.innerText = wishlistCount;
    if (cartEl) cartEl.innerText = cartCount;

    /* Mobile */
    const wishlistMobile = document.getElementById("wishlist-count-mobile");
    const cartMobile = document.getElementById("cart-count-mobile");

    if (wishlistMobile) wishlistMobile.innerText = wishlistCount;
    if (cartMobile) cartMobile.innerText = cartCount;
}

/* ================= INITIAL LOAD ================= */
document.addEventListener("DOMContentLoaded", function () {
    updateHeaderCounts();
});

/* ================= AUTO UPDATE (IMPORTANT) ================= */
window.addEventListener("storage", function () {
    updateHeaderCounts();
});