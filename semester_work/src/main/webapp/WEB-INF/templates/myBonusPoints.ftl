<#include "base.ftl">
<#macro title>My bonus points</#macro>
<#macro content>
    <div class="container mt-5">
        <#if user.role=="STUDENT">
            <div class="alert alert-info">
                <h5>Total active bonus points: ${totalActivePoints}</h5>
            </div>
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>Event</th>
                    <th>Points</th>
                    <th>Expiration Date</th>
                    <th>Issue Date</th>
                </tr>
                </thead>
                <tbody>
                <#if (activeBonuses?size>0)>
                    <#list activeBonuses as bonus>
                        <tr>
                            <td><a href="/event?id=${bonus.event.id}" class="link-no-style">${bonus.event.title}</a></td>
                            <td>${bonus.points}</td>
                            <td>${bonus.expirationDate}</td>
                            <td>${bonus.date}</td>
                        </tr>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="4" class="text-center">You have no active bonuses</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        <#elseif user.role=="TEACHER">
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>Student</th>
                    <th>Event</th>
                    <th>Points</th>
                    <th>Expiration Date</th>
                    <th>Issue Date</th>
                </tr>
                </thead>
                <tbody>
                <#if (teacherBonuses?size>0)>
                    <#list teacherBonuses as bonus>
                        <tr>
                            <td><a href="/profile?id=${bonus.student.id}" class="link-no-style">${bonus.student.username}</a></td>
                            <td><a href="/event?id=${bonus.event.id}" class="link-no-style">${bonus.event.title}</a></td>
                            <td>${bonus.points}</td>
                            <td>${bonus.expirationDate}</td>
                            <td>${bonus.date}</td>
                        </tr>
                    </#list>
                <#else >
                    <tr>
                        <td colspan="5" class="text-center">No bonuses assigned to students</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </#if>
    </div>
</#macro>
<@page/>