function signIn(){
 //event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('passwordInput').value;

	if (!email || !password) {
	        alert("Please enter both email and password.");
	        return;
	    }
		
    const loginData = {
        email: email,
        password: password
    };

    fetch('controller/LoginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData),
        credentials:"include"
    })
    .then(response => {
		if(!response.ok)
			{
				throw new Error("Invalid response");
			}
			return response.json();
		})
    .then(data => {
        if (data.success) {
            window.location.href = "userHome.html";
        } else {
            alert("Invalid credentials. Please try again.");
      }
    })
	.catch(error => {
	    console.log('Error:', error);
	    alert("An error occurred while logging in. Please try again later.");
	});
};

function togglePasswordIcon(event){
    if(passwordInput.type == "password"){
        passwordInput.type = "text";
    }
    else{
        passwordInput.type = "password";
    }
    event.stopPropagation();
}