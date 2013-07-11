<div class="navbar navbar-fixed-top navbar-inverse">
    <div class="navbar-inner">
        <div class="container">
            <ul class="nav">
                <li><a href="/account"><spring:message code="navigation.account.title" htmlEscape="true"/></a></li>
            </ul>
            <ul class="nav pull-right">
                <li class="navbar-text"><security:authentication property="principal.username"/></li>
                <li><a href="<spring:url value="/logout"/>"><spring:message code="navigation.logout.title" htmlEscape="true"/></a></li>
            </ul>
        </div>
    </div>
</div>