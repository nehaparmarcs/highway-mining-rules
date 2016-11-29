var typingTimer; //timer identifier
var doneTypingInterval = 2000;
var placeSearch, autocomplete;

function initialize() {
    initMap();
    initAutoComplete();
}

function initAutoComplete() {
    autocomplete2 = new google.maps.places.Autocomplete(document.getElementById('end'));
    autocomplete = new google.maps.places.Autocomplete(document.getElementById('autocomplete'));
    autocomplete3 = new google.maps.places.Autocomplete(document.getElementById('city'));
}


function initMap() {
    var directionsService = new google.maps.DirectionsService;
    var directionsDisplay = new google.maps.DirectionsRenderer;
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: {
            lat: 37.332716,
            lng: -121.882374
        },
    });
    directionsDisplay.setMap(map);
    document.getElementById('submit').addEventListener('click', function() {
        calculateAndDisplayRoute(directionsService, directionsDisplay);
    });
}

function calculateAndDisplayRoute(directionsService, directionsDisplay) {
    var waypts = [];
    var checkboxArray = document.getElementById('waypoints');
    for (var i = 0; i < checkboxArray.length; i++) {
        if (checkboxArray.options[i].selected) {
            waypts.push({
                location: checkboxArray[i].value,
                stopover: true
            });
        }
    }
    directionsService.route({
        origin: document.getElementById('autocomplete').value,
        destination: document.getElementById('end').value,
        waypoints: waypts,
        optimizeWaypoints: true,
        travelMode: 'DRIVING'
    }, function(response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);
            var route = response.routes[0];
            var summaryPanel = document.getElementById('directions-panel');
            summaryPanel.innerHTML = '';
            // For each route, display summary information.
            for (var i = 0; i < route.legs.length; i++) {
                var routeSegment = i + 1;
                summaryPanel.innerHTML += '<b>Route Segment: ' + routeSegment + '</b><br>';
                summaryPanel.innerHTML += route.legs[i].start_address + ' to ';
                summaryPanel.innerHTML += route.legs[i].end_address + '<br>';
                summaryPanel.innerHTML += route.legs[i].duration.text + '<br><br>';
                summaryPanel.innerHTML += route.legs[i].distance.text + '<br><br>';
                console.log(route.legs[i]);
            }
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });
}