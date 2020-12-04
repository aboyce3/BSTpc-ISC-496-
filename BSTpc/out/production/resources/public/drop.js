function drop(){
    document.getElementById("dropdown").classList.toggle("show");
}

window.onclick = function(event){
    if(!event.target.matches(".dropdown")){
        var drop = document.getElementsByClassName("dropdown-content");
        var i;
        for(i = 0; i < drop.length; i++){
            var open = drop[i];
            if(open.classList.contains('show')){
                open.classList.remove('show');
            }
        }
    }
}

function showForm(a){
    if(a == 1){
        document.getElementById("regForm").style.display="none";
        document.getElementById("login-button").style.display="inline-block";
    }
    if(a == 2){
        document.getElementById("loginForm").style.display="none";
        document.getElementById("login-button").style.display="inline-block";
    } else {
        document.getElementById("loginForm").style.display="table";
        document.getElementById("login-button").style.display="none";
    }
}

function check() {
    if(document.getElementById('password').value ==
        document.getElementById('confirm_password').value) {
            document.getElementById('register').disabled = false;
            document.getElementById('register').style.backgroundColor = "green"
        } else if(document.getElementById('password').value == '' ||
                    document.getElementById('confirm_password').value == ''){
            document.getElementById('register').disabled = true;
            document.getElementById('register').style.backgroundColor = "red"
        }
}

function redirect(url){
var elements = document.myform.getElementsByTagName("offer")
const Http = new XMLHttpRequest();
const redirect=url + "message="+elements;
Http.open("GET", redirect);
Http.send();
}
