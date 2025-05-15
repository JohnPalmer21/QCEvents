const events = [
  { id: 1, title: "Career Fair", date: "May 20" },
  { id: 2, title: "Art Club Showcase", date: "May 22" },
];

// Populate index.html with event cards
if (document.getElementById("event-list")) {
  const list = document.getElementById("event-list");
  events.forEach(event => {
    const div = document.createElement("div");
    div.className = "event-card";
    div.innerHTML = `
      <h3>${event.title}</h3>
      <p>${event.date}</p>
      <a href="event.html?id=${event.id}">View Details</a>
    `;
    list.appendChild(div);
  });
}