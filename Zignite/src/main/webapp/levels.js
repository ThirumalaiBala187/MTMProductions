function changeTab(selectedTab){
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
  
  const details = getCookie("DETAILS");
  console.log(details)
  var courses={};
  var courseCards={};

  function getCookie(name) {
      const value = `; ${document.cookie}`;
      const parts = value.split(`; ${name}=`);
      if (parts.length === 2) {
          return parts.pop().split(';').shift();
      }
      return null;
  }

  if (details) {
      const decodedDetails = atob(details);
      const parsedDetails = JSON.parse(decodedDetails); 
  	parsedDetails.courses.forEach(a=>{
  		console.log(a.level_name);
  		courseCards[a.course_name]=[a.level_name,Math.round((a.levels_completed/7)*100),a.levels_completed,a.xp,a.streak_count];
  	});
  }
  
  for(let course_name in courseCards){
  	console.log(course_name)
  	let course_percent = courseCards[course_name][1]; 
  	let level_name= courseCards[course_name][0];
  	let levelCount=courseCards[course_name][2];
	let xp=courseCards[course_name][3];
	let streak=courseCards[course_name][4];
  	var new_learn_prog = document.getElementsByClassName()[0];
  	new_learn_prog.innerHTML =document.getElementByClassName("upro")[0];
`	              
	                  <div class="streak cpro">
	                      <span class="bd">${streak} Day Streak 🔥</span><br>
	                      <span class="bd1">Personal Best!</span>
	                  </div>
	                  <div class="xp cpro">
	                      <span class="bd">${xp} XP ⚡</span><br>
	                      <span class="bd1">Level 3 Master</span>
	                  </div>
	                  <div class="achievement cpro">
	                      <span class="bd">${levelCount} Completed 🏆</span><br>
	                  </div>
	                  <div class="progressrange">
	                      <div class="fill"></div>
	                      <div class="progress_marker">
	                          <div class="pmarker completed"></div>
	                          <div class="pmarker completed"></div>
	                          <div class="pmarker completed"></div>
	                          <div class="pmarker"></div>
	                          <div class="pmarker"></div>
	                          <div class="pmarker"></div>
	                      </div>
	                `
  }
