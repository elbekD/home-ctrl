<html style="font-size: 10px">
<head>
    <title>Sign Up</title>
    <#include "header.html">
</head>
<body onload="signup()">
<form method="post">
    <table class="table">
        <thead>
        <tr>
            <td align="center" colspan="2">Signup</td>
        </tr>
        </thead>

        <tr>
            <td>
                Login<span style="color: red;">* </span>
            </td>
            <td>
                <input id="username" placeholder="login" type="text" autocomplete="off" name="login" value="${username}">
            </td>
            <td class="error">
            ${username_error!""}
            </td>
        </tr>

        <tr>
            <td>
                Password<span style="color: red;">* </span>
            </td>
            <td>
                <input id="password" placeholder="password" type="password" autocomplete="off" name="password">
            </td>
        </tr>

        <tr>
            <td>
                Verify password<span style="color: red;">* </span>
            </td>
            <td>
                <input id="verify" placeholder="verify" type="password" autocomplete="off" name="verify">
            </td>
        </tr>

        <tr>
            <td>
                Firstname<span style="color: red;">* </span>
            </td>
            <td>
                <input id="firstname" placeholder="firstname" type="text" autocomplete="off" name="firstname" value="${firstname}">
            </td>
        </tr>

        <tr>
            <td>
                Lastname
            </td>
            <td>
                <input id="lastname" placeholder="lastname" type="text" autocomplete="off" name="lastname" value="${lastname}">
            </td>
        </tr>

        <tr>
            <td>
                Email
            </td>
            <td>
                <input id="email" placeholder="email" type="email" autocomplete="on" name="email" value="${email}">
            </td>
            <td class="error">
            ${email_error!""}
            </td>
        </tr>
        <tfoot>
        <tr>
            <td align="right" colspan="2">
                <input id="reg_btn" type="submit" value="Register">
            </td>
        </tr>
        </tfoot>
    </table>
</form>
<script src="js/script.js"></script>
</body>
</html>