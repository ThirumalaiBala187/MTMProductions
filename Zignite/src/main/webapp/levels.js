

document.querySelectorAll('.level-card').forEach(card => {
    card.addEventListener('mousemove', e => {
        const rect = card.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        const xc = rect.width / 2;
        const yc = rect.height / 2;
        const dx = x - xc;
        const dy = y - yc;
        const rotationX = (dy / yc) * 20;
        const rotationY = -(dx / xc) * 20;
        card.style.transform = `rotateX(${rotationX}deg) rotateY(${rotationY}deg)`;
    });

    card.addEventListener('mouseleave', () => {
        card.style.transform = 'rotateX(0deg) rotateY(0deg)';
    });
});

document.addEventListener("DOMContentLoaded", async function () {

	const response = await fetch("controller/pythonLevels", {
	    method: "POST",
	    headers: {
	        'Content-Type': 'application/json',
	    },
	    body: JSON.stringify({ level: 999, courseId:999}) 
	});
	if (!response.ok) {
	    throw new Error(`HTTP error! Status: ${response.status}`);
	}else{
		console.log("good !")
	}
	})
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

document.addEventListener("DOMContentLoaded", async function () {
	setTimeout(() => {
	  console.log(document.cookie);
	}, 500);

	const response = await fetch("controller/pythonLevels", {
	    method: "POST",
	    headers: {
	        'Content-Type': 'application/json',
	    },
	    body: JSON.stringify({ level: 999, courseId:999}) 
	});
	if (!response.ok) {
	    throw new Error(`HTTP error! Status: ${response.status}`);
	}else{
		console.log("good !")
	}
	
    function updateLevels() {
        const details = getCookie("DETAILS");
        const streak = atob(getCookie("streak"));
        if (!details) return;

        const decodedDetails = atob(details);
        const parsedDetails = JSON.parse(decodedDetails);
        let courseCards = {};

        parsedDetails.courses.forEach(a => {
            courseCards[a.course_name] = [
                a.level_name,
                Math.round((a.levels_completed / 7) * 100),
                a.levels_completed,
                a.xp,
            ];
        });

        let courseName = "Introduction To Python";
        if (!(courseName in courseCards)) return;

        let [levelName, completionPercentage, levelsCompleted, xp] = courseCards[courseName];

        document.querySelectorAll(".level-card").forEach((levelElement, index) => {
            let i = index + 1;
            let button = levelElement.querySelector(".level-button");

            if (i <= levelsCompleted + 1) {
                levelElement.classList.remove("locked");
                button.innerText = i === 1 ? "Continue" : "Review";
                button.classList.remove("locked");
                button.href = `http://localhost:8080/Zignite/level${i}python.html`;
            } else {
                levelElement.classList.add("locked");
                button.innerText = "🔒 Locked";
                button.classList.add("locked");
                button.href = "#";
            }
        });

        let progressSection = document.querySelector(".progress-section");
        if (progressSection) {
            progressSection.innerHTML = `
                <div class="progress-grid">
                    <div class="progress-card">
                        <div class="progress-icon">🔥</div>
                        <div class="progress-details">
                            <h3>${streak} Days</h3>
                            <p>Current Streak</p>
                        </div>
                    </div>
                    <div class="progress-card">
                        <div class="progress-icon">⚡</div>
                        <div class="progress-details">
                            <h3>${xp} XP</h3>
                            <p>Total Experience</p>
                        </div>
                    </div>
                    <div class="progress-card">
                        <div class="progress-icon">🏆</div>
                        <div class="progress-details">
                            <h3>${levelsCompleted} Levels</h3>
                            <p>Completed</p>
                        </div>
                    </div>
                </div>
                <div id="progress">
                    <div id="progressBar" style="width:${completionPercentage}%"></div>
                </div>
            `;
        }
    }

    updateLevels();
});
document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.querySelector(".search-input");
    const levelCards = document.querySelectorAll(".level-card");

    searchInput.addEventListener("input", function () {
        const query = searchInput.value.toLowerCase();

        levelCards.forEach(card => {
            const title = card.querySelector("h3").textContent.toLowerCase();
            if (title.includes(query)) {
                card.style.display = "flex"; 
            } else {
                card.style.display = "none"; 
            }
        });
    });
});


