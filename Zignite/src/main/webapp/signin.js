function signIn(){
 event.preventDefault();

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

    fetch('LoginServlet', {
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
            window.location.href = "https://muthueshwaran-8389.zcodeusers.in/Zignite_Learnings/userHome/userHome.html#";
        } else {
            alert("Invalid credentials. Please try again.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
};