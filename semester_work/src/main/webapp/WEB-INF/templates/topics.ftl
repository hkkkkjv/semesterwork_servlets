<#include "base.ftl">
<#macro title>
    Forum
</#macro>
<#macro content>
    <div class="container mt-5">
        <h4>Add new Topic</h4>
        <form id="new-topic-form" class="mb-4" method="post" action="/topics">
            <div class="form-group">
                <label for="topicTitle">Title</label>
                <input type="text" id="topicTitle" name="topicTitle" class="form-control" required
                       placeholder="Enter topic title">
            </div>
            <div class="form-group">
                <label for="topicDescription">Description</label>
                <textarea id="topicDescription" name="topicDescription" class="form-control" rows="3" required
                          placeholder="Enter topic description"></textarea>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-purple">Add topic</button>
            </div>
        </form>
        <ul class="list-group">
            <#list topics as topic>
                <li class="list-group-item">
                    <a href="/topic?topicId=${topic.id}" class="link-no-style">
                        <h5>${topic.title}</h5>
                        <p><#if (topic.description?length>100)>
                            ${topic.description?substring(0,100)}...
                            <#else >
                                ${topic.description}
                            </#if></p>
                        <small class="text-muted">Last update at ${topic.updatedAt?string("dd.MM.yyyy HH:mm")}</small>
                    </a>
                </li>
            </#list>
        </ul>
    </div>
</#macro>
<@page/>
