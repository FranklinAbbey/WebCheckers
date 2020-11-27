<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <#if currentUser??>
        <p>
            <#if playerList??>
                <#if playersSignedIn??>
                    Currently signed in Players: ${playersSignedIn}
                </#if>
                <ul>
                <#list playerList as current>
                    <#if current != currentUser>
                        <li><a href="/game?opponentPlayer=${current.name}">${current.name}</a></br></li>
                    </#if>
                </#list>
                </ul>
            </#if>

            </br>
            <li><a href="/game?opponentPlayer=AI">Play against an AI opponent</a></br></li>

    <#else>
        <#if playersSignedIn??>
            Currently signed in Players: ${playersSignedIn}
        </#if>
    </#if>

  </div>

</div>
</body>
</html>