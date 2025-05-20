// Color palette for event images
const eventColors = {
  career: "#5c6bc0",
  art: "#ec407a",
  sports: "#66bb6a",
  tech: "#ff7043"
};

// Fetch and display event list on index.html
if (document.getElementById("event-list")) {
  fetch("http://3.86.107.16:8080/your-app-name/events")
    .then(response => response.json())
    .then(events => {
      const list = document.getElementById("event-list");
      events.forEach(event => {
        const bgColor = eventColors[event.image] || "#5c6bc0";
        const card = document.createElement("div");
        card.className = "event-card";
        card.innerHTML = `
          <div class="event-image" style="background-color: ${bgColor}"></div>
          <div class="event-content">
            <h3 class="event-title">${event.title}</h3>
            <span class="event-date"><i class="far fa-calendar-alt"></i> ${event.date}</span>
            <p>${event.description.substring(0, 100)}${event.description.length > 100 ? '...' : ''}</p>
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
  fetch("http://3.86.107.16:8080/your-app-name/events")
    .then(response => response.json())
    .then(events => {
      const event = events.find(e => e.id === eventId);
      const detailContainer = document.getElementById("event-detail");
      if (event) {
        const bgColor = eventColors[event.image] || "#5c6bc0";
        detailContainer.innerHTML = `
          <div class="event-banner" style="background-color: ${bgColor}"></div>
          <h1>${event.title}</h1>
          <span class="event-date"><i class="far fa-calendar-alt"></i> ${event.date}</span>
          <p><strong><i class="far fa-clock"></i> Time:</strong> ${event.time}</p>
          <p><strong><i class="fas fa-map-marker-alt"></i> Location:</strong> ${event.location}</p>
          <p><strong><i class="fas fa-info-circle"></i> Description:</strong> ${event.description}</p>
          <a href="index.html" class="back-link"><i class="fas fa-arrow-left"></i> Back to All Events</a>
        `;
      } else {
        detailContainer.innerHTML = `
          <h1>Event Not Found</h1>
          <p>Sorry, the event you're looking for doesn't exist.</p>
          <a href="index.html" class="back-link"><i class="fas fa-arrow-left"></i> Back to All Events</a>
        `;
      }
    });
}