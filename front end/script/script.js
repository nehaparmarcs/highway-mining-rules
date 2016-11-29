var typingTimer; //timer identifier
var doneTypingInterval = 2000;
var placeSearch, autocomplete;

 var locationss = [
      ['High risk', 37.3472753, -121.9046515, 20],
      ['caution', 37.3394506, -121.8991359, 19],
      ['caution', 37.3301157, -121.8990128, 19],
      ['caution', 37.3313414, -121.896420, 19],

    ];

var marker, i;

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

for (i = 0; i < locationss.length; i++) { 
        marker = new google.maps.Marker({
        position: new google.maps.LatLng(locationss[i][1], locationss[i][2]),
        map: map
      });
    }





    directionsDisplay.setMap(map);
    document.getElementById('submit').addEventListener('click', function() {
        calculateAndDisplayRoute(directionsService, directionsDisplay);
    });
}

function calculateAndDisplayRoute(directionsService, directionsDisplay) {

    directionsService.route({
        origin: document.getElementById('autocomplete').value,
        destination: document.getElementById('end').value,
        //waypoints: waypts,
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
                summaryPanel.innerHTML += route.legs[i].end_address + '<br> Total time:';
                summaryPanel.innerHTML += route.legs[i].duration.text + '<br><br> total distance:';
                summaryPanel.innerHTML += route.legs[i].distance.text + '<br><br>';
                console.log(route.legs[i]);
            }
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });
}