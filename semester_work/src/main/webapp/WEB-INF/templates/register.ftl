<#include "base.ftl">
<#macro title>
    Registration
</#macro>
<#macro content>
    <div class="container mt-5">
        <#if error??>
            <div class="alert alert-danger">${error}</div>
        </#if>
        <#if success??>
            <div class="alert alert-success">${success}</div>
        </#if>
        <form action="register" method="post" class="registration-form mx-auto w-50" id="registrationForm">
            <div class="input-group mb-2">
                <div class="form-floating">
                    <input type="text" name="username" class="form-control" id="floatingInputGroup1"
                           placeholder="Username" required pattern="^[a-zA-Z0-9_]{3,20}$"
                           title="Username must be 3-20 characters long and can contain letters, numbers, and underscores.">
                    <label for="floatingInputGroup1">Username</label>
                </div>
            </div>
            <#if errors??&& errors.username??>
                <div class="text-danger">${errors.username}</div>
            </#if>
            <div id="usernameAvailability"></div>

            <div class="input-group mb-2">
                <div class="form-floating">
                    <input type="email" name="email" class="form-control" placeholder="Email" required
                           pattern="/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/"
                           title="Please enter a valid email address (for example, user@example.com).">
                    <label for="floatingInputGroup1">Email</label>
                </div>
            </div>
            <#if errors??&& errors.email??>
                <div class="text-danger">${errors.email}</div>
            </#if>
            <div id="emailAvailability"></div>

            <div class="input-group mb-2">
                <div class="form-floating">
                    <input type="password" name="password" class="form-control" placeholder="Password" required
                           pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                           title="Password must be at least 8 characters long and contain at least one letter and one number.">
                    <label for="floatingInputGroup1">Password</label>
                </div>
            </div>
            <#if errors??&& errors.password??>
                <div class="text-danger">${errors.password}</div>
            </#if>
            <div id="passwordValidationMessage"></div>

            <div class="input-group mb-2">
                <div class="form-floating">
                    <input type="password" name="confirmPassword" class="form-control" placeholder="Confirm Password"
                           required>
                    <label for="floatingInputGroup1">Confirm Password</label>
                </div>
            </div>
            <#if errors??&& errors.confirmPassword??>
                <div class="text-danger">${errors.confirmPassword}</div>
            </#if>
            <div id="confirmPasswordMessage"></div>

            <div class="form-floating mb-2">
                <select name="role" class="form-select" id="floatingSelect"
                        aria-label="Floating label select example">
                    <option value="STUDENT">STUDENT</option>
                    <option value="TEACHER">TEACHER</option>
                </select>
                <label for="floatingSelect">Choose your role</label>
            </div>
            <div class="text-center">
                <input type="submit" class="btn btn-purple w-100" value="Register" id="registerButton"></div>
        </form>
    </div>
    <script type="application/javascript">
        $(document).ready(
            function () {
                const usernameInput = $('input[name="username"]')
                const emailInput = $('input[name="email"]')
                const usernameAvailabilityMessage = $('#usernameAvailability')
                const emailAvailabilityMessage = $('#emailAvailability')
                const registerButton = $('#registerButton');
                const passwordInput = $('input[name="password"]');
                const confirmPasswordInput = $('input[name="confirmPassword"]');
                const passwordValidationMessage = $('#passwordValidationMessage');
                const confirmPasswordMessage = $('#confirmPasswordMessage');
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
                        registerButton.prop('disabled', true)
                    }
                });
                passwordInput.on('input', function () {
                    const password = passwordInput.val();
                    if (password.length > 0) {
                        const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
                        if (regex.test(password)) {
                            passwordValidationMessage.text("Valid password").css("color", "green");
                        } else {
                            passwordValidationMessage.text("Invalid password.").css("color", "red");
                        }
                    } else {
                        passwordValidationMessage.text("");
                    }
                });
                confirmPasswordInput.on('input', function () {
                    const password = passwordInput.val();
                    const confirmPassword = confirmPasswordInput.val();
                    if (confirmPassword.length > 0) {
                        if (password === confirmPassword) {
                            confirmPasswordMessage.text("Passwords match").css("color", "green");
                        } else {
                            confirmPasswordMessage.text("Passwords do not match").css("color", "red");
                        }
                    } else {
                        confirmPasswordMessage.text("");
                    }
                });
            });
    </script>
</#macro>
<@page/>