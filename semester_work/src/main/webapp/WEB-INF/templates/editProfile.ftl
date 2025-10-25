<#include "base.ftl">
<#macro title>
    User Profile
</#macro>
<#macro content>
    <div class="container mt-5">
        <form action="/editProfile" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" class="form-control" id="username" name="username" value="${user.username}" required
                       pattern="^[a-zA-Z0-9_]{3,20}$"
                       title="Username must be 3-20 characters long and can contain letters, numbers, and underscores.">
            </div>
            <div id="usernameAvailability"></div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${user.email}" required
                       pattern="/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/"
                       title="Please enter a valid email address (for example, user@example.com).">
            </div>
            <div id="emailAvailability"></div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" id="password" name="password"
                       pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                       title="Password must be at least 8 characters long and contain at least one letter and one number.">
            </div>
            <div id="passwordValidationMessage"></div>
            <div class="form-group">
                <label for="about">About</label>
                <textarea class="form-control" id="about" name="about" maxlength="255"
                          oninput="updateCharacterCount()">${user.about}</textarea>
                <small id="charCount" class="form-text text-muted">${user.about?length}/255</small>
            </div>
            <button type="submit" class="btn btn-purple mt-3 mx-auto">Update</button>
        </form>
    </div>
    <script>
        function updateCharacterCount() {
            const aboutField = document.getElementById('about');
            const charCountDisplay = document.getElementById('charCount');
            const currentLength = aboutField.value.length;
            charCountDisplay.textContent = currentLength + '/255';
            if (currentLength > 255) {
                aboutField.value = aboutField.value.substring(0, 255);
                charCountDisplay.textContent = '255/255';
            }
        }

        $(document).ready(
            function () {
                const usernameInput = $('input[name="username"]')
                const emailInput = $('input[name="email"]')
                const usernameAvailabilityMessage = $('#usernameAvailability')
                const emailAvailabilityMessage = $('#emailAvailability')
                const registerButton = $('#registerButton');
                const passwordInput = $('input[name="password"]');
                const passwordValidationMessage = $('#passwordValidationMessage');
                usernameInput.on('input', function () {
                    const username = usernameInput.val()
                    if (username.length > 0) {
                        $.get('register?action=checkUsername&username=' + encodeURIComponent(username), function (data) {
                            if (data.available && username.match(/^[a-zA-Z0-9_]{3,20}$/)) {
                                usernameAvailabilityMessage.text("Username is available").css("color", "green");
                            } else {
                                usernameAvailabilityMessage.text("Username is not available").css("color", "red");
                            }
                        }, 'json')
                    } else {
                        usernameAvailabilityMessage.text("");
                        registerButton.prop('disabled', true)
                    }
                });
                emailInput.on('input', function () {
                    const email = emailInput.val()
                    if (email.length > 0) {
                        $.get('register?action=checkEmail&email=' + encodeURIComponent(email), function (data) {
                            if (data.available && email.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)) {
                                emailAvailabilityMessage.text("Email is available").css("color", "green");
                            } else {
                                emailAvailabilityMessage.text("Email is not available").css("color", "red");
                            }
                        }, 'json')
                    } else {
                        emailAvailabilityMessage.text("");
                    }
                });
                passwordInput.on('input', function () {
                    const password = passwordInput.val();
                    if (password.length > 0) {
                        const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
                        if (regex.test(password)) {
                            passwordValidationMessage.text("Valid password").css("color", "green");
                        } else {
                            passwordValidationMessage.text("Invalid password. Must be at least 8 characters long and include at least one letter and one number.").css("color", "red");
                        }
                    } else {
                        passwordValidationMessage.text("");
                    }
                });
            });
    </script>

</#macro>

<@page/>