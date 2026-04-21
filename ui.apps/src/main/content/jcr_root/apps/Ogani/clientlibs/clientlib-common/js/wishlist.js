const WishlistService = (() => {

    const KEY = "wishlist";

    /* ================= SAFE GET ================= */
    function get() {
        try {
            return JSON.parse(localStorage.getItem(KEY)) || [];
        } catch (e) {
            console.error("Wishlist parse error:", e);
            return [];
        }
    }

    /* ================= SAVE ================= */
    function save(list) {
        localStorage.setItem(KEY, JSON.stringify(list));
    }

    /* ================= ADD ================= */
    function add(product) {

        if (!product || !product.id) {
            console.warn("Invalid product:", product);
            return false;
        }

        let list = get();

        const exists = list.find(p => p.id === product.id);

        if (!exists) {
            list.push(product);
            save(list);
            return true;  
        }

        return false;      
    }

    /* ================= REMOVE ================= */
    function remove(id) {
        save(get().filter(p => p.id !== id));
    }

    /* ================= EXISTS ================= */
    function exists(id) {
        return get().some(p => p.id === id);
    }

    /* ================= COUNT ================= */
    function count() {
        return get().length;
    }

    /* ================= CLEAR ================= */
    function clear() {
        localStorage.removeItem(KEY);
    }

    return {
        get,
        add,
        remove,
        exists,
        count,
        clear
    };

})();  