<#include "base.ftl">
<#macro title>My events</#macro>
<#macro content>
    <div class="container mt-5">
        <#if user.role=="STUDENT">
            <h2 class="mb-3">My registrations</h2>
            <ul class="list-group mb-4">
                <#if registrations?? && (registrations?size> 0)>
                    <#list registrations as registration>
                        <li class="list-group-item d-flex align-items-start">
                            <a href="event?id=${registration.event.id}"
                               class="text-decoration-none d-flex align-items-start">
                                <img src="/images?image_name=${registration.event.imagePath}"
                                     alt="${registration.event.title}"
                                     class="img-fluid me-3"
                                     style="height: 150px; width: 200px;object-fit: scale-down">
                                <div>
                                    <h5>${registration.event.title}</h5>
                                    <p>${registration.event.description}</p>
                                    <small class="text-muted">${registration.event.time?string("yyyy-MM-dd HH:mm:ss")}</small>
                                </div>
                            </a>
                            <span class="ms-auto"><small>Registration time</small><br>
                            <small class="text-muted">${registration.registrationTime?string("yyyy-MM-dd HH:mm:ss")}</small></span>
                        </li>
                    </#list>
                <#else>
                    <li class="list-group-item">There are no registrations for events.</li>
                </#if>
            </ul>
        <#elseif user.role=="TEACHER">
            <a href="/addEvent" class="btn btn-purple mb-3">Add event</a>
            <ul class="list-group mb-4">
                <#if events?? && (events?size> 0)>
                    <#list events as event>
                        <li class="list-group-item d-flex justify-content-between align-items-start">
                            <a href="/event?id=${event.id}" class="text-decoration-none d-flex align-items-start">
                                <img src="/images?image_name=${event.imagePath}"
                                     alt="${event.title}"
                                     class="img-fluid me-3"
                                     style="height: 150px; width: 200px;object-fit: scale-down">
                                <div>
                                    <h5>${event.title}</h5>
                                    <p>${event.description}</p>
                                    <small class="text-muted">${event.time?string("yyyy-MM-dd HH:mm:ss")}</small>
                                </div>
                            </a>
                            <#if event.hasPassed()>
                                <a href="/addBonusPoints?eventId=${event.id}" class="btn btn-purple btn-sm">Add points</a>
                            </#if>
                        </li>
                    </#list>
                    <#else >
                    <li class="list-group-item">There are no events.</li>
                </#if>
            </ul>
        </#if>
    </div>

</#macro>
<@page/>