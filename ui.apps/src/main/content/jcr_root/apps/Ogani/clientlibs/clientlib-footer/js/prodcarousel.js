document.addEventListener("DOMContentLoaded", function () {

    const track = document.querySelector(".carousel-track");
    let cards = document.querySelectorAll(".product-card");

    const dotsContainer = document.getElementById("carouselDots");

    const visibleCards = 3;
    const cardWidth = cards[0].offsetWidth + 20;

    /* ================= CLONE FOR LOOP ================= */

    for (let i = 0; i < visibleCards; i++) {
        track.appendChild(cards[i].cloneNode(true)); // end clone
        track.insertBefore(cards[cards.length - 1 - i].cloneNode(true), track.firstChild); // start clone
    }

    cards = document.querySelectorAll(".product-card");

    /* ================= INITIAL POSITION ================= */

    track.scrollLeft = cardWidth * visibleCards;

    /* ================= DOTS ================= */

    const totalSlides = Math.ceil((cards.length - (visibleCards * 2)) / visibleCards);

    for (let i = 0; i < totalSlides; i++) {
        const dot = document.createElement("span");
        if (i === 0) dot.classList.add("active");

        dot.addEventListener("click", () => {
            track.scrollTo({
                left: (i + 1) * cardWidth * visibleCards,
                behavior: "smooth"
            });
        });

        dotsContainer.appendChild(dot);
    }

    function updateDots() {
        let index = Math.round(track.scrollLeft / (cardWidth * visibleCards)) - 1;

        if (index < 0) index = totalSlides - 1;
        if (index >= totalSlides) index = 0;

        document.querySelectorAll(".carousel-dots span")
            .forEach((dot, i) => {
                dot.classList.toggle("active", i === index);
            });
    }

    /* ================= INFINITE SCROLL FIX ================= */

    track.addEventListener("scroll", () => {

        const maxScroll = cardWidth * (cards.length - visibleCards);

        if (track.scrollLeft <= 0) {
            track.scrollLeft = cardWidth * (cards.length - (visibleCards * 2));
        }

        if (track.scrollLeft >= maxScroll) {
            track.scrollLeft = cardWidth * visibleCards;
        }

        updateDots();
    });

    /* ================= DRAG ================= */

    let isDragging = false;
    let startX;
    let scrollLeft;

    track.addEventListener("mousedown", (e) => {
        isDragging = true;
        startX = e.pageX - track.offsetLeft;
        scrollLeft = track.scrollLeft;
        track.style.cursor = "grabbing";
    });

    track.addEventListener("mouseup", () => {
        isDragging = false;
        track.style.cursor = "grab";
    });

    track.addEventListener("mouseleave", () => {
        isDragging = false;
        track.style.cursor = "grab";
    });

    track.addEventListener("mousemove", (e) => {
        if (!isDragging) return;
        e.preventDefault();
        const x = e.pageX - track.offsetLeft;
        const walk = (x - startX) * 1.5;
        track.scrollLeft = scrollLeft - walk;
    });

});