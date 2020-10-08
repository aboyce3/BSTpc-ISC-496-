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