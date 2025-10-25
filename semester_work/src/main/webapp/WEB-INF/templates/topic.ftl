<#include "base.ftl">
<#macro title>
    Forum
</#macro>
<#macro content>
    <div class="container mt-5">
        <div class="card mb-4">
            <div class="card-body">
                <h3 class="card-title">${topic.title}</h3>
                <p class="card-text">${topic.description}</p>
                <#if topic.participants?? &&(topic.participants?size>0)>
                    <p class="card-text">Participants:
                        <#list topic.participants as participant>
                            <#if (participant_index < 5)>
                                <a href="/profile?id=${participant.id}"
                                   class="link-no-style"><strong>${participant.username}</strong></a>
                                <#if participant_has_next>, </#if>
                            </#if>
                        </#list>
                    </p>
                </#if>
                <p class="card-text">Created at ${topic.createdAt?string("dd.MM.yyyy HH:mm")}
                    by <a href="/profile?id=${topic.createdBy.id}" class="link-no-style">${topic.createdBy.username}</a>
                </p>
            </div>
        </div>

        <form method="post" class="border p-4 rounded shadow w-50 mx-auto">
            <div class="form-group">
                <textarea name="text" class="form-control" rows="4" maxlength="500" id="forumText"
                          required></textarea>
                <div class="d-flex justify-content-between">
                    <small id="charCount" class="text-muted ms-auto mb-3">0/500</small>
                </div>
            </div>
            <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" name="anonymous" id="anonymousCheckbox">
                <label class="form-check-label" for="anonymousCheckbox">Stay Anonymous</label>
            </div>
            <div class="text-center">
                <button type="submit" class="btn-purple btn w-100">Send</button>
            </div>
        </form>
        <#if pinnedForums?? &&(pinnedForums?size>0)>
            <ul class="list-group mt-3">
                <#list pinnedForums as forum>
                    <li class="list-group-item d-flex justify-content-between align-items-center mb-1">
                        <div><strong>
                                <#if forum.user?has_content>
                                    <a href="/profile?id=${forum.user.id}" class="link-no-style">
                                        ${forum.user.username}
                                    </a>
                                <#else >
                                    Anonymous
                                </#if>
                            </strong>:${forum.text}<br>
                            <small class="text-muted">${forum.date?string("dd.MM.yyyy HH:mm")}</small>
                        </div>
                        <#if user.id == topic.createdBy.id>
                            <button type="button" class="btn btn-deep-purple btn-sm pin-forum"
                                    data-forum-id="${forum.id}"
                                    data-action="unpin">Unpin
                            </button>
                        <#else >
                            <small class="text-muted">Pinned</small>
                        </#if>
                    </li>
                </#list>
            </ul>
            <hr>
        </#if>

        <#if unpinnedForums??&&(unpinnedForums?size>0)>
            <ul class="list-group mt-3">
                <#list unpinnedForums as forum>
                    <li class="list-group-item d-flex justify-content-between align-items-center mb-1">
                        <div><strong>
                                <#if forum.user?has_content>
                                    <a href="/profile?id=${forum.user.id}" class="link-no-style">
                                        ${forum.user.username}
                                    </a>                                <#else >
                                    Anonymous
                                </#if>
                            </strong>:${forum.text}<br>
                            <small class="text-muted">${forum.date?string("dd.MM.yyyy HH:mm")}</small>
                        </div>
                        <#if user.id == topic.createdBy.id>
                            <button type="button" class="btn btn-deep-purple btn-sm unpin-forum"
                                    data-forum-id="${forum.id}"
                                    data-action="pin">Pin
                            </button>
                        </#if>
                    </li>
                </#list>
            </ul>
        </#if>
    </div>
    <script>
        $(document).ready(function () {
            $('#forumText').on('input', function () {
                var count = $(this).val().length;
                $('#charCount').text(count + '/500');
            });
            $('.pin-forum, .unpin-forum').click(function () {
                const forumId = $(this).data('forum-id');
                const action = $(this).data('action');
                $.ajax({
                    url: '/topic',
                    type: 'POST',
                    data: {
                        forumId: forumId, action: action
                    },
                    success: function (response) {
                        location.reload();
                    },
                    error: function () {
                        alert('Error pinning  the forum post');
                    }
                });
            });
        });
    </script>
</#macro>
<@page/>