<%-- 
    Document   : student-form
    Created on : 21 thg 11, 2025, 16:27:23
    Author     : NGHIA
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <!-- Dynamic title using c:choose (Task 3.2) -->
        <c:choose>
            <c:when test="${student != null}">Edit Student: ${student.fullName}</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f7f9fb; }
        .container { max-width: 600px; margin: 40px auto; padding: 20px; }
        .error-message { color: #cc0000; font-size: 0.85rem; margin-top: 4px; }
    </style>
</head>
<body>
    <div class="container bg-white p-8 rounded-xl shadow-2xl">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 pb-2 border-b">
            <!-- Dynamic heading using c:if (Task 3.2) -->
            <c:if test="${student != null}">Edit Existing Student</c:if>
            <c:if test="${student == null}">Add New Student</c:if>
        </h1>
        
        <form action="student" method="POST" class="space-y-6">
            
            <!-- Hidden field for action (insert or update) (Task 3.2) -->
            <input type="hidden" name="action" 
                   value="${student != null ? 'update' : 'insert'}">
            
            <!-- Hidden field for id if editing (Task 3.2) -->
            <c:if test="${student != null}">
                <input type="hidden" name="id" value="${student.id}">
            </c:if>

            <!-- Student Code Field -->
            <div>
                <label for="studentCode" class="block text-sm font-medium text-gray-700">Student Code</label>
                <input type="text" id="studentCode" name="studentCode" 
                       value="${student.studentCode}"
                       placeholder="e.g., S1005"
                       class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-3 border 
                              ${student != null ? 'bg-gray-100' : 'focus:border-indigo-500 focus:ring-indigo-500'}"
                       <!-- Student code is readonly if editing (Task 3.2) -->
                       ${student != null ? 'readonly' : 'required'}>
                <c:if test="${not empty errors.studentCode}">
                    <p class="error-message">${errors.studentCode}</p>
                </c:if>
            </div>

            <!-- Full Name Field -->
            <div>
                <label for="fullName" class="block text-sm font-medium text-gray-700">Full Name</label>
                <input type="text" id="fullName" name="fullName" 
                       value="${student.fullName}"
                       placeholder="Enter full name"
                       class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-3 border focus:border-indigo-500 focus:ring-indigo-500"
                       required>
                <c:if test="${not empty errors.fullName}">
                    <p class="error-message">${errors.fullName}</p>
                </c:if>
            </div>

            <!-- Email Field -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                <input type="email" id="email" name="email" 
                       value="${student.email}"
                       placeholder="e.g., example@university.edu"
                       class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-3 border focus:border-indigo-500 focus:ring-indigo-500"
                       required>
                <c:if test="${not empty errors.email}">
                    <p class="error-message">${errors.email}</p>
                </c:if>
            </div>
            
            <!-- Major Field -->
            <div>
                <label for="major" class="block text-sm font-medium text-gray-700">Major</label>
                <select id="major" name="major" 
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-3 border focus:border-indigo-500 focus:ring-indigo-500 bg-white"
                        required>
                    <option value="" disabled selected>Select Major</option>
                    <c:set var="currentMajor" value="${student.major != null ? student.major : param.major}" />
                    <option value="Computer Science" ${currentMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${currentMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Business Administration" ${currentMajor == 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                    <!-- Add more majors as needed -->
                </select>
                <c:if test="${not empty errors.major}">
                    <p class="error-message">${errors.major}</p>
                </c:if>
            </div>

            <div class="flex justify-end space-x-4 pt-4">
                <!-- Submit button with dynamic text (Task 3.2) -->
                <button type="submit" 
                        class="px-6 py-2 bg-indigo-600 text-white font-semibold rounded-md shadow-md hover:bg-indigo-700 transition duration-150">
                    <c:choose>
                        <c:when test="${student != null}">Update Student</c:when>
                        <c:otherwise>Add Student</c:otherwise>
                    </c:choose>
                </button>
                
                <!-- Cancel button -->
                <a href="student?action=list" 
                   class="px-6 py-2 border border-gray-300 text-gray-700 font-semibold rounded-md shadow-sm hover:bg-gray-100 transition duration-150">
                   Cancel
                </a>
            </div>
            
            <c:if test="${not empty errors.global}">
                <p class="error-message text-center">${errors.global}</p>
            </c:if>
        </form>
    </div>
</body>
</html>