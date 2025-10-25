<#include "base.ftl">
<#macro title>
    User Profile
</#macro>
<#macro content>
    <div class="container mt-5">
        <#if isCurrentUser >
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div></div>
                <a href="/logout" class="btn btn-danger">Logout</a>
            </div>
        </#if>
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">Username:${userProfile.username}</h5>
                <p class="card-text">Email:${userProfile.email}</p>
                <p class="card-text">Role:${userProfile.role}</p>
                <p class="card-text">About:${userProfile.about}</p>
                <#if isCurrentUser >
                    <a href="/editProfile" class="btn btn-purple">Edit Profile</a>
                </#if>
            </div>
        </div>
        <#if isCurrentUser >
            <div class="d-flex justify-content-start mb-4">
                <a href="/myEvents" class="btn btn-purple">My events</a>
                <a href="/myBonusPoints" class="btn btn-purple ms-2">My bonus points</a>
            </div>
        </#if>
    </div>
</#macro>

<@page/>