'use strict';

function login() {
    let working = false;
    $('.login').on('submit', function (e) {
        e.preventDefault();
        if (working) return;
        working = true;
        const $this = $(this),
            $state = $this.find('button > .state');
        $this.addClass('loading');
        $state.html('Authenticating');
        setTimeout(function () {
            $this.addClass('ok');
            $state.html('Welcome back!');
            setTimeout(function () {
                $state.html('Log in');
                $this.removeClass('ok loading');
                working = false;
            }, 4000);
        }, 3000);
    });
}

function signup() {
    let username = document.getElementById("username");
    let password = document.getElementById("password");
    let verify = document.getElementById("verify");
    let firstname = document.getElementById("firstname");
    let lastname = document.getElementById("lastname");
    let email = document.getElementById("email");

    let register = document.getElementById("reg_btn");
    register.disabled = true;

    username.addEventListener("input", isFormCorrect);
    password.addEventListener("input", isFormCorrect);
    verify.addEventListener("input", isFormCorrect);
    firstname.addEventListener("input", isFormCorrect);
    email.addEventListener("input", isFormCorrect);

    function isFormCorrect() {
        let isIt = username.value.match("^[a-zA-Z0-9_-]{3,20}$")
            && password.value.match("^.{3,20}$")
            && verify.value.match("^.{3,20}$")
            && password.value === verify.value
            && firstname.value !== "";

        if (email.value !== "")
            isIt = isIt && email.value.match("^[\\S]+@[\\S]+\\.[\\S]+$");

        register.disabled = !isIt;
    }
}
