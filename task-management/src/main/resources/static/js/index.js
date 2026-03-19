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
