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

    // Initialize positions
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

var levelCount = localStorage.getItem("levelCount") ? parseInt(localStorage.getItem("levelCount")) : 1;

function completeLevel() {
    levelCount += 1;
	if(levelCount <= 7){
	localStorage.setItem("levelCount", levelCount);
	window.location.href = `level${levelCount}python.html`;		
	}
}
