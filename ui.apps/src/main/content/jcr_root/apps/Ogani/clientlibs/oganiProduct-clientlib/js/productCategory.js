document.addEventListener("DOMContentLoaded", function () {

    const carousel = document.querySelector(".category-carousel");
    if (!carousel) return;

    const track = carousel.querySelector(".carousel-track");
    const nextBtn = carousel.querySelector(".next");
    const prevBtn = carousel.querySelector(".prev");

    let items = Array.from(track.children);
    let index = 0;

    function getVisibleItems() {
        if (window.innerWidth <= 480) return 1;
        if (window.innerWidth <= 768) return 2;
        return 4;
    }

    let visibleItems = getVisibleItems();

    // ✅ Clone WITHOUT deleting original HTL content
    function cloneItems() {
        const clones = items.slice(0, visibleItems).map(item => item.cloneNode(true));
        clones.forEach(clone => track.appendChild(clone));

        items = Array.from(track.children);
    }

    cloneItems();

    function getItemWidth() {
        const item = track.querySelector(".product-carousel-item");

        const style = window.getComputedStyle(track);
        const gap = parseInt(style.gap || 0);

        return item.offsetWidth + gap;
    }

    function updateCarousel(withTransition = true) {
        const itemWidth = getItemWidth();

        track.style.transition = withTransition
            ? "transform 0.5s ease"
            : "none";

        track.style.transform = `translateX(-${index * itemWidth}px)`;
    }

    function nextSlide() {
        index++;
        updateCarousel(true);

        if (index >= items.length - visibleItems) {
            setTimeout(() => {
                index = 0;
                updateCarousel(false);
            }, 500);
        }
    }

    function prevSlide() {
        if (index === 0) {
            index = items.length - visibleItems;
            updateCarousel(false);
        }

        index--;
        updateCarousel(true);
    }

    nextBtn.addEventListener("click", nextSlide);
    prevBtn.addEventListener("click", prevSlide);

    // Auto slide
    let autoSlide = setInterval(nextSlide, 3000);

    carousel.addEventListener("mouseenter", () => clearInterval(autoSlide));
    carousel.addEventListener("mouseleave", () => {
        autoSlide = setInterval(nextSlide, 3000);
    });

    // ⚠️ On resize → DO NOT rebuild DOM, just reset
    window.addEventListener("resize", () => {
        visibleItems = getVisibleItems();
        index = 0;
        updateCarousel(false);
    });

    updateCarousel(false);
});
