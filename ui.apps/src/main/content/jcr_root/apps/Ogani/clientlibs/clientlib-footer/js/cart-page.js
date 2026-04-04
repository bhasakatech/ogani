document.addEventListener("DOMContentLoaded", function () {

    function updateCart() {
        let subtotal = 0;

        document.querySelectorAll(".cart-row").forEach(row => {
            let price = parseFloat(row.dataset.price);
            let qty = parseInt(row.querySelector(".qty").value) || 1;

            let total = price * qty;

            row.querySelector(".row-total").innerText = "$" + total.toFixed(2);
            subtotal += total;
        });

        document.getElementById("subtotal").innerText = "$" + subtotal.toFixed(2);
        document.getElementById("total").innerText = "$" + subtotal.toFixed(2);
    }

    // PLUS
    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("plus")) {
            let input = e.target.parentElement.querySelector(".qty");
            input.value = parseInt(input.value) + 1;
            updateCart();
        }
    });

    // MINUS
    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("minus")) {
            let input = e.target.parentElement.querySelector(".qty");
            if (input.value > 1) {
                input.value = parseInt(input.value) - 1;
                updateCart();
            }
        }
    });

    // REMOVE
    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("remove")) {
            e.target.closest(".cart-row").remove();
            updateCart();
        }
    });

    updateCart();
});