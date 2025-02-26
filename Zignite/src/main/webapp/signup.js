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
		document.getElementById("eyeToggle").innerHTML="👁️";
    }
    else{
        passwordInput.type = "password";
		document.getElementById("eyeToggle").innerHTML="🔒";
    }
    event.stopPropagation();
}

window.onload = function() {
       document.getElementById('dob').type = 'text';
   };

async function signUp() {
	if(validatePassword() && validateDate()){
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
            window.location.href = "/Zignite/entrance.html"; 
            console.log("Sign up successful");
        } else {
            console.log("Failed to sign up, status:", response.status);
			alert("Username already exists");
        }
    } catch (error) {
        console.error("Error during signup:", error);
    }
	}
}


function validatePassword() {
    let passwordField = document.getElementById("passwordInput");
    let password = passwordField.value;

    let hasLength = password.length >= 8;
    let hasUppercase = /[A-Z]/.test(password);
    let hasNumber = /[0-9]/.test(password);
    let hasSpecial = /[!@#$%^&*]/.test(password);

    if (hasLength && hasUppercase && hasNumber && hasSpecial) {
        console.log("Password is valid");
		return true;
    } else {
        console.log("Invalid password. Please enter again.");
        passwordField.value = "";
        alert("Invalid password! Must have 8+ characters, 1 uppercase, 1 number, 1 special character.");
    }
	return false;
}


function validateDate() {
let dobValue = document.getElementById("dob");
let dob = dobValue.value;
    var dobDate = new Date(dob);
    var today = new Date();

    var age = today.getFullYear() - dobDate.getFullYear();
	
    if (today.getMonth() < dobDate.getMonth() || (today.getMonth() === dobDate.getMonth() && today.getDate() < dobDate.getDate())) {
        age--;
    }
	
	if(age <= 1){
		alert("Please Enter a valid age");
		dob.value="";
		return false;
	}
	else if(age > 1 && age <= 10){
		alert("You must be atleast 10 years old to use this application.");
		dob.value="";
		return false;
	}
	return true;
}

