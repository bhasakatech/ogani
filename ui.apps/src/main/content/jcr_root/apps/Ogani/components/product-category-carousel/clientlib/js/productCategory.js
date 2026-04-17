document.addEventListener("DOMContentLoaded", function () {

    const carousel = document.querySelector(".category-ogani-carousel");
    if (!carousel) return;

    const track = carousel.querySelector(".category-ogani-carousel-track");
    const nextBtn = carousel.querySelector(".next");
    const prevBtn = carousel.querySelector(".prev");

    let originalItems = Array.from(track.children);
    let items = [];
    let index = 0;
    let visibleItems = getVisibleItems();

    function getVisibleItems() {
        if (window.innerWidth <= 576) return 1;
        if (window.innerWidth <= 992) return 3;
        return 4;
    }

    
    function setupCarousel() {
        track.innerHTML = ""; // clear
        visibleItems = getVisibleItems();

        // add original items
        originalItems.forEach(item => {
            track.appendChild(item.cloneNode(true));
        });

        // clone for infinite loop
        originalItems.slice(0, visibleItems).forEach(item => {
            track.appendChild(item.cloneNode(true));
        });

        items = Array.from(track.children);
        index = 0;
        updateCarousel(false);
    }

    function getItemWidth() {
        const item = track.querySelector(".category-ogani-carousel-item");
        const style = window.getComputedStyle(track);
        const gap = parseInt(style.gap || 0);

        return item.offsetWidth + gap;
    }

    function updateCarousel(withTransition = true) {
        const itemWidth = getItemWidth();

        track.style.transition = withTransition ? "transform 0.5s ease" : "none";
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

    let autoSlide = setInterval(nextSlide, 2000);

    carousel.addEventListener("mouseenter", () => clearInterval(autoSlide));
    carousel.addEventListener("mouseleave", () => {
        autoSlide = setInterval(nextSlide, 2000);
    });

    
    window.addEventListener("resize", () => {
        setupCarousel();
    });

    
    setupCarousel();
});
