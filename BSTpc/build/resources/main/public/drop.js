function drop(){
var drop = document.getElementsByClassName("drop-btn")
var i;

for(i = 0; i < drop.length; i++){
    drop[i].addEventListener("click", function(){
        this.classList.toggle("active");
        var dropdownContent = this.nextElementSibling;
        if(dropdownContent.style.display === "block"){
            dropdownContent.style.display = "none";
        } else {
            dropdownContent.style.display = "block";
            }
        });
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