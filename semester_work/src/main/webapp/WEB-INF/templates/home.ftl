<#include "base.ftl">
<#macro title>${greeting}</#macro>
<#macro content>
    <div class="container mt-5">
        <h2 class="text-center">Upcoming Events</h2>
        <section id="event-slider" class="mb-4">
            <div id="carouselEvents" class="carousel slide" data-bs-ride="carousel">
                <div class="carousel-inner">
                    <#list upcomingEvents as event>
                        <div class="carousel-item <#if event_index==0>active</#if>">
                            <a href="event?id=${event.id}" class="d-block">
                                <img src="/images?image_name=${event.imagePath}" class="d-block w-100"
                                     alt="${event.title}">
                                <div class="carousel-caption d-none d-md-block">
                                    <h5 class="card-title">${event.title}</h5>
                                </div>
                            </a>
                        </div>
                    </#list>
                </div>
                <div class="carousel-indicators">
                    <#list upcomingEvents as event>
                        <button type="button" data-bs-target="#carouselEvents" data-bs-slide-to="${event_index}"
                                class="<#if event_index==0>active</#if>"
                                aria-current="<#if event_index==0>true</#if>"
                                aria-label="Slide ${event_index+1}"></button>
                    </#list>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#carouselEvents"
                        data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carouselEvents"
                        data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
        </section>
        <#if newTopics?? &&(newTopics?size>0)>
            <h2 class="text-center">New topics</h2>
            <section id="topic-slider" class="mb-4">
                <div id="carouselTopics" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <#list newTopics as topic>
                            <a href="topic?topicId=${topic.id}" class="d-block">
                                <div class="carousel-item <#if topic_index==0>active</#if>">
                                    <div class="carousel-caption d-none d-md-block">
                                        <h5 class="card-title">${topic.title}</h5>
                                        <p>${topic.description}</p>
                                        <small class="text-muted">Last update
                                            at ${topic.updatedAt?string("dd.MM.yyyy HH:mm")}</small>
                                    </div>
                                </div>
                            </a>
                        </#list>
                    </div>
                    <div class="carousel-indicators">
                        <#list newTopics as topic>
                            <button type="button" data-bs-target="#carouselTopics" data-bs-slide-to="${topic_index}"
                                    class="<#if topic_index==0>active</#if>"
                                    aria-current="<#if topic_index==0>true</#if>"
                                    aria-label="${topic.title}"></button>
                        </#list>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselTopics"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselTopics"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </section>
        </#if>
    </div>
</#macro>
<@page/>
