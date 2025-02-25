document.addEventListener("DOMContentLoaded", () => {
    const options = document.querySelectorAll(".option");
    document.getElementById("questionsContainer").style.display = "none";

    options.forEach((option) => {
        option.addEventListener("mouseover", createSparkles);
        option.addEventListener("click", handleOptionClick);
    });
});

function createSparkles(e) {
    const sparklesContainer = e.currentTarget;

    for (let i = 0; i < 8; i++) {
        const sparkle = document.createElement("div");
        sparkle.className = "sparkle";
        const size = Math.random() * 35 + 8;
        const tx = (Math.random() - 0.5) * 150;
        const ty = (Math.random() - 0.5) * 150;

        sparkle.style.cssText = `
              width: ${size}px;
              height: ${size}px;
              background: ${getRandomColor()};
              left: ${e.offsetX}px;
              top: ${e.offsetY}px;
              --tx: ${tx}px;
              --ty: ${ty}px;
              box-shadow: 0 0 ${size / 2}px ${size / 4}px rgba(255,255,255,0.3);
          `;

        sparklesContainer.appendChild(sparkle);
        setTimeout(() => sparkle.remove(), 1000);
    }
}

function getRandomColor() {
    const colors = ["#06b6d4", "#0891b2", "#7dd3fc", "#93c5fd", "#bae6fd"];
    return colors[Math.floor(Math.random() * colors.length)];
}

const questions = [
    {
        text: "What excites you the most?",
        icon:["💻","🤖","💰"],
        options: [
            "Building software & backend systems",
            "Understanding AI & machine learning",
            "Learning how to invest & grow money"
        ]
    },
    {
        text: "Which skill do you want to master first?",
        icon:["🖥","🧠","📊"],
        options: [
            "Coding & API development",
            "AI-powered problem-solving",
            "Making smart financial decisions"
        ]
    },
    {
        text: "Do you have coding experience?",
        icon:["🧑🏻‍💻","💻","🤷"],
        options: [
            "Yes, I am an expert in coding.",
            "I know some basics in coding.",
            "No, I'm a beginner"
        ]
    },
    {
        text: "Are you comfortable with mathematics?",
        icon:["😅","🧮","🤷"],
        options: [
            "Yes, I enjoy math",
            "I struggle with math",
            "I’m okay with basic math"
        ]
    },
    {
        text: "What’s your end goal?",
        icon:["🧑🏻‍💻","🏢","📈"],
        options: [
            "Become a software developer",
            "Work in AI research & development",
            "Build a successful business"
        ]
    },
    {
        text: "How much time can you invest in learning?",
        icon:["⏳","⌛","⏳"],
        options: [
            "Less than 2 hours per week",
            "2-5 hours per week",
            "More than 5 hours per week"
        ]
    }
];

var selectedOption = null;
var currentQuestion = 0;
var alertRedirect = null;
let continueButton = document.getElementById("continueQuestions");

function handleOptionClick(event) {
    continueButton.disabled = false;
    continueButton.style.cursor = "pointer";
    continueButton.style.opacity = "1";

    if (selectedOption) {
        selectedOption.classList.remove("selected");
    }

    selectedOption = event.currentTarget;
    selectedOption.classList.add("selected");

    const text = selectedOption.querySelector("span");
    if (text) {
        text.style.animation = "none";
        text.offsetHeight;
        setTimeout(() => {
            text.style.animation = "rotateText 0.5s ease forwards";
        }, 10);
    }
}

function loadQuestions() {
    let questionBox = document.getElementById("questionText");
    let optionsButton = document.querySelectorAll(".option1, .option2, .option3");
    let optionsIcon = document.querySelectorAll("#icon1, #icon2, #icon3");

    questionBox.innerHTML = questions[currentQuestion].text;

    optionsButton.forEach((option, index) => {
        let span = option.querySelector("span");
        if (span) {
            span.textContent = questions[currentQuestion].options[index];
        }
        option.classList.remove("selected");
        option.removeEventListener("click", handleOptionClick);
        option.addEventListener("click", handleOptionClick);
    });

    optionsIcon.forEach((icon, index) => {
        icon.innerHTML =  questions[currentQuestion].icon[index];
    });



    continueButton.disabled = true;
    continueButton.style.cursor = "default";
    continueButton.style.opacity = "0.5";

    let progress = ((currentQuestion + 1) / questions.length) * 100;
    document.getElementById("progress").style.width = progress + "%";

    selectedOption = null;
}

function nextQuestion() {
    if (currentQuestion < questions.length - 1) {
        currentQuestion++;
        loadQuestions();
    } else {
        showAlert("Success!", "Your personalized learning path is ready!", "allCourses.html");
    }
}

function showQuestions() {
    var botContainer = document.getElementById("botContainer");
    var questionsContainer = document.getElementById("questionsContainer");
    botContainer.style.display = "none";
    questionsContainer.style.display = "flex";
    loadQuestions();
}

function showAlert(title, message, redirectUrl) {
    document.getElementById("alertTitle").innerText = title;
    document.getElementById("alertMessage").innerText = message;
    document.getElementById("alertOverlay").style.display = "flex";
    alertRedirect = redirectUrl;
}

function closeAlert() {
    document.getElementById("alertOverlay").style.display = "none";
    if (alertRedirect) {
        window.location.href = alertRedirect;
    }
}
