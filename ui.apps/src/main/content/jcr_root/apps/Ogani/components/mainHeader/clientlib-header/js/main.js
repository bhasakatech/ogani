/*  ---------------------------------------------------
    Template Name: Ogani
    Description:  Ogani eCommerce  HTML Template
    Author: Colorlib
    Author URI: https://colorlib.com
    Version: 1.0
    Created: Colorlib
---------------------------------------------------------  */

"use strict";

(function ($) {
  /*------------------
        Preloader
    --------------------*/
  $(window).on("load", function () {
    $(".loader").fadeOut();
    $("#preloder").delay(200).fadeOut("slow");

    /*------------------
            Gallery filter
        --------------------*/
    $(".featured__controls li").on("click", function () {
      $(".featured__controls li").removeClass("active");
      $(this).addClass("active");
    });
    if ($(".featured__filter").length > 0) {
      var containerEl = document.querySelector(".featured__filter");
      var mixer = mixitup(containerEl);
    }
  });

  /*------------------
        Background Set
    --------------------*/
  $(".set-bg").each(function () {
    var bg = $(this).data("setbg");
    $(this).css("background-image", "url(" + bg + ")");
  });

  document.addEventListener("DOMContentLoaded", function () {
    const hamburgerOpenBtn = document.querySelector(".humberger__open");
    const menuWrapper = document.querySelector(".humberger__menu__wrapper");
    const menuOverlay = document.querySelector(".humberger__menu__overlay");

    if (hamburgerOpenBtn && menuWrapper && menuOverlay) {
      hamburgerOpenBtn.addEventListener("click", function () {
        menuWrapper.classList.add("show__humberger__menu__wrapper");
        menuOverlay.classList.add("active");

        document.body.style.overflow = "hidden";
      });
    }

    if (menuOverlay) {
      menuOverlay.addEventListener("click", function () {
        menuWrapper.classList.remove("show__humberger__menu__wrapper");
        menuOverlay.classList.remove("active");

        document.body.style.overflow = "auto";
      });
    }
  });

  /*------------------
    Navigation
  --------------------*/

  document.addEventListener("DOMContentLoaded", function () {
    const topLevelItems = document.querySelectorAll(
      ".humberger__menu__nav .cmp-navigation__item--level-0",
    );

    topLevelItems.forEach((item) => {
      const subMenu = item.querySelector(".cmp-navigation__group");
      if (subMenu) {
        const link = item.querySelector(".cmp-navigation__item-link");

        const arrow = document.createElement("span");
        arrow.classList.add("nav-arrow");

        arrow.innerHTML = "&#9658;";

        link.appendChild(arrow);

        arrow.addEventListener("click", function (e) {
          e.preventDefault();
          e.stopPropagation();
          item.classList.toggle("expanded");
        });
      }
    });
  });

  // ======================================
  // Dropdowns for ALL Department section

  $(document).ready(function () {
    const $heroList = $(".hero__categories__list");
    const $searchList = $(".hero__search__categories__list");

    // Show dropdown by default if on home.html page
    if (window.location.pathname.includes("/home.html")) {
      $heroList.show();
    }

    $(document).on("click", ".hero__categories__all", function (e) {
      e.preventDefault();
      e.stopPropagation();

      $heroList.slideToggle(400);
    });

    $(".hero__search__categories").on("click", function (e) {
      e.preventDefault();
      e.stopPropagation();

      if ($searchList.is(":visible")) {
        $searchList.hide();
      } else {
        $searchList.show();
      }
    });


  });

  /* =========================
   HEADER CART TOTAL
========================= */
  document.addEventListener("DOMContentLoaded", function () {

    function getCart() {
      try {
        return JSON.parse(localStorage.getItem("cart")) || [];
      } catch {
        return [];
      }
    }

    function updateHeaderCart() {

      const cart = getCart();

      let total = 0;
      let count = 0;

      cart.forEach(item => {
        const qty = item.quantity || 1;
        const price = item.price || 0;

        total += price * qty;
        count += qty;
      });

      /* ===== UPDATE TOTAL PRICE ===== */
      const totalEl = document.getElementById("cart-total");
      const totalMobileEl = document.getElementById("cart-total-mobile");

      if (totalEl) totalEl.innerText = "$" + total.toFixed(2);
      if (totalMobileEl) totalMobileEl.innerText = "$" + total.toFixed(2);

      /* ===== UPDATE ITEM COUNT ===== */
      const countEl = document.getElementById("cart-count");
      const countMobileEl = document.getElementById("cart-count-mobile");

      if (countEl) countEl.innerText = count;
      if (countMobileEl) countMobileEl.innerText = count;
    }

    updateHeaderCart();

    /* OPTIONAL: update when storage changes (multi-tab sync) */
    window.addEventListener("storage", updateHeaderCart);
  });
})(jQuery);
