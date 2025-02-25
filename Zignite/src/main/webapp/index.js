const observerOptions = {
    threshold: 0.1
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = "1";
            entry.target.style.transform = "translateY(0)";
        }
    });
}, observerOptions);

document.querySelectorAll('.featureCard').forEach(card => {
    card.style.opacity = "0";
    card.style.transform = "translateY(30px)";
    observer.observe(card);
});


document.querySelectorAll('.btn').forEach(button => {
    button.addEventListener('mouseover', function () {
        this.style.transition = "all 0.3s ease";
    });
});

const logo = document.querySelector('.logo');
logo.addEventListener('mouseover', function () {
    document.querySelector('.logo img').style.animation = "pulse 1s infinite";
});

logo.addEventListener('mouseout', function () {
    document.querySelector('.logo img').style.animation = "pulse 2s infinite";
});

document.getElementById("more").addEventListener("click", function () {
    let icon = document.getElementById("moreButton");
    if (icon.classList.contains("fa-angles-right")) {
        icon.classList.replace("fa-angles-right", "fa-angles-down");
        document.getElementById("advantages1").style.display = "flex";
        document.getElementById("advantages2").style.display = "flex";
        document.getElementById("advantages3").style.display = "flex";
        window.scrollTo({ top: window.innerHeight * 1.5, behavior: "smooth" });
    }
    else {
        icon.classList.replace("fa-angles-down", "fa-angles-right");
        document.getElementById("advantages1").style.display = "none";
        document.getElementById("advantages2").style.display = "none";
        document.getElementById("advantages3").style.display = "none";
        window.scrollTo({ top: window.innerHeight * -(0.8), behavior: "smooth" });
    }
});