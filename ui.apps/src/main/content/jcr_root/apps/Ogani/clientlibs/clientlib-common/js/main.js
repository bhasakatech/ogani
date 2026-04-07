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

    document.addEventListener('DOMContentLoaded', function() {
    const hamburgerOpenBtn = document.querySelector('.humberger__open');
    const menuWrapper = document.querySelector('.humberger__menu__wrapper');
    const menuOverlay = document.querySelector('.humberger__menu__overlay');

    if (hamburgerOpenBtn && menuWrapper && menuOverlay) {
        hamburgerOpenBtn.addEventListener('click', function() {
            menuWrapper.classList.add('show__humberger__menu__wrapper');
            menuOverlay.classList.add('active');
            
			document.body.style.overflow = 'hidden'; 
        });
    }

    if (menuOverlay) {
        menuOverlay.addEventListener('click', function() {
            menuWrapper.classList.remove('show__humberger__menu__wrapper');
            menuOverlay.classList.remove('active');
            
            document.body.style.overflow = 'auto'; 
        });
    }
});



    /*------------------
		Navigation
	--------------------*/

    document.addEventListener("DOMContentLoaded", function() {
    const topLevelItems = document.querySelectorAll('.humberger__menu__nav .cmp-navigation__item--level-0');

    topLevelItems.forEach(item => {
        const subMenu = item.querySelector('.cmp-navigation__group');
        if (subMenu) {
            const link = item.querySelector('.cmp-navigation__item-link');
            
            const arrow = document.createElement('span');
            arrow.classList.add('nav-arrow');
            
            arrow.innerHTML = '&#9658;'; 
            
            link.appendChild(arrow);

            arrow.addEventListener('click', function(e) {
                e.preventDefault();  
                e.stopPropagation(); 
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
            e.stopPropagation(); 
            
            if (categoryList.style.display === 'none' || categoryList.style.display === '') {
                categoryList.style.display = 'block';
            } else {
                categoryList.style.display = 'none';
            }
        });

        document.addEventListener('click', function() {
            categoryList.style.display = 'none';
        });
    }
});

})(jQuery);
