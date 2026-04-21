const CartService = (() => {

    const KEY = "cart";

    function getCart() {
        return JSON.parse(localStorage.getItem(KEY)) || [];
    }

    function saveCart(cart) {
        localStorage.setItem(KEY, JSON.stringify(cart));
    }

    function add(product) {
        let cart = getCart();

        const existing = cart.find(p => p.id === product.id);

        if (existing) {
            existing.quantity += 1;
        } else {
            product.quantity = 1;
            cart.push(product);
        }

        saveCart(cart);
    }

    function remove(id) {
        let cart = getCart().filter(p => p.id !== id);
        saveCart(cart);
    }

    function updateQty(id, qty) {
        let cart = getCart().map(p => {
            if (p.id === id) p.quantity = qty;
            return p;
        });

        saveCart(cart);
    }

    return { getCart, add, remove, updateQty };

})();