const form = document.getElementById("myform");
const nameInput = document.getElementById("data-name");
const statusDiv = document.getElementById("status");

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = nameInput.value.trim();
  if (!name) return;

  try {
    const response = await fetch(`/registerName`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name }),
    });

    if (response.ok) {
      statusDiv.textContent = "Name registered successfully!";
      nameInput.value = "";
      loadNames();
    } else {
      statusDiv.textContent = "Error registering name.";
    }
  } catch (err) {
    statusDiv.textContent = "Connection error.";
  }
});

async function loadNames() {
    try {
        const response = await fetch(`/names`);
        if (!response.ok) throw new Error("Failed to fetch names");
        const names = await response.json();

        const tbody = document.getElementById("demo");
        tbody.innerHTML = "";

        names.forEach((entry, index) => {
            const row = document.createElement("tr");

            const idCell = document.createElement("th");
            idCell.scope = "row";
            idCell.textContent = entry.id ?? (index + 1);

            const userCell = document.createElement("td");
            userCell.textContent = entry.name ?? entry.user ?? entry.username ?? "";

            const dateCell = document.createElement("td");
            const rawDate = entry.timestamp ?? entry.date ?? "";
            let formatted = rawDate;
            if (rawDate) {
                try {
                    const d = new Date(rawDate);
                    if (!isNaN(d)) formatted = d.toLocaleString();
                } catch (_) {}
            }
            dateCell.textContent = formatted;

            row.appendChild(idCell);
            row.appendChild(userCell);
            row.appendChild(dateCell);

            tbody.appendChild(row);
        });

        statusDiv.textContent = "";
    } catch (err) {
        console.error(err);
        statusDiv.textContent = "Could not load names.";
    }
}

setInterval(loadNames, 5000);
loadNames();