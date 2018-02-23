<html>
<head>
    <title>Login Page</title>
    <#include "header.html">
</head>
<body>
<div class="wrapper">
    <form class="login" method="post">
        <p class="title">Log in</p>
        <input type="text" name="username" value="${username}" placeholder="Username" autofocus/>
        <i class="fa fa-user"></i>
        <input type="password" name="password" placeholder="Password"/>
        <i class="fa fa-key"></i>
        <p class="error">${login_error!""}</p>
        <a href="#">Forgot your password?</a>
        <button>
            <i class="spinner"></i>
            <span class="state">Log in</span>
        </button>
    </form>
    <footer><a target="blank" href="#">homectrl.com</a></footer>
</div>
</body>
</html>