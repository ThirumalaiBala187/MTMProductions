



function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}


const details = getCookie("DETAILS");
if (details) {
    const decodedDetails = atob(details);
    const parsedDetails = JSON.parse(decodedDetails); 
    console.log("User Details:", parsedDetails);
}
var session_id=getCookie('JSESSIONID');


var prog_cont=document.getElementsByClassName('card learning-progress')[0];


var courses = {
    "Machine Learning": 78,
    "Data Structures": 85,
    "Web Development": 90,
    "Artificial Intelligence": 92
};


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

////
const api_key="AIzaSyDXchtjhYmS5zHfhVrWAEMlGDwJQqZJkI4";
const endpoint="";
