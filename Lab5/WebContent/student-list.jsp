<%-- 
    Document   : student-list
    Created on : 21 thg 11, 2025, 16:24:01
    Author     : NGHIA
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List (MVC)</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Custom CSS for a professional look */
        body { font-family: 'Inter', sans-serif; background-color: #f7f9fb; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .success { background-color: #e6ffed; color: #1f8745; padding: 10px; border-radius: 6px; margin-bottom: 20px; border: 1px solid #c8f5d7; }
        .error { background-color: #ffe6e6; color: #cc0000; padding: 10px; border-radius: 6px; margin-bottom: 20px; border: 1px solid #f5c8c8; }
        .data-table th, .data-table td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #e0e0e0; }
        .data-table th { background-color: #eef2ff; color: #4338ca; cursor: pointer; user-select: none; }
        .data-table tr:hover { background-color: #f0f4ff; }
        .action-link { margin-right: 10px; color: #4338ca; font-weight: 500; }
    </style>
    
    <script>
        // Function to submit the search/filter form
        function applyFilters(newSortBy, currentOrder) {
            const form = document.getElementById('filter-form');
            const sortByInput = document.getElementById('sortBy');
            const orderInput = document.getElementById('order');

            if (newSortBy && newSortBy === sortByInput.value) {
                // If clicking the same column, reverse the order
                orderInput.value = currentOrder === 'asc' ? 'desc' : 'asc';
            } else if (newSortBy) {
                // If clicking a new column, set it and default to asc
                sortByInput.value = newSortBy;
                orderInput.value = 'asc';
            }
            
            // Update the action to ensure we route back to the list
            form.action = 'student?action=list';
            form.submit();
        }
    </script>
</head>
<body>
    <div class="container">
        <h1 class="text-3xl font-extrabold text-gray-800 mb-6 border-b pb-2">📚 Student Management (MVC)</h1>

        <!-- Display success message if param.message exists (Task 3.1) -->
        <c:if test="${not empty param.message}">
            <div class="success">${param.message}</div>
        </c:if>

        <!-- Display error message if param.error exists -->
        <c:if test="${not empty param.error}">
            <div class="error">${param.error}</div>
        </c:if>

        <!-- Search, Filter, and Add Controls (Task 5.3 & 7.3) -->
        <div class="bg-white p-6 rounded-xl shadow-lg mb-6">
            <form id="filter-form" action="student" method="GET" class="space-y-4 md:space-y-0 md:flex md:gap-4 md:items-end">
                <input type="hidden" name="action" value="list">
                <input type="hidden" id="sortBy" name="sortBy" value="${sortBy}">
                <input type="hidden" id="order" name="order" value="${order}">

                <div class="flex-grow">
                    <label for="keyword" class="block text-sm font-medium text-gray-700">Search (Code, Name, or Email)</label>
                    <input type="text" id="keyword" name="keyword" value="${keyword}" placeholder="Enter keyword..."
                           class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 p-2 border">
                </div>
                
                <div class="w-full md:w-auto">
                    <label for="major" class="block text-sm font-medium text-gray-700">Filter by Major</label>
                    <select id="major" name="major" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 p-2 border bg-white">
                        <option value="All Majors" ${selectedMajor == 'All Majors' ? 'selected' : ''}>All Majors</option>
                        <option value="Computer Science" ${selectedMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                        <option value="Information Technology" ${selectedMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                        <option value="Business Administration" ${selectedMajor == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                        <!-- Add more majors as needed -->
                    </select>
                </div>
                
                <div class="flex gap-4">
                    <button type="submit" class="w-full md:w-auto px-4 py-2 bg-indigo-600 text-white font-semibold rounded-md shadow-md hover:bg-indigo-700 transition duration-150">
                        Apply Filters
                    </button>
                    <!-- Button to add new student (Task 3.1) -->
                    <a href="student?action=new" class="w-full md:w-auto px-4 py-2 bg-green-600 text-white font-semibold rounded-md shadow-md hover:bg-green-700 transition duration-150 text-center">
                        + Add New Student
                    </a>
                </div>
            </form>
            
            <c:if test="${not empty keyword || selectedMajor != 'All Majors'}">
                <div class="mt-4 text-sm text-gray-600">
                    Active Filters: 
                    <c:if test="${not empty keyword}"><span class="font-medium mr-2">Search: "${keyword}"</span></c:if>
                    <c:if test="${selectedMajor != 'All Majors'}"><span class="font-medium mr-2">Major: ${selectedMajor}</span></c:if>
                    <a href="student?action=list" class="text-red-500 hover:text-red-700 font-semibold ml-2">Clear All Filters</a>
                </div>
            </c:if>
        </div>

        <!-- Student List Table -->
        <div class="bg-white rounded-xl shadow-lg overflow-x-auto">
            <table class="data-table w-full border-collapse">
                <thead>
                    <tr>
                        <th onclick="applyFilters('id', '${order}')">
                            ID 
                            <c:if test="${sortBy == 'id'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </th>
                        <th onclick="applyFilters('studentCode', '${order}')">
                            Code 
                            <c:if test="${sortBy == 'studentCode'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </th>
                        <th onclick="applyFilters('fullName', '${order}')">
                            Name 
                            <c:if test="${sortBy == 'fullName'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </th>
                        <th onclick="applyFilters('email', '${order}')">
                            Email 
                            <c:if test="${sortBy == 'email'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </th>
                        <th onclick="applyFilters('major', '${order}')">
                            Major 
                            <c:if test="${sortBy == 'major'}">${order == 'asc' ? '▲' : '▼'}</c:if>
                        </th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Use c:forEach to loop through students (Task 3.1) -->
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.id}</td>
                            <td>${student.studentCode}</td>
                            <td>${student.fullName}</td>
                            <td>${student.email}</td>
                            <td>${student.major}</td>
                            <td>${student.createdAt}</td>
                            <td>
                                <!-- Edit link -->
                                <a href="student?action=edit&id=${student.id}" class="action-link hover:underline">Edit</a>
                                <!-- Delete link - with client-side confirmation (Task 4.2) -->
                                <a href="student?action=delete&id=${student.id}" 
                                   onclick="return confirm('Are you sure you want to delete student ${student.studentCode} - ${student.fullName}?')" 
                                   class="action-link text-red-600 hover:underline">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <!-- Handle empty list with c:if (Task 3.1) -->
                    <c:if test="${empty students}">
                        <tr>
                            <td colspan="7" class="text-center py-6 text-gray-500">No students found matching your criteria.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>