// Fetch and display flagged events, with optional filtering
function fetchFlaggedEvents(major, interest) {
  var url = '/qcevents/events/filter?flagged=true';
  if (major && major != 'all') {
    url += '&major=' + encodeURIComponent(major);
  }
  if (interest && interest != 'all') {
    url += '&interest=' + encodeURIComponent(interest);
  }
  fetch(url)
    .then(function(response) { return response.json(); })
    .then(function(events) {
      renderAdminEventList(events);
    })
    .catch(function(error) {
      var messageDiv = document.getElementById('admin-event-message');
      messageDiv.innerHTML = '<p>Error loading events.</p>';
      console.error('Error loading flagged events:', error);
    });
}

// Render the admin event list with Approve/Reject buttons
function renderAdminEventList(events) {
  var tbody = document.getElementById('admin-event-tbody');
  var messageDiv = document.getElementById('admin-event-message');
  tbody.innerHTML = '';
  messageDiv.innerHTML = '';
  if (!events || events.length == 0) {
    messageDiv.innerHTML = '<p>No flagged events found.</p>';
    return;
  }
  events.forEach(function(event) {
    var row = document.createElement('tr');
    row.innerHTML =
      '<td>' + (event.title || '') + '</td>' +
      '<td>' + (event.date || '') + '</td>' +
      '<td>' + (event.major || '') + '</td>' +
      '<td>' + (event.interest || '') + '</td>' +
      '<td>' + (event.flag_reason || '') + '</td>' +
      '<td>' +
        '<button class="approve-btn" data-id="' + event.id + '">Approve</button>' +
        '<button class="reject-btn" data-id="' + event.id + '">Reject</button>' +
      '</td>';
    tbody.appendChild(row);
  });
  var approveBtns = document.getElementsByClassName('approve-btn');
  Array.prototype.forEach.call(approveBtns, function(btn) {
    btn.addEventListener('click', function() {
      var eventId = this.getAttribute('data-id');
      approveEvent(eventId);
    });
  });
  var rejectBtns = document.getElementsByClassName('reject-btn');
  Array.prototype.forEach.call(rejectBtns, function(btn) {
    btn.addEventListener('click', function() {
      var eventId = this.getAttribute('data-id');
      rejectEvent(eventId);
    });
  });
}

// Approve event
function approveEvent(eventId) {
  fetch('/secure/admin/approve', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'event_id=' + encodeURIComponent(eventId)
  })
    .then(function(response) { return response.json(); })
    .then(function(data) {
      if (data.success) {
        alert('Event approved!');
        reloadAdminEvents();
      } else {
        alert('Failed to approve event.');
      }
    })
    .catch(function() { alert('Error approving event.'); });
}

// Reject event
function rejectEvent(eventId) {
  fetch('/secure/admin/reject', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'event_id=' + encodeURIComponent(eventId)
  })
    .then(function(response) { return response.json(); })
    .then(function(data) {
      if (data.success) {
        alert('Event rejected!');
        reloadAdminEvents();
      } else {
        alert('Failed to reject event.');
      }
    })
    .catch(function() { alert('Error rejecting event.'); });
}

// Reload events with current filter
function reloadAdminEvents() {
  var major = document.getElementById('admin-major-filter').value;
  var interest = document.getElementById('admin-interest-filter').value;
  fetchFlaggedEvents(major, interest);
}

// Set up filter listeners
window.addEventListener('DOMContentLoaded', function() {
  var majorFilter = document.getElementById('admin-major-filter');
  var interestFilter = document.getElementById('admin-interest-filter');
  if (majorFilter && interestFilter) {
    majorFilter.addEventListener('change', reloadAdminEvents);
    interestFilter.addEventListener('change', reloadAdminEvents);
  }
  reloadAdminEvents(); // Initial load
});
