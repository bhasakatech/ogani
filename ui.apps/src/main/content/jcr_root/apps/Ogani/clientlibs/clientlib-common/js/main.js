/*  ---------------------------------------------------
    Template Name: Ogani
    Description:  Ogani eCommerce  HTML Template
    Author: Colorlib
    Author URI: https://colorlib.com
    Version: 1.0
    Created: Colorlib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");

        /*------------------
            Gallery filter
        --------------------*/
        $('.featured__controls li').on('click', function () {
            $('.featured__controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.featured__filter').length > 0) {
            var containerEl = document.querySelector('.featured__filter');
            var mixer = mixitup(containerEl);
        }
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    //Humberger Menu
    // $(".humberger__open").on('click', function () {
    //     $(".humberger__menu__wrapper").addClass("show__humberger__menu__wrapper");
    //     $(".humberger__menu__overlay").addClass("active");
    //     $("body").addClass("over_hid");
    // });

    // $(".humberger__menu__overlay").on('click', function () {
    //     $(".humberger__menu__wrapper").removeClass("show__humberger__menu__wrapper");
    //     $(".humberger__menu__overlay").removeClass("active");
    //     $("body").removeClass("over_hid");
    // });

    document.addEventListener('DOMContentLoaded', function() {
    // 1. Select the necessary elements from your HTML
    const hamburgerOpenBtn = document.querySelector('.humberger__open');
    const menuWrapper = document.querySelector('.humberger__menu__wrapper');
    const menuOverlay = document.querySelector('.humberger__menu__overlay');

    // 2. Event listener to OPEN the menu
    if (hamburgerOpenBtn && menuWrapper && menuOverlay) {
        hamburgerOpenBtn.addEventListener('click', function() {
            // Add the classes that trigger your CSS transitions
            menuWrapper.classList.add('show__humberger__menu__wrapper');
            menuOverlay.classList.add('active');
            
            // Optional: Prevent the main body from scrolling while the menu is open
            document.body.style.overflow = 'hidden'; 
        });
    }

    // 3. Event listener to CLOSE the menu when clicking the dark overlay
    if (menuOverlay) {
        menuOverlay.addEventListener('click', function() {
            // Remove the classes to slide the menu back out
            menuWrapper.classList.remove('show__humberger__menu__wrapper');
            menuOverlay.classList.remove('active');
            
            // Restore main body scrolling
            document.body.style.overflow = 'auto'; 
        });
    }
});



    /*------------------
		Navigation
	--------------------*/
    // $(".mobile-menu").slicknav({
    //     prependTo: '#mobile-menu-wrap',
    //     allowParentLinks: true
    // });

    // ====================================

    document.addEventListener("DOMContentLoaded", function() {
    // 1. Find all top-level items in the hamburger menu
    const topLevelItems = document.querySelectorAll('.humberger__menu__nav .cmp-navigation__item--level-0');

    topLevelItems.forEach(item => {
        // 2. Check if this item has a sub-menu (children)
        const subMenu = item.querySelector('.cmp-navigation__group');
        
        if (subMenu) {
            const link = item.querySelector('.cmp-navigation__item-link');
            
            // 3. Create the clickable arrow span
            const arrow = document.createElement('span');
            arrow.classList.add('nav-arrow');
            
            // Insert a Right-pointing arrow (►) initially
            arrow.innerHTML = '&#9658;'; 
            
            // Append arrow right after the text inside the link
            link.appendChild(arrow);

            // 4. Add click listener to the arrow ONLY
            arrow.addEventListener('click', function(e) {
                e.preventDefault();  // Stop the link from navigating
                e.stopPropagation(); // Stop the event from bubbling
                
                // Toggle the 'expanded' class to handle the CSS display and rotation
                item.classList.toggle('expanded');
            });
        }
    });
});

    // ======================================

$(document).ready(function(){
    
    $(document).on('click', '.hero__categories__all', function(){
        console.log("--> Click detected! Toggling dropdown.");
        $(".hero__categories__list").slideToggle(400); 
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const categoryTrigger = document.querySelector('.hero__search__categories');
    const categoryList = document.querySelector('.hero__search__categories__list');

    if (categoryTrigger && categoryList) {
        categoryTrigger.addEventListener('click', function(e) {
            e.stopPropagation(); // Prevents click from immediately closing it
            
            // Toggle between displaying the block and hiding it
            if (categoryList.style.display === 'none' || categoryList.style.display === '') {
                categoryList.style.display = 'block';
            } else {
                categoryList.style.display = 'none';
            }
        });

        // Optional: Close dropdown if user clicks anywhere else on the page
        document.addEventListener('click', function() {
            categoryList.style.display = 'none';
        });
    }
});

    // $('.hero__categories__all').on('click', function(){
    //     $('.hero__categories ul').slideToggle(400);
    // });

    


})(jQuery);