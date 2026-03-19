document.addEventListener("DOMContentLoaded", async () => {
    console.log("index.js is running");

    const res = await fetch("/tasks")
    const data = await res.json();
    console.log(data)


   const tableBody = document.getElementById("task-table-body");
   tableBody.innerHTML = "";

    data.forEach((task) => {
        const row = document.createElement("tr");

        const title = task.title;
        const description = task.description ?? "N/A";
        const status = formatStatus(task.status);
        const date = new Date(task.dueDate).toLocaleString();

        const action = `
        <div class="dropdown">
            <button class="btn btn-success dropdown-toggle" type="button" data-bs-toggle="dropdown">
            Actions
            </button>
            <ul class="dropdown-menu dropdown-menu-success">
                <li><a class="dropdown-item" href="/task-form.html?id=${task.id}">Edit</a></li>
                <li><button class="dropdown-item text-danger" onclick="deleteTask(${task.id})">Delete</button></li>
            </ul>
        </div>`;

        row.innerHTML = `
          <td>${title}</td>
          <td>${description}</td>
          <td>${status}</td>
          <td>${date}</td>
          <td>${action}</td>
        `;

        tableBody.appendChild(row);
        updateCounts();
    })
});

async function deleteTask(id) {
    await fetch(`/tasks/${id}`, { method: "DELETE" });
    location.reload();
}


function formatStatus(status) {
    return status
        .toLowerCase()
        .replace(/_/g, " ")
        .replace(/\b\w/g, char => char.toUpperCase());
}

document.getElementById("searchInput").addEventListener("input", () => {
    const field = document.getElementById("searchField").value;
    const query = document.getElementById("searchInput").value.toLowerCase();
    const rows = document.querySelectorAll("#task-table-body tr");

    rows.forEach(row => {
        const fieldIndex = { title: 0, description: 1 };
        const cellText = row.cells[fieldIndex[field]].textContent.toLowerCase();
        row.style.display = cellText.includes(query) ? "" : "none";
    });
});


function filterByStatus(status) {
    const rows = document.querySelectorAll("#task-table-body tr");
    rows.forEach(row => {
        const statusCell = row.cells[2].textContent.trim();
        if (status === 'ALL') {
            row.style.display = "";
        } else {
            const statusMap = {
                'TODO': 'Todo',
                'IN_PROGRESS': 'In Progress',
                'DONE': 'Done'
            };
            row.style.display = statusCell === statusMap[status] ? "" : "none";
        }
    });
}

function updateCounts() {
    const rows = document.querySelectorAll("#task-table-body tr");
    let todo = 0, inProgress = 0, done = 0;
    rows.forEach(row => {
        const status = row.cells[2].textContent.trim();
        if (status === 'Todo') todo++;
        else if (status === 'In Progress') inProgress++;
        else if (status === 'Done') done++;
    });
    document.getElementById("count-all").textContent = rows.length;
    document.getElementById("count-todo").textContent = todo;
    document.getElementById("count-inprogress").textContent = inProgress;
    document.getElementById("count-done").textContent = done;
}