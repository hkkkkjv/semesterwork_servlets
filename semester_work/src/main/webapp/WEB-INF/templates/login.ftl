<#include "base.ftl">
<#macro title>
    Login
</#macro>
<#macro content>
    <div class="container mt-5">
        <#if error??>
            <div class="alert alert-danger">${error}</div>
        </#if>
        <form method="post" class="border p-4 rounded shadow mx-auto w-50">
            <div class="form-group mb-2">
                <input type="text" class="form-control p-3" name="username" placeholder="Username" required>
            </div>
            <div class="form-group mb-2">
                <input type="password" class="form-control p-3" name="password" placeholder="Password" required>
            </div>
            <div class="form-group form-check mb-2 p-3">
                <input type="checkbox" class="form-check-input" name="rememberMe" id="rememberMe">
                <label class="form-check-label" for="rememberMe">Remember me</label>
            </div>
            <button type="submit" class="btn btn-purple w-100 p-2">Login</button>
            <p class="mt-3">Not registered yet? <a href="/register">Registration</a></p>
        </form>
    </div>
</#macro>
<@page/>
