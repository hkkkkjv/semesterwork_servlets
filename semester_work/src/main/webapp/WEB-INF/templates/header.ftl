<#macro header>
    <nav class="navbar">
        <div class="container-fluid">
            <h1 class="site-title">Unevents</h1>
            <ul class="navlist">
                <li class="nav-item"><a class="nav-link" href="/">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="/events">Events</a></li>
                <li class="nav-item"><a class="nav-link" href="/topics">Forum</a></li>
                <li class="nav-item"><a class="nav-link" href="/profile">Profile <#if user??>${user.username}</#if></a></li>
                <li class="nav-item"><a class="nav-link" href="/myEvents">My events</a></li>
            </ul>
        </div>
    </nav>
</#macro>