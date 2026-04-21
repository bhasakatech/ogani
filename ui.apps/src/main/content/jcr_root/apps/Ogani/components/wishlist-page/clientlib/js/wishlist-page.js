function getWishlist() {
    return WishlistService.get();
}

document.addEventListener("DOMContentLoaded", function () {

    const container = document.getElementById("wishlist-container");
    const emptyMsg = document.getElementById("wishlist-empty");

    function renderWishlist() {
        const list = getWishlist();

        if (!container) return;

        container.innerHTML = "";

        if (!list || list.length === 0) {
            emptyMsg.style.display = "block";
            return;
        }

        emptyMsg.style.display = "none";

        list.forEach(product => {

            const card = document.createElement("div");
            card.className = "wl__card";
            card.dataset.id = product.id;

            card.innerHTML = `
                <img src="${product.image}" alt="${product.title}">

                <h4>${product.title}</h4>
                <p>$${product.price}</p>

                <div class="wl__actions">
                    <button class="wl__remove">Remove</button>
                    <button class="wl__add-cart">Add to Cart</button>
                </div>
            `;

            container.appendChild(card);
        });
    }

    /* ================= REMOVE ================= */
    function removeItem(id) {
        let list = getWishlist().filter(p => p.id !== id);
        localStorage.setItem("wishlist", JSON.stringify(list));
    }

    /* ================= MOVE TO CART ================= */
    function moveToCart(product) {
        CartService.add(product);
        removeItem(product.id);
    }

    /* ================= CLICK EVENTS ================= */
    document.addEventListener("click", function (e) {

        const card = e.target.closest(".wl__card");
        if (!card) return;

        const id = card.dataset.id;
        const list = getWishlist();
        const product = list.find(p => p.id === id);

        if (e.target.classList.contains("wl__remove")) {
            removeItem(id);
            card.remove();
            renderWishlist();
        }

        if (e.target.classList.contains("wl__add-cart")) {
            moveToCart(product);
            renderWishlist();
        }
    });

    /* ================= INITIAL LOAD ================= */
    renderWishlist();
});