var passwordBox = document.getElementById("passwordBox");
var passwordInput = document.getElementById("passwordInput");

passwordBox.addEventListener("click", () => {
    passwordBox.classList.add("active");
    passwordInput.focus();
});

passwordBox.addEventListener("click", (event) => {
    if(!passwordBox.contains(event.target)){
        passwordBox.classList.remove("active");
    }
});

function togglePasswordIcon(event){
    if(passwordInput.type == "password"){
        passwordInput.type = "text";
    }
    else{
        passwordInput.type = "password";
    }
    event.stopPropogation();
}

async function signUp() {
    var uemail = document.getElementById("email").value;
    var upassword = document.getElementById("passwordInput").value;  
    var ufirstName = document.getElementById("firstName").value;
    var ulastName = document.getElementById("lastName").value;
    var udob = document.getElementById("dob").value;
  //  console.log("user input:", uemail, ufirstName, ulastName, udob, upassword);

    var userData = {
        email: uemail,
        password: upassword,
        firstName: ufirstName,
        lastName: ulastName,
        dob: udob
    };
    try {
        const response = await fetch("controller/SignupServlet", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) { 
            const responseJSON = await response.json();
			console.log(responseJSON);  
            window.location.href = "/Zignite/allCourses.html"; 
            console.log("Sign up successful");
        } else {
            console.log("Failed to sign up, status:", response.status);
        }
    } catch (error) {
        console.error("Error during signup:", error);
    }
}
