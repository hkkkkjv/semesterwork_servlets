<#include "base.ftl">
<#macro title>Events</#macro>
<#macro content>
    <div class="container mt-5">
        <div class="mb-3 d-flex justify-content-between align-items-center">
            <input type="text" id="searchInput" class="form-control" placeholder="Search events...">
            <div class="dropdown">
                <button class="btn dropdown-toggle" type="button" id="sortOrderDropdown" data-bs-toggle="dropdown"
                        aria-expanded="false">Date desc
                </button>
                <ul class="dropdown-menu" aria-labelledby="sortOrderDropdown">
                    <li><a class="dropdown-item" data-sort="dateAsc">Date asc</a></li>
                    <li><a class="dropdown-item" data-sort="dateDesc">Date desc</a></li>
                    <li><a class="dropdown-item" data-sort="titleAsc">Title (A-Z)</a></li>
                    <li><a class="dropdown-item" data-sort="titleDesc">Title (Z-A)</a></li>
                </ul>
            </div>
        </div>
        <form id="filterForm">
            <div class="row mb-3">
                <div class="col">
                    <label for="categoryId">Category</label>
                    <select id="categoryId" class="form-select">
                        <option value="">All categories</option>
                        <#list categories as category>
                            <option value="${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="col">
                    <label for="startDate">Start date</label>
                    <input type="date" id="startDate" class="form-control">
                </div>
                <div class="col">
                    <label for="endDate">End date</label>
                    <input type="date" id="endDate" class="form-control">
                </div>
                <div class="col">
                    <label for="createdBy">Created by</label>
                    <select id="createdBy" class="form-select">
                        <option value="">All teachers</option>
                        <#list creators as creator>
                            <option value="${creator.id}">${creator.username}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </form>

        <ul class="list-group mb-3" id="eventList"></ul>
    </div>
    <script>
        $(document).ready(function () {
            var sortOrder = 'dateDesc';

            function fetchEvents() {
                var query = $('#searchInput').val();
                var categoryId = $('#categoryId').val();
                var startDate = $('#startDate').val();
                var endDate = $('#endDate').val();
                var createdBy = $('#createdBy').val();
                if (query.trim() === "") {
                    $('#eventList').empty();
                    return;
                }
                $.ajax({
                    url: '/ajaxEventSearch',
                    type: 'GET',
                    data: {
                        query: query,
                        categoryId: categoryId,
                        startDate: startDate,
                        endDate: endDate,
                        createdBy: createdBy,
                        sortOrder: sortOrder
                    },
                    success: function (msg) {
                        $('#eventList').empty();
                        const events = JSON.parse(msg);
                        if (events.length > 0) {
                            events.forEach(function (event) {
                                $('#eventList').append('' +
                                    '<li class="list-group-item" data-category-id="' + event.category.id + '">' +
                                    '<a href="/event?id=' + event.id + '" class="link-no-style d-flex align-items-start">' +
                                    '<img src="/images?image_name=' + event.imagePath + '" alt="' + event.title + '" ' +
                                    'class="img-fluid me-3" style="height: 150px; width: 200px;object-fit: scale-down"> ' +
                                    '<div> <h5>' + event.title + '</h5>' +
                                    '<p>' + (event.description.length > 100 ? event.description.substring(0, 100) + '...' : event.description) + '</p>' +
                                    '<small class="text-muted">' + new Date(event.time).toLocaleDateString() +
                                    '</small></div></a></li>');
                            });
                        }
                    },
                    error: function () {
                        $('#eventList').empty();
                        $('#eventList').append('<li class ="list-group-item">Error retrieving events</li>');
                    }
                });
            }

            $('#searchInput').on('keyup', function () {
                fetchEvents();
            });
            $('#filterForm select').on('change', function () {
                fetchEvents();
            });
            $('#filterForm input').on('keydown', function (event) {
                if (event.key==='Enter'){
                    fetchEvents();
                }
            });
            $('.dropdown-item').on('click', function () {
                sortOrder = $(this).data('sort');
                $('#sortOrderDropdown').text($(this).text());
                fetchEvents();
            });
            fetchEvents();
        });
    </script>
</#macro>
<@page/>
