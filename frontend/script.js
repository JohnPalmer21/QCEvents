// Event data with more details
const events = [
  { 
    id: 1, 
    title: "Career Fair", 
    date: "May 20", 
    time: "1:00 PM - 5:00 PM",
    location: "Student Union Grand Hall",
    description: "Connect with top employers from various industries who are actively recruiting for internships and full-time positions. Bring your resume and dress professionally.",
    image: "career" // This will be used to select a color for the event card
  },
  { 
    id: 2, 
    title: "Art Club Showcase", 
    date: "May 22",
    time: "3:00 PM - 7:00 PM",
    location: "Visual Arts Building Gallery",
    description: "Join us for an evening celebrating student artwork across various mediums. Refreshments will be served and many pieces will be available for purchase.",
    image: "art" 
  },
  { 
    id: 3, 
    title: "Basketball Tournament", 
    date: "May 25",
    time: "10:00 AM - 4:00 PM",
    location: "Campus Recreation Center",
    description: "The annual 3-on-3 basketball tournament is back! Form your team and register by May 20th. Prizes for winning teams and free refreshments for all participants.",
    image: "sports" 
  },
  { 
    id: 4, 
    title: "Tech Workshop", 
    date: "May 28",
    time: "2:00 PM - 5:00 PM",
    location: "Computer Science Building Room 201",
    description: "Learn the basics of web development in this hands-on workshop. No prior experience required! Bring your laptop and come ready to build your first website.",
    image: "tech" 
  }
];

// Color palette for event images
const eventColors = {
  career: "#5c6bc0",  // blue
  art: "#ec407a",     // pink
  sports: "#66bb6a",  // green
  tech: "#ff7043"     // orange
};

// For the home page - populate event cards
if (document.getElementById("event-list")) {
  const list = document.getElementById("event-list");
  
  events.forEach(event => {
    // Create event card
    const card = document.createElement("div");
    card.className = "event-card";
    
    // Set color based on event type or use default
    const bgColor = eventColors[event.image] || "#5c6bc0";
    
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
}

// For the event detail page
if (document.getElementById("event-detail")) {
  // Get event ID from URL
  const urlParams = new URLSearchParams(window.location.search);
  const eventId = parseInt(urlParams.get('id'));
  
  // Find the event
  const event = events.find(e => e.id === eventId);
  
  if (event) {
    const detailContainer = document.getElementById("event-detail");
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
    // Event not found
    const detailContainer = document.getElementById("event-detail");
    detailContainer.innerHTML = `
      <h1>Event Not Found</h1>
      <p>Sorry, the event you're looking for doesn't exist.</p>
      <a href="index.html" class="back-link"><i class="fas fa-arrow-left"></i> Back to All Events</a>
    `;
  }
}