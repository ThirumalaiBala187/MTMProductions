function changeTab(selectedTab) {
    document.querySelectorAll(".cnav").forEach(tab => {
        tab.classList.remove("active");
    });
    selectedTab.classList.add("active");
}


document.querySelectorAll('.courselevel').forEach(card => {
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


function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

document.addEventListener("DOMContentLoaded", function () {
    function updateLevels() {
        const details = getCookie("DETAILS");
		const streak=atob(getCookie("streak"));
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

        for (let i = 1; i <= 7; i++) {
            let levelId = `level${i}`;
            let levelElement = document.getElementById(levelId);
            let button = document.getElementById(`${levelId}-btn`);

            if (levelElement && button) {
                if (i <= levelsCompleted + 1) {
                    levelElement.classList.remove("blurred");
                    button.innerText = i === 1 ? "Continue" : "Review";
                    button.classList.remove("locked");
                    button.removeAttribute("disabled");
                } else {
                    levelElement.classList.add("blurred");
                    button.innerHTML = "🔒 Locked";
                    button.classList.add("locked");
                    button.setAttribute("disabled", "true");
                }
            }
        }


        let new_learn_prog = document.querySelector(".upro");
        if (new_learn_prog) {
            new_learn_prog.innerHTML = `
                <div class="streak cpro">
                    <span class="bd">${streak} Day Streak 🔥</span><br>
                    <span class="bd1"></span>
                </div>
                <div class="xp cpro">
                    <span class="bd">${xp} XP ⚡</span><br>
                </div>
                <div class="achievement cpro">
                    <span class="bd">${levelsCompleted} Completed 🏆</span><br>
                </div>
                <div id="progress">
                    <div id="progressBar" style="width:${completionPercentage}%"></div>
                </div>
            `;
        }
    }

    updateLevels(); 
});
