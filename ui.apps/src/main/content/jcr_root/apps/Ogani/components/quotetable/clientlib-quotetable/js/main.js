document.addEventListener("DOMContentLoaded", () => {
  fetch("/bin/quote")
    .then((response) => response.json())
    .then((data) => {
      const tbody = document.querySelector(".quote-table__body");
      tbody.innerHTML = "";

      data.forEach((item) => {
        tbody.innerHTML += `
                    <tr>
                        <td>${item.q || ""}</td>
                        <td>${item.a || ""}</td>
                        <td>${item.c || ""}</td>
                        <td>${item.h || ""}</td>
                    </tr>
                `;
      });
    })
    .catch((error) => console.error("Error fetching quotes:", error));
});
