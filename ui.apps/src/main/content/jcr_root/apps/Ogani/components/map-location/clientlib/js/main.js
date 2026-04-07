"use strict";

(function ($) {

  //map

  document.addEventListener("DOMContentLoaded", function() {
    const mapElements = document.querySelectorAll('.js-leaflet-map');
    
    mapElements.forEach(function(mapElement) {
        if (mapElement._leaflet_id) {
            return; 
        }

        const lat = parseFloat(mapElement.getAttribute('data-lat'));
        const lng = parseFloat(mapElement.getAttribute('data-lng'));
        
        const title = mapElement.getAttribute('data-title');
        const phone = mapElement.getAttribute('data-phone');
        const address = mapElement.getAttribute('data-address');

        const zoomLevel = 10; 
        const apiKey = "bb19ff895a5547fb93d840a1dd17fd01"; 

        if (!isNaN(lat) && !isNaN(lng)) {
            const map = L.map(mapElement).setView([lat, lng], zoomLevel);

            L.tileLayer(`https://maps.geoapify.com/v1/tile/osm-carto/{z}/{x}/{y}.png?apiKey=${apiKey}`, {
                attribution: '',
                maxZoom: 20
            }).addTo(map);

            let popupHtml = '<div class="custom-leaflet-popup">';
            
            if (title) {
                popupHtml += `<h4>${title}</h4>`;
            }
            if (phone || address) {
                popupHtml += `<ul>`;
                if (phone) popupHtml += `<li><b>Phone:</b> ${phone}</li>`;
                if (address) popupHtml += `<li><b>Address:</b> ${address}</li>`;
                popupHtml += `</ul>`;
            }
            popupHtml += '</div>';

            const marker = L.marker([lat, lng]).addTo(map);

            if (title || phone || address) {
                marker.bindPopup(popupHtml).openPopup();
            }
        }
    });
});

//   ============================
})(jQuery);
