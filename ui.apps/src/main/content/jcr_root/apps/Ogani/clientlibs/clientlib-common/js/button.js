document.addEventListener("click", function (e) {

    const card =
        e.target.closest(".pc__product-card") ||
        e.target.closest(".product__item") ||
        e.target.closest(".product-card")||
        e.target.closest(".RC__product-card");

    if (!card) return;

    const product = {
        id: card.dataset.id,
        title: card.dataset.title,
        price: parseFloat(card.dataset.price),
        image: card.dataset.image
    };

    if (e.target.closest(".cart-btn")) {
        CartService.add(product);
        updateHeaderCounts();  
        console.log("Cart:", CartService.getCart());
    }

    if (e.target.closest(".wishlist-btn")) {
        WishlistService.add(product);
        updateHeaderCounts(); 
        console.log("Wishlist:", WishlistService.get());
    }

});