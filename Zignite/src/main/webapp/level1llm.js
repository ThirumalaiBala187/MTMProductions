function createBubbles() {
    const count = 15;
    for (let i = 0; i < count; i++) {
        const bubble = document.createElement('div');
        bubble.className = 'bubble';

        const size = Math.random() * 100 + 40;
        bubble.style.width = `${size}px`;
        bubble.style.height = `${size}px`;

        bubble.style.left = `${Math.random() * 100}vw`;
        bubble.style.top = `${Math.random() * 100}vh`;

        bubble.style.animationDelay = `${Math.random() * 5}s`;
        bubble.style.animationDuration = `${5 + Math.random() * 10}s`;

        document.body.appendChild(bubble);
    }
}

window.onload = () => {
    createBubbles();
};

const chatMessages = document.getElementById('chatMessages');
const userInput = document.getElementById('userInput');

async function generateResponse(prompt) {
    try {
        const response = await fetch('http://localhost:5000/generate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ text: prompt }),
        });
        const data = await response.json();

        if (response.ok) {
            return data['Generated Output'];
        }
    } catch (error) {
       return error.message;
    }
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
            userInput.focus();
        }
    }
}
document.addEventListener("DOMContentLoaded", function () {
    const sections = document.querySelectorAll(".learning-section");
    let currentIndex = 0;

    function updateSections() {
        document.getElementById("allSections").style.transform = `translateY(-${currentIndex * 87}vh)`;
        document.getElementById("progressBar").style.width=currentIndex*20 + "%";
    }
    

    document.querySelectorAll(".continue").forEach((button) => {
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
	let finish=(localStorage.getItem("finishedLLM")==null)?0:localStorage.getItem("finishedLLm");
		 completion=document.getElementById("completion");
		               completion.innerHTML=finish+"%";
     updateSections();
});

 
document.addEventListener('DOMContentLoaded', function() {
    const chatButton = document.getElementById('chatButton');
    const chatContainer = document.getElementById('chatContainer');
    const closeButton = document.getElementById('closeButton');
    const sendButton = document.getElementById('sendButton');
    const messageInput = document.getElementById('messageInput');
    const chatBody = document.getElementById('chatBody');

    chatButton.addEventListener('click', function() {
        chatContainer.classList.add('active');
    });

    closeButton.addEventListener('click', function() {
        chatContainer.classList.remove('active');
    });

    async function sendMessage() {
        const message = messageInput.value.trim();
        if (message) {
            addMessage(message, 'user');
            messageInput.value = '';
            showTypingIndicator();
            
            try {
                const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyD_FLM3dwEA-R27v-V1Z2BEyBzP4-lQ3mM`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        contents: [
                            {
                                parts: [{ text: message }]
                            }
                        ]
                    })
                });

                const data = await response.json();
                removeTypingIndicator();

                if (data.candidates && data.candidates.length > 0) {
                    const reply = data.candidates[0].content.parts[0].text;
                    addMessage(reply, 'bot');
                } else {
                    addMessage('No response received from the AI.', 'bot');
                }
            } catch (error) {
                removeTypingIndicator();
                addMessage('Sorry, something went wrong. Please try again.', 'bot');
                console.error('Error fetching response:', error);
            }
        }
    }

    sendButton.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    function addMessage(text, sender) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', sender + '-message');
        messageElement.textContent = text;
        chatBody.appendChild(messageElement);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function showTypingIndicator() {
        const typingIndicator = document.createElement('div');
        typingIndicator.classList.add('typing-indicator');
        typingIndicator.id = 'typingIndicator';

        for (let i = 0; i < 3; i++) {
            const dot = document.createElement('div');
            dot.classList.add('typing-dot');
            typingIndicator.appendChild(dot);
        }

        chatBody.appendChild(typingIndicator);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function removeTypingIndicator() {
        const typingIndicator = document.getElementById('typingIndicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }
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
			 levelCount = num;
			 			        if (levelCount <= 7) {
			 			            window.location.href = `level${num}LLM.html`;
			 			        }
			 let courseName = "Introduction To LLM";
			 if (courseCards[courseName]) {
				console.log(4);
			     let [levelName, completionPercentage, levelsCompleted, xp] = courseCards[courseName];
				 console.log(5000+": "+levelsCompleted);
				 localStorage.setItem("finishedLLM", completionPercentage);

			//	completion=document.getElementById("completion");
			//	completion.innerText=completionPercentage+"%";
				 if(levelsCompleted < num-1){
					console.log("nammadjaan");
				        const response = await fetch("controller/pythonLevels", {
				            method: "POST",
				            headers: {
				                'Content-Type': 'application/json',
				            },
				            body: JSON.stringify({ level: num, courseId:2}) 
				        });
				        if (!response.ok) {
				            throw new Error(`HTTP error! Status: ${response.status}`);
				        }else{
							console.log("good !")
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