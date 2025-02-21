

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
		courses[a.course_name]=Math.round((a.levels_completed/7)*100);
	});
	parsedDetails.courses.forEach(a=>{
		console.log(a.level_name);
		courseCards[a.course_name]=[a.level_name,Math.round((a.levels_completed/7)*100),a.levels_completed];
	});
}
var session_id=getCookie('JSESSIONID');


var prog_cont=document.getElementsByClassName('card learning-progress')[0];


//var courses = {
 //   "Machine Learning": 78,
 //   "Data Structures": 85,
 //   "Web Development": 90,
 //   "Artificial Intelligence": 92
//};


for (let course_name in courses) {
    let course_percent = courses[course_name]; 
    var new_learn_prog = document.createElement("div");
    new_learn_prog.innerHTML = `
        <div class="progress-container">
            <div class="progress-header">
                <span class="progress-label">${course_name}</span>
                <span class="progress-value">${course_percent}%</span>
            </div>
            <div class="progress-bar">
                <div class="progress-fill" style="width:${course_percent}%"></div>
            </div>
        </div>
    `;
    prog_cont.appendChild(new_learn_prog);
}

var course_grid=document.getElementsByClassName('courses-grid')[0];
var courses_ongoing=document.getElementsByClassName('stat-value')[0];
var certificates=document.getElementsByClassName('stat-value')[1];
let cert_count=0;
let course_count=0;

for(let course_name in courseCards){
	console.log(course_name)
	let course_percent = courseCards[course_name][1]; 
	let level_name= courseCards[course_name][0];
	let levelCount=courseCards[course_name][2];
	var new_learn_prog = document.createElement("div");
	new_learn_prog.classList.add("course-card");
	new_learn_prog.innerHTML =`
	                    <div class="course-icon icon-1">🎯</div>
	                    <div class="course-content">
	                        <h3 class="course-title">${course_name}: ${level_name}</h3>
	                        <div class="course-progress">
								<div class="course-progress-fill" style="width:${course_percent}%;"></div>
					        </div>
	                    <div class="course-meta">
	                            <span class="modules-left">${levelCount} modules left</span>
	                            <button class="continue-btn" onclick="window.location.href='http://localhost:8080/Zignite/levels.html'">Continue</button>
	                        </div>
	                    </div>`;
						if(course_percent==100){
							cert_count++;
						}
						else{
							course_count++;	
						}
	course_grid.appendChild(new_learn_prog);
}

courses_ongoing.innerText=course_count;
certificates.innerText=cert_count;

var username=(atob(getCookie("name")))
console.log(username)
document.getElementsByClassName("burger")[0].innerHTML = `
   <svg
     viewBox="0 0 24 24"
     fill="white"
     height="20"
     width="20"
     xmlns="http://www.w3.org/2000/svg"
   >
     <path
       d="M12 2c2.757 0 5 2.243 5 5.001 0 2.756-2.243 5-5 5s-5-2.244-5-5c0-2.758 2.243-5.001 5-5.001zm0-2c-3.866 0-7 3.134-7 7.001 0 3.865 3.134 7 7 7s7-3.135 7-7c0-3.867-3.134-7.001-7-7.001zm6.369 13.353c-.497.498-1.057.931-1.658 1.302 2.872 1.874 4.378 5.083 4.972 7.346h-19.387c.572-2.29 2.058-5.503 4.973-7.358-.603-.374-1.162-.811-1.658-1.312-4.258 3.072-5.611 8.506-5.611 10.669h24c0-2.142-1.44-7.557-5.631-10.647z"
     ></path>
   </svg>
   <span> ${username.slice(0,1).toLocaleUpperCase()+username.slice(1,3) || "Guest"}</span>
`;


function logout() {
       fetch('controller/LogoutServlet') 
           .then(response => response.text()) 
           .then(data => {
               alert(data); 
               window.location.href = '/Zignite/index.html'; 
           })
           .catch(error => {
               console.error('Error logging out:', error);
           });
   }
   
   document.getElementsByClassName("username")[0].innerText=username;
////
const api_key="AIzaSyDXchtjhYmS5zHfhVrWAEMlGDwJQqZJkI4";
const endpoint="";
