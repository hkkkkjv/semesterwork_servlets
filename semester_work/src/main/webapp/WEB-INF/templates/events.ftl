<#include "base.ftl">
<#macro title>Events</#macro>
<#macro content>
    <div class="container mt-5">
        <div class="mb-3 d-flex">
            <a href="/eventSearch" class="btn btn-deep-purple ms-auto">Search</a>
        </div>
        <ul class="list-group mb-3" id="eventList">
            <#list events as event>
                <li class="list-group-item" data-category-id="${event.category.id}">
                    <a href="/event?id=${event.id}" class="link-no-style d-flex align-items-start">
                        <img src="/images?image_name=${event.imagePath}" alt="${event.title}" class="img-fluid me-3"
                             style="height: 150px; width: 200px;object-fit: scale-down">
                        <div>
                            <h5>${event.title}</h5>
                            <p><#if (event.description?length>100)>
                                    ${event.description?substring(0,100)}...
                                <#else >
                                    ${event.description}
                                </#if>
                            </p>
                            <small class="text-muted">${event.time?string("yyyy-MM-dd HH:mm:ss")}</small>
                        </div>
                    </a>
                </li>
            </#list>
        </ul>
    </div>
</#macro>
<@page/>
