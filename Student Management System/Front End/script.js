const apiUrl = "http://localhost:8080/api/students";

// جلب الطلاب وعرضهم
function fetchStudents() {
  fetch(apiUrl)
    .then(res => res.json())
    .then(data => {
      const tbody = document.querySelector("#studentsTable tbody");
      tbody.innerHTML = "";
      data.forEach(student => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${student.id}</td>
          <td>${student.firstName}</td>
          <td>${student.lastName}</td>
          <td>${student.email}</td>
          <td><button class="deleteBtn" onclick="deleteStudent(${student.id})">🗑 حذف</button></td>
        `;
        tbody.appendChild(tr);
      });
    })
    .catch(err => console.error("Error fetching students:", err));
}

// إضافة طالب
document.getElementById("studentForm").addEventListener("submit", (e) => {
  e.preventDefault();
  const student = {
    firstName: document.getElementById("firstName").value,
    lastName: document.getElementById("lastName").value,
    email: document.getElementById("email").value,
    passwordHash: document.getElementById("passwordHash").value,
  };

  fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(student),
  })
    .then(res => res.json())
    .then(() => {
      fetchStudents();
      document.getElementById("studentForm").reset();
    });
});

// حذف طالب
function deleteStudent(id) {
  fetch(`${apiUrl}/${id}`, { method: "DELETE" })
    .then(() => fetchStudents());
}

// تحميل الطلاب عند بداية الصفحة
fetchStudents();
