<#include "base.ftl">
<#macro title>
    Add points
</#macro>
<#macro content>
    <div class="container mb-4">
        <#if event??>
            <h4>${event.title}</h4>
            <p>${event.description}</p>
            <p><strong>Date:</strong> ${event.time?string("yyyy-MM-dd HH:mm:ss")}</p>
        <#else>
            <p>Event didn't find</p>
        </#if>
        <form class="mx-auto w-75" action="/addBonusPoints" method="post">
            <input type="hidden" name="eventId" value="${event.id}">
            <div class="form-group mb-2">
                <label for="points">Points for all:</label>
                <input type="number" class="form-control" id="points" name="points" placeholder="Points for all"
                       required>
            </div>
            <div class="form-group mb-2">
                <label for="expiryDate">Expiry date:</label>
                <input type="date" class="form-control" id="expiryDate" name="expiryDate" required>
            </div>
            <h5>Choose students:</h5>
            <div class="form-group mb-3">
                <#if users??&&(users?size>0)>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Student</th>
                            <th>Points</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list users as user>
                            <tr>
                                <td>
                                    <label for="user_${user.id}">${user.username}</label>
                                </td>
                                <td>
                                    <input type="number" class="form-control" id="user_${user.id}" name="userIds"
                                           value="${user.id}" hidden>
                                    <input type="number" class="form-control" id="pointsForUser_${user.id}"
                                           name="userPoints"
                                           placeholder="Points for ${user.username}">
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                <#else >
                    <p>There are no registered users for this event.</p>
                </#if>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-purple mx-auto">Add points</button>
            </div>
        </form>
    </div>
    <script>
        $(document).ready(function () {
            $('#points').on('input', function () {
                var pointsValue = $(this).val();
                $('input[name^="userPoints"]').val(pointsValue);
            });
        });
    </script>
</#macro>
<@page/>