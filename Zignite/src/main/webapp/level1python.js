document.addEventListener("DOMContentLoaded", function () {
    const sections = document.querySelectorAll(".section");
    let currentIndex = 0;

    function updateSections() {
        sections.forEach((section, index) => {
            section.style.transform = `translateY(${(index - currentIndex) * 50}vh)`;
        });
    }

    document.querySelectorAll(".next-btn").forEach((button) => {
        button.addEventListener("click", function () {
            if (currentIndex < sections.length - 1) {
                currentIndex++;
                updateSections();
            }
        });
    });

    document.querySelectorAll(".prev-btn").forEach((button) => {
        button.addEventListener("click", function () {
            if (currentIndex > 0) {
                currentIndex--;
                updateSections();
            }
        });
    });
	let finish=(localStorage.getItem("finishedPy")==null)?0:localStorage.getItem("finishedPy");
		completion=document.getElementById("completion");
		               completion.innerHTML=finish+"%";
    updateSections();
});



const API_KEY = 'AIzaSyBSXrz7hdoysQVAfy-Ee8VkIEdWxEsP2XoAIzaSyB3wN4V7OEF6ItvcxiFJYbHHHzaKW3bf_U';
const API_URL = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent';

const chatMessages = document.getElementById('chatMessages');
const userInput = document.getElementById('userInput');
const sendButton = document.getElementById('sendBtn');

async function generateResponse(prompt) {
    const response = await fetch(`${API_URL}?key=${API_KEY}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ prompt })
    });
    const data = await response.json();
    return data.candidates[0].content.parts[0].text;
}



function addMessage(message, isUser) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.classList.add(isUser ? 'userMessage' : 'botMessage');
    const profileImage = document.createElement('img');
    profileImage.classList.add('profileImage');
    profileImage.src = isUser ? 'user.jpg' : 'bot.jpg';
    const messageContent = document.createElement('div');
    messageContent.classList.add('messageContent');
    messageContent.textContent = message;
    messageElement.appendChild(profileImage);
    messageElement.appendChild(messageContent);
    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

async function handleUserInput() {
    const userMessage = userInput.value.trim();
    if (userMessage) {
        addMessage(userMessage, true);
        userInput.value = '';
        sendButton.disabled = true;
        userInput.disabled = true;
        try {
            const botMessage = await generateResponse(userMessage);
            addMessage(botMessage, false);
        }
        catch (error) {
            console.error('Error:', error);
            addMessage('Sorry, I encountered an error. Please try again.', false);
        }
        finally {
            sendButton.disabled = false;
            userInput.disabled = false;
            userInput.focus();
        }
    }
}

sendButton.addEventListener('click', handleUserInput);
userInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        handleUserInput();
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const sectionsWrapper = document.querySelector(".sectionsBox");
    const sections = document.querySelectorAll(".section");
    let currentIndex = 0;

    function updateSections() {
        sectionsWrapper.style.transform = `translateY(-${currentIndex * 165}%)`;
        let progress = ((currentIndex + 1)/ 5) * 100;
        document.getElementById("progressBar").style.width = progress + "%";
    }

    window.next = function () {
        if (currentIndex < sections.length - 1) {
            currentIndex++;
            updateSections();
        }
    };

    window.prev = function () {
        if (currentIndex > 0) {
            currentIndex--;
            updateSections();
        }
    };

    updateSections();
});

function getCookie(name) {
    const value = `; ${document.cookie}`;
	console.log(100);
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

courseCards={};
async function completeLevel(num) {
    try {
		console.log(1);
	     	details=getCookie("DETAILS")
			
			if (details) {
				console.log(2);
			     const decodedDetails = atob(details);
			     const parsedDetails = JSON.parse(decodedDetails); 
			 	parsedDetails.courses.forEach(a=>{
					console.log(3);
			 		console.log(a.course_name);
			 		courseCards[a.course_name]=[a.level_name,Math.round((a.levels_completed/7)*100),a.levels_completed,a.xp];
			 	});
			 }
			 let courseName = "Introduction To Python";
			 levelCount = num;
			 if (levelCount <= 7) {
				
		  window.location.href = `level${num}python.html`;

			 			 			        }
			 if (courseCards[courseName]) {
				console.log(4);
			     let [levelName, completionPercentage, levelsCompleted, xp] = courseCards[courseName];
				 localStorage.setItem("finishedPy", completionPercentage);
				 console.log(5000+": "+levelsCompleted);
				 levelCount = num;
				
									
				 if(levelsCompleted < num-1){
				        const response = await fetch("controller/pythonLevels", {
				            method: "POST",
				            headers: {
				                'Content-Type': 'application/json',
				            },
				            body: JSON.stringify({ level: num, courseId:1}) 
				        });
				        if (!response.ok) {
				            throw new Error(`HTTP error! Status: ${response.status}`);
				        }
						console.log(5);
				        const result = await response.text(); 
				        console.log("Server response:", result);

				    } 
				 }else {
							     console.log("Course not found.");}
			 } catch (error) {
			 				 				console.log(error);
			 				 		        console.error("Error sending level data:", error);
			 				 		    }
			 }
	    



