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

    <form>
        <br/>
        <input name="username"/>
        <br/><br/>
        Username must contain:
        <li> At least one alphanumeric character </li>
        <li> No special symbols </li>
        <br/>
        <button type="submit" formmethod="post">Submit</button>
        <br/>
    </form>

  </div>

</div>
</body>

</html>
