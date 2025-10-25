<#include "base.ftl">
<#macro title>
    Add event
</#macro>
<#macro content>
    <div class="container mb-4">
        <form class="mx-auto w-75" action="/addEvent" method="post" enctype="multipart/form-data">
            <div class="form-group mb-2">
                <label for="title">Title</label>
                <input type="text" class="form-control" id="title" name="title" required>
            </div>
            <div class="form-group mb-2">
                <label for="description">Description</label>
                <textarea class="form-control" id="description" name="description" required></textarea>
            </div>
            <div class="form-group mb-2">
                <label for="location">Location</label>
                <input type="text" class="form-control" id="location" name="location" required>
            </div>
            <div class="form-group mb-2">
                <label for="seatCount">Seat count</label>
                <input type="number" class="form-control" id="seatCount" name="seatCount" required>
            </div>
            <div class="form-group mb-2">
                <label for="category">Category</label>
                <select class="form-control" id="category" name="category" required>
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group mb-2">
                <label for="eventTime">Event Time</label>
                <input type="datetime-local" class="form-control" id="eventTime" name="eventTime" required>
            </div>
            <div class="form-group mb-2 ">
                <label for="eventImage">Event Image</label>
                <input type="file" class="btn btn-purple w-100" id="eventImage" name="eventImage"
                       accept=".jpg"
                       required>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-purple mx-auto">Add event</button></div>
        </form>
    </div>
</#macro>
<@page/>