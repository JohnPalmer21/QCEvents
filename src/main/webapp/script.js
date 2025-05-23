// Color palette for event images
const eventColors = {
  career: "#5c6bc0",
  art: "#ec407a",
  sports: "#66bb6a",
  tech: "#ff7043"
};

// Global variable to hold ec2 instance URL
// let ec2InstanceUrl = "http://ec2-3-86-107-16.compute-1.amazonaws.com:8080";

// Pads a number with a leading zero if needed (ex: 9 -> 09 for time formatting)
function pad(number) {
  return number.toString().padStart(2, '0');
}

// Fetch and display event list on index.html
if (document.getElementById("event-list")) {
  fetch("/qcevents/events")
    .then(response => response.json())
    .then(events => {
      console.log("Fetched events:", events); // ✅ Log for debugging
      const list = document.getElementById("event-list");
      list.innerHTML = ""; // Clear loading text
      events.forEach(event => {
        console.log("Rendering event:", event); // ✅ Log each event

        const imageKey = event.image || "career"; // fallback to career
        const bgColor = eventColors[imageKey] || "#5c6bc0";
        const title = event.title || "Untitled Event";
        const date = event.date || "Unknown Date";
        const description = event.description || "No description provided";

        const card = document.createElement("div");
        card.className = "event-card";
        card.innerHTML = `
          <div class="event-image" style="background-color: ${bgColor}"></div>
          <div class="event-content">
            <h3 class="event-title">${title}</h3>
            <span class="event-date"><i class="far fa-calendar-alt"></i> ${date}</span>
            <p>${description.substring(0, 100)}${description.length > 100 ? '...' : ''}</p>
            <a href="event.html?id=${event.id}" class="event-link">View Details</a>
          </div>
        `;
        list.appendChild(card);
      });
    })
    .catch(error => console.error("Error loading events:", error));
}

// Fetch and display filtered events using FilterEventsServlet
function fetchFilteredEvents(major, interest) {
  var urlParams = new URLSearchParams(window.location.search);
  if (major != undefined && major != null && major != "") {
    urlParams.set('major', major);
    console.log("Filtering by major:", major);
  }
  if (interest != undefined && interest != null && interest != "") {
    urlParams.set('interest', interest);
    console.log("Filtering by interest:", interest);
  }
  var url = "/qcevents/events/filter?" + urlParams.toString();
  console.log("Fetching filtered events from:", url);
  fetch(url)
    .then(function(response) { return response.json(); })
    .then(function(events) {
      var list = document.getElementById("event-list");
      if (!list) return;
      list.innerHTML = "";
      for (var i = 0; i < events.length; i++) {
        var event = events[i];
        var imageKey = event.image || "career";
        var bgColor = eventColors[imageKey] || "#5c6bc0";
        var title = event.title || "Untitled Event";
        var date = event.date || "Unknown Date";
        var description = event.description || "No description provided";
        var card = document.createElement("div");
        card.className = "event-card";
        card.innerHTML =
          '<div class="event-image" style="background-color: ' + bgColor + '"></div>' +
          '<div class="event-content">' +
            '<h3 class="event-title">' + title + '</h3>' +
            '<span class="event-date"><i class="far fa-calendar-alt"></i> ' + date + '</span>' +
            '<p>' + description.substring(0, 100) + (description.length > 100 ? '...' : '') + '</p>' +
            '<a href="event.html?id=' + event.id + '" class="event-link">View Details</a>' +
          '</div>';
        list.appendChild(card);
        console.log("Rendered filtered event:", event);
      }
    })
    .catch(function(error) {
      console.error("Error loading filtered events:", error);
    });
}

// Fetch and display event details on event.html with filter support
if (document.getElementById("event-detail")) {
  var urlParams = new URLSearchParams(window.location.search);
  var eventId = parseInt(urlParams.get('id'));
  var major = urlParams.get('major');
  var interest = urlParams.get('interest');
  var fetchUrl;
  if ((major != null && major != "") || (interest != null && interest != "")) {
    fetchUrl = "/qcevents/events/filter?" + urlParams.toString();
    console.log("Fetching filtered event details from:", fetchUrl);
  } else {
    fetchUrl = "/qcevents/events";
    console.log("Fetching all events for event details from:", fetchUrl);
  }
  fetch(fetchUrl)
    .then(response => response.json())
    .then(events => {
      var event = null;
      if (Array.isArray(events)) {
        for (var i = 0; i < events.length; i++) {
          if (parseInt(events[i].id) == eventId) {
            event = events[i];
            break;
          }
        }
      } else if (typeof events == 'object' && events.id == eventId) {
        event = events;
      }

      var detailContainer = document.getElementById("event-detail");
      if (event) {
        const imageKey = event.image || "career";
        const bgColor = eventColors[imageKey] || "#5c6bc0";
        detailContainer.innerHTML = `
          <div class="event-banner" style="background-color: ${bgColor}"></div>
          <h1>${event.title}</h1>
          <span class="event-date"><i class="far fa-calendar-alt"></i> ${event.date}</span>
          <p><strong><i class="far fa-clock"></i> Time:</strong> ${event.time}</p>
          <p><strong><i class="fas fa-map-marker-alt"></i> Location:</strong> ${event.location}</p>
          <p><strong><i class="fas fa-info-circle"></i> Description:</strong> ${event.description}</p>
          <button id="rsvp-button" class="btn"><i class="fas fa-calendar-check"></i> RSVP to this Event</button>
          <a href="index.html" class="back-link"><i class="fas fa-arrow-left"></i> Back to All Events</a>
        `;

        if (event.flaggedByGemini) {
          const flagDiv = document.getElementById("gemini-flag");
          if (flagDiv) flagDiv.style.display = "block";
        }

        const exportBtn = document.getElementById("export-gcal-btn");
        if (exportBtn) {
          exportBtn.style.display = "inline-block";
          exportBtn.onclick = function() {
            exportEventToICS(event);
          };
        }

        document.addEventListener("DOMContentLoaded", function() {
          const mapDiv = document.getElementById("map");
          if (mapDiv && event.location) {
            mapDiv.style.display = "block";
            mapboxgl.accessToken = 'pk.eyJ1IjoiZGF2aW5zb21hbiIsImEiOiJjbWF6MW5jY3YwZzAyMmpwcnF2djl5Mm9hIn0.G4XZtb-RbCzifyJgvqI5NA';

            const qcBuildings = {
              "Kiely Hall": [-73.8157, 40.7365],
              "Science Building": [-73.8152, 40.7372],
              "Rosenthal Library": [-73.8162, 40.7369],
              "Student Union": [-73.8172, 40.7362],
              "Powdermaker Hall": [-73.8142, 40.7367],
              "Remsen Hall": [-73.8137, 40.7371],
              "Colden Auditorium": [-73.8155, 40.7357],
              "Dining Hall": [-73.8170, 40.7357],
            };

            let found = false;
            for (const [building, coords] of Object.entries(qcBuildings)) {
              if (event.location.toLowerCase().includes(building.toLowerCase())) {
                const map = new mapboxgl.Map({
                  container: 'map',
                  style: 'mapbox://styles/mapbox/streets-v11',
                  center: coords,
                  zoom: 17
                });
                new mapboxgl.Marker()
                  .setLngLat(coords)
                  .setPopup(new mapboxgl.Popup().setText(building))
                  .addTo(map);
                found = true;
                break;
              }
            }

            if (!found) {
              fetch(`https://api.mapbox.com/geocoding/v5/mapbox.places/${encodeURIComponent(event.location)}.json?access_token=${mapboxgl.accessToken}`)
                .then(res => res.json())
                .then(data => {
                  if (data.features && data.features.length > 0) {
                    const [lng, lat] = data.features[0].center;
                    const map = new mapboxgl.Map({
                      container: 'map',
                      style: 'mapbox://styles/mapbox/streets-v11',
                      center: [lng, lat],
                      zoom: 17
                    });
                    new mapboxgl.Marker()
                      .setLngLat([lng, lat])
                      .setPopup(new mapboxgl.Popup().setText(event.title))
                      .addTo(map);
                  } else {
                    mapDiv.style.display = "block";
                  }
                })
                .catch(() => { mapDiv.style.display = "none"; });
            }
          }
        });

        document.getElementById("rsvp-button").addEventListener("click", () => {
          const phone = localStorage.getItem("userPhone");
          if (!phone) {
            alert("You must log in before RSVPing.");
            sessionStorage.setItem("redirectAfterLogin", window.location.href);
            window.location.href = "login.html";
            return;
          }

          fetch("/qcevents/rsvp", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `phone=${encodeURIComponent(phone)}&event_id=${encodeURIComponent(event.id)}`
          })
            .then(response => {
              if (response.ok) {
                alert("RSVP successful!");
              } else {
                alert("RSVP failed. Please try again.");
              }
            })
            .catch(error => {
              console.error("Error during RSVP:", error);
              alert("Error while RSVPing.");
            });
        });
      } else {
        detailContainer.innerHTML = `
          <h1>Event Not Found</h1>
          <p>Sorry, the event you're looking for doesn't exist.</p>
          <a href="index.html" class="back-link"><i class="fas fa-arrow-left"></i> Back to All Events</a>
        `;
      }
    });
}

// Export event to ICS file for Google Calendar
function exportEventToICS(event) {
  var startDate = event.date.replace(/-/g, '');
  var hour, minute;
  if (event.time && event.time.indexOf(':') > -1) {
    var timeParts = event.time.split(':');
    hour = timeParts[0];
    minute = timeParts[1];
  } else {
    hour = '12';
    minute = '00';
  }
  var startTime = pad(hour) + pad(minute) + '00';
  var endHour = pad(parseInt(hour, 10) + 1);
  var endTime = endHour + pad(minute) + '00';

  var icsContent = [
    'BEGIN:VCALENDAR',
    'VERSION:2.0',
    'BEGIN:VEVENT',
    'SUMMARY:' + event.title,
    'DESCRIPTION:' + event.description,
    'LOCATION:' + event.location,
    'DTSTART:' + startDate + 'T' + startTime,
    'DTEND:' + startDate + 'T' + endTime,
    'END:VEVENT',
    'END:VCALENDAR'
  ].join('\n');

  var blob = new Blob([icsContent], { type: 'text/calendar' });
  var link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = event.title.replace(/\s+/g, '_') + '.ics';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// Fetch and display user's RSVP'd events on my-events.html
if (document.getElementById("my-events-list")) {
  const phone = localStorage.getItem("userPhone");
  if (!phone) {
    alert("You must log in to view your events.");
    window.location.href = "login.html";
  } else {
    fetch(`/qcevents/my-events?phone=${encodeURIComponent(phone)}`)
      .then(response => response.json())
      .then(events => {
        const list = document.getElementById("my-events-list");
        if (events.length === 0) {
          list.innerHTML = `<p>You haven’t RSVP’d to any events yet.</p>`;
        } else {
          list.innerHTML = "";
          events.forEach(event => {
            const imageKey = event.image || "career";
            const bgColor = eventColors[imageKey] || "#5c6bc0";
            const title = event.title || "Untitled Event";
            const date = event.date || "Unknown Date";
            const description = event.description || "No description";

            const card = document.createElement("div");
            card.className = "event-card";
            card.innerHTML = `
              <div class="event-image" style="background-color: ${bgColor}"></div>
              <div class="event-content">
                <h3 class="event-title">${title}</h3>
                <span class="event-date"><i class="far fa-calendar-alt"></i> ${date}</span>
                <p>${description.substring(0, 100)}${description.length > 100 ? '...' : ''}</p>
                <a href="event.html?id=${event.id}" class="event-link">View Details</a>
              </div>
            `;
            list.appendChild(card);
          });
        }
      })
      .catch(error => {
        console.error("Error loading RSVP'd events:", error);
        document.getElementById("my-events-list").innerHTML = `<p>Could not load your events.</p>`;
      });
  }
}

// Listen for changes on the major and interest dropdowns and filter events
var majorFilter = document.getElementById("major-filter");
var interestFilter = document.getElementById("interest-filter");

function handleFilterChange() {
  var majorValue = majorFilter ? majorFilter.value : "all";
  var interestValue = interestFilter ? interestFilter.value : "all";
  fetchFilteredEvents(majorValue, interestValue);
}

if (majorFilter) {
  majorFilter.addEventListener("change", handleFilterChange);
}
if (interestFilter) {
  interestFilter.addEventListener("change", handleFilterChange);
}

