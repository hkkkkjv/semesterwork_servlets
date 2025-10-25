<#include "base.ftl">
<#macro title>${event.title}</#macro>
<#macro content>
    <div class="container mt-5">
        <#if event??>
            <div class="card mb-4">
                <div class="card-body d-flex">
                    <img src="/images?image_name=${event.imagePath}" class="card-img-top me-3" alt="${event.title}"
                         style="width: 300px;height: 250px;object-fit: contain">
                    <div>
                        <p class="card-text">${event.description}</p>
                        <p><strong>Date and Time:</strong>${event.time?string("yyyy-MM-dd HH:mm:ss")}</p>
                        <p><strong>Location:</strong>${event.location}</p>
                        <p>
                            <strong>Available Seats:</strong> ${event.seatCount-activeRegistrationCount}
                            /${event.seatCount}
                        </p>
                        <p><strong>Category:</strong> ${event.category.name}</p>
                        <p><strong>Created by:</strong>
                            <a href="/profile?id=${event.createdBy.id}"
                               class="link-no-style"> ${event.createdBy.username}</a>
                        </p>
                    </div>
                </div>
            </div>
            <#if user.role=="TEACHER" &&user.username==event.createdBy.username>
                <div class="accordion w-75 mx-auto mb-3" id="activeRegistrationsAccordion">
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingOne">
                            <button class="accordion-button custom-accordion-button" type="button"
                                    data-bs-toggle="collapse"
                                    data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                Registrations
                            </button>
                        </h2>
                        <div id="collapseOne" class="accordion-collapse collapse" aria-labelledby="headingOne"
                             data-bs-parent="#activeRegistrationsAccordion">
                            <div class="accordion-body">
                                <ul class="list-group">
                                    <#list activeRegistrations as activeRegistration>
                                        <li class="list-group-item d-flex justify-content-between align-items-center mb-1">
                                            <div>
                                                <strong><a href="/profile?id=${activeRegistration.user.id}">${activeRegistration.user.username}</a></strong>: ${activeRegistration.comment}
                                                <br>
                                                <small class="text-muted">${activeRegistration.registrationTime?string("dd.MM.yyyy HH:mm")}</small>
                                            </div>
                                            <div>
                                                <button onclick="cancelRegistration(${activeRegistration.id})"
                                                        class="btn btn-danger btn-sm">Cancel Registration
                                                </button>
                                            </div>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </#if>
            <#if registration??>
                <#if registration.status == "CANCELLED">
                    <p class="alert alert-warning mx-auto w-75">Registration canceled.</p>
                <#else >
                    <#if eventHasPassed>
                        <p class="alert alert-warning mx-auto w-75">The event has taken place.</p>
                        <p class="alert alert-success mx-auto w-75">You have attended this event.</p>
                    <#else >
                        <p class="alert alert-success  mx-auto w-75">You are registered!</p>
                        <div class="text-center">
                            <button onclick="cancelRegistration(${registration.id})" type="submit"
                                    class="btn btn-deep-purple mx-auto">
                                Cancel Registration
                            </button>
                        </div>
                    </#if>
                </#if>
            <#else>
                <#if eventHasPassed>
                    <p class="alert alert-warning mx-auto w-75">The event has taken place.</p>
                <#else >
                    <#if (activeRegistrationCount >= event.seatCount)>
                        <p class="alert alert-danger">Registration is closed. All seats are taken.</p>
                    <#else>
                        <#if user.role =="STUDENT">
                            <h3>Register for this Event</h3>
                            <form id="register-form" class="mb-4 mx-auto w-50">
                                <input type="hidden" name="eventId" value="${event.id}">
                                <div class="form-group">
                            <textarea name="commentText" class="form-control" rows="3" required
                                      placeholder="Write your comment here..." maxlength="250"
                                      oninput="updateCommentCount(this, 'registerCommentCount')"></textarea>
                                    <div class="d-flex justify-content-between">
                                        <small id="registerCommentCount"
                                               class="form-text text-muted ms-auto mb-3">0/250</small>
                                    </div>
                                </div>
                                <div class="text-center">
                                    <button type="submit" class="btn btn-purple mx-auto">Register</button>
                                </div>
                            </form>
                        </#if>
                    </#if>
                </#if>
            </#if>
            <form id="comment-form" class="w-50 mx-auto mb-4">
                <input type="hidden" name="eventId" value="${event.id}">
                <div class="form-group">
                    <textarea name="commentText" class="form-control" rows="3" required
                              placeholder="Write your comment here..." maxlength="250"
                              oninput="updateCommentCount(this, 'commentCount')"></textarea>
                    <div class="d-flex justify-content-between">
                        <small id="commentCount" class="form-text text-muted ms-auto mb-3">0/250</small>
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-purple mx-auto">Submit Comment</button>
                </div>
            </form>
            <#if comments?? && (comments?size>0)>
                <ul class="list-group mx-auto w-75 mb-5">
                    <#list comments?reverse as comment>
                        <li class="list-group-item d-flex justify-content-between align-items-center mb-1"
                            id="comment-${comment.id}">
                            <div>
                                <strong><a href="/profile?id=${comment.user.id}"
                                           class="link-no-style">${comment.user.username}</a>:</strong> ${comment.text}
                                <br>
                                <small class="text-muted">${comment.date?string("dd.MM.yyyy HH:mm")}</small>
                            </div>
                            <#if user.id == comment.user.id>
                                <div class="ms-auto">
                                    <button class="btn btn-outline-primary btn-sm"
                                            onclick="confirmDeleteComment(${comment.id})">
                                        Delete
                                    </button>
                                </div>
                            </#if>
                        </li>
                    </#list>
                </ul>
            <#else>
                <p class="alert alert-info text-center w-75 mx-auto">No comments yet.</p>

            </#if>

        <#else>
            <h2>Event not found</h2>
            <a href="/events" class="btn btn-primary">Back to Events</a>
        </#if>
        <div class="modal fade" id="deleteCommentModal" tabindex="-1" role="dialog"
             aria-labelledby="deleteCommentModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="deleteCommentModalLabel">Confirm Deletion</h5>
                        <button type="button" class="btn-close" id="closeModal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this comment?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" id="confirmCancelButton">Cancel</button>
                        <button type="button" class="btn btn-danger" id="confirmDeleteButton">Delete</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        var commentIdToDelete;

        function confirmDeleteComment(commentId) {
            commentIdToDelete = commentId;
            $('#deleteCommentModal').modal('show')
        }

        $('#confirmDeleteButton').click(function () {
            deleteComment(commentIdToDelete);
            $('#deleteCommentModal').modal('hide');
        });
        $('#confirmCancelButton , #closeModal').click(function () {
            $('#deleteCommentModal').modal('hide');
        });

        function deleteComment(commentId) {
            $.ajax({
                url: '/event',
                type: 'POST',
                data: {commentId: commentId, action: 'deleteComment'},
                success: function (response) {
                    $('#comment-' + commentId).remove();
                },
                error: function (xhr, status, error) {
                    console.error('Error liking comment', error);
                    alert('Failed to delete comment. Try again.');
                }
            });
        }

        function cancelRegistration(registrationId) {
            $.ajax({
                url: '/event',
                type: 'POST',
                data: {registrationId: registrationId, action: 'cancelRegistration'},
                success: function (response) {
                    location.reload();
                },
                error: function (xhr, status, error) {
                    console.error('Error cancelling registration', error);
                }
            });
        }

        $(document).ready(function () {
            $('#register-form').submit(function (event) {
                event.preventDefault();
                $.ajax({
                    url: '/event',
                    type: 'POST',
                    data: $(this).serialize() + '&action=register',
                    success: function (response) {
                        location.reload();
                    },
                    error: function (xhr, status, error) {
                        console.error('Error registering for event', error);
                    }
                });
            });

            $('#comment-form').submit(function (event) {
                event.preventDefault();
                $.ajax({
                    url: '/event',
                    type: 'POST',
                    data: $(this).serialize() + '&action=addComment',
                    success: function (response) {
                        location.reload();
                    },
                    error: function (xhr, status, error) {
                        console.error('Error adding comment', error);
                    }
                });
            });
        });

        function updateCommentCount(textarea, countId) {
            var count = textarea.value.length;
            document.getElementById(countId).innerText = count + '/250'
        }
    </script>
</#macro>
<@page/>