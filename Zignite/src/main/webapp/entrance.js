const questions = [
    {
        text: "What excites you the most?",
        options: [
            "💻 Building software & backend systems",
            "🤖 Understanding AI & machine learning",
            "💰 Learning how to invest & grow money"
        ]
    },
    {
        text: "Which skill do you want to master first?",
        options: [
            "🖥 Coding & API development",
            "🧠 AI-powered problem-solving",
            "📊 Making smart financial decisions"
        ]
    },
    {
        text: "Do you have coding experience?",
        options: [
            "✅ Yes, I have coding experience",
            "🆕 No, I'm a beginner"
        ]
    },
    {
        text: "Are you comfortable with mathematics?",
        options: [
            "🧮 Yes, I enjoy math",
            "😅 I struggle with math",
            "🤷 I’m okay with basic math"
        ]
    },
    {
        text: "What’s your end goal?",
        options: [
            "🚀 Become a software developer",
            "🏢 Work in AI research & development",
            "📈 Build a successful business"
        ]
    },
    {
        text: "How much time can you invest in learning?",
        options: [
            "⏳ Less than 2 hours per week",
            "⌛ 2-5 hours per week",
            "🔥 More than 5 hours per week"
        ]
    }
];

var selectedOption = null;
var currentQuestion = 0;
var alertRedirect = null;

function loadQuestions(){
    let questionBox = document.getElementById("questionText");
    let optionsContainer = document.getElementById("optionsContainer");
    let continueButton = document.getElementById("continueBtn");

    questionBox.innerHTML = questions[currentQuestion].text;
    optionsContainer.innerHTML = "";

    questions[currentQuestion].options.forEach(optionsText => {
        let option = document.createElement("div");
        option.classList.add("options");
        option.innerHTML = optionsText;
        option.onclick = function(){
             document.querySelectorAll("#optionsContainer .options").forEach(opt => {
                opt.classList.remove("selected");
                opt.style.background = "";
                opt.style.border = "";
            }); 
            option.classList.add("selected");
            option.style.background = "linear-gradient(to right, #1ee9d1, #14B8A6)";
            selectedOption = optionsText;
            continueButton.disabled = false;
            continueButton.style.opacity = "1";
        }
        optionsContainer.appendChild(option);
    });
    continueButton.disabled = true;
    continueButton.style.opacity = "0.5";

    let progress = ((currentQuestion + 1)/ questions.length) * 100;
    document.getElementById("progressBar").style.width = progress + "%";
}

function nextQuestion(){
    if(currentQuestion < questions.length - 1){
        currentQuestion++;
        loadQuestions();
    }
    else{
        showAlert("Success !", "Your personalized learning path is ready !", "signup.html");
    }
}

function showQuestions(){
    var questionsBox = document.getElementById("questionBox");
    document.getElementById("videoBox").style.display = "none";
    questionsBox.style.display="flex";
    questionsBox.style.flexDirection = "column";
    questionsBox.style.alignItems="center";
    questionsBox.style.justifyContent="space-evenly";

    loadQuestions();
}

function showAlert(title, message, redirectUrl){
    document.getElementById("alertTitle").innerText = title;
    document.getElementById("alertMessage").innerText = message;
    document.getElementById("alertOverlay").style.display = "flex";
    alertRedirect = redirectUrl;
}

function closeAlert(){
    document.getElementById("alertOverlay").style.display = "none";
    if(alertRedirect){
        window.location.href = alertRedirect;
    }
}
