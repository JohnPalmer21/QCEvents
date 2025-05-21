// Color palette for event images
const eventColors = {
  career: "#5c6bc0",
  art: "#ec407a",
  sports: "#66bb6a",
  tech: "#ff7043"
};

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

// Fetch and display event details on event.html
if (document.getElementById("event-detail")) {
  const urlParams = new URLSearchParams(window.location.search);
  const eventId = parseInt(urlParams.get('id'));
  fetch("/qcevents/events")
    .then(response => response.json())
    .then(events => {
      const event = events.find(e => e.id === eventId);
      const detailContainer = document.getElementById("event-detail");
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
