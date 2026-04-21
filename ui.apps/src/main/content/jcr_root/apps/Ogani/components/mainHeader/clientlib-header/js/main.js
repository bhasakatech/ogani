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

    /*------------------
		Navigation
	--------------------*/
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
    const $searchInput = $('#cf-search-input');
    const $suggestionsBox = $('#search-suggestions');
    const $searchForm = $searchInput.closest('form');
    let suggestionDebounceTimer;

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
    // search dropdown ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    const fetchSuggestions = async (query) => {
      if (query.trim().length === 0) {
        $suggestionsBox.hide();
        return;
      }

      try {
        const response = await fetch(`/bin/api/cfsuggestions?q=${encodeURIComponent(query)}`);
        const data = await response.json();

        $suggestionsBox.empty();

        if (data.length > 0) {
          data.forEach(item => {
            const $div = $('<div>', { class: 'suggestion-item' });
            
            $div.html(`
              <img src="${item.image || '/content/dam/default-fallback.png'}" class="suggestion-img" alt="${item.name}">
              <span class="suggestion-name">${item.name}</span>
            `);
            
            $div.on('click', function () {
              $searchInput.val(item.name);
              $suggestionsBox.hide();
              
            });

            $suggestionsBox.append($div);
          });
        } else {
          $suggestionsBox.html('<div style="padding: 15px; color: #666; font-size: 14px;">No results found.</div>');
        }
        
        $suggestionsBox.show();

      } catch (error) {
        console.error('Error fetching suggestions:', error);
      }
    };

    if ($searchInput.length > 0) {
      $searchInput.on('input', function () {
        clearTimeout(suggestionDebounceTimer);
        const query = $(this).val();

        suggestionDebounceTimer = setTimeout(function () {
          fetchSuggestions(query);
        }, 300);
      });
    }

    if ($searchForm.length > 0) {
      $searchForm.on('submit', function (e) {
        e.preventDefault();
        fetchSuggestions($searchInput.val()); 
      });

    }

    $(document).on('click', function (e) {
      if (!$(e.target).closest($searchForm).length && !$(e.target).closest($suggestionsBox).length) {
        $suggestionsBox.hide();
      }
    });
  });
})(jQuery);
