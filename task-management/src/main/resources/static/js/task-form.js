document.addEventListener("DOMContentLoaded", async () => {
    const form = document.getElementById("taskForm");
    const params = new URLSearchParams(window.location.search);
    const taskId = params.get("id");

    document.getElementById("dueDate").min = new Date().toISOString().slice(0, 16);

    if (taskId) {
        try {
            const res = await fetch(`/tasks/${taskId}`);
            if (res.status === 404) {
                window.location.href = "index.html";
                return;
            }
            if (!res.ok) throw new Error("Failed to fetch task");

            const task = await res.json();
            form.title.value = task.title || "";
            form.description.value = task.description || "";
            form.status.value = task.status || "TODO";
            form.dueDate.value = task.dueDate
                ? new Date(task.dueDate).toISOString().slice(0, 16)
                : "";
        } catch (err) {
            console.error(err);
            window.location.href = "index.html";
        }
    }

    document.getElementById("submitButton").addEventListener("click", async () => {
        if (!form.checkValidity()) {
            form.classList.add("was-validated");
            return;
        }

        const data = {
            title: form.title.value,
            description: form.description.value,
            status: form.status.value,
            dueDate: form.dueDate.value
        };

        let url = "/tasks";
        let method = "POST";

        if (taskId) {
            url = `/tasks/${taskId}`;
            method = "PATCH";
        }

        try {
            const response = await fetch(url, {
                method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            if (!response.ok) throw new Error("Request failed");

            window.location.href = "index.html";
        } catch (error) {
            console.error(error);
            alert("Something went wrong");
        }
    });
});