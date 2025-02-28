function changeTab(selectedTab){
  document.querySelectorAll(".cnav").forEach(tab => {
      tab.classList.remove("active");
  });
  selectedTab.classList.add("active");
}
//Course 



function arrowchange(x){
  arrow = "▼"
  document.getElementById("arr"+x).innerText = `${arrow}`;
}

function normalPosition(y){
  arrow = "▶"
  document.getElementById("arr"+y).innerText = `${arrow}`;
}

function arrowchange1(index) {
    document.getElementById("arr" + index).innerHTML = "&#9660"; // Down arrow
}

function normalPosition1(index) {
    document.getElementById("arr" + index).innerHTML = "&#9654"; // Right arrow
}
function changearrow(x, id) {
	console.log("get element");
	    let div = document.getElementById(id);
	    let arrow = document.getElementById("arr" + x);
	    let arrowClosed = "▶";
	    let arrowOpen = "▼";

	    if (arrow.innerText === arrowClosed) {
	        arrow.innerText = arrowOpen;
			console.log("arrow closed check");

	        filter[x].forEach((course, index) => {
				console.log("filter");
	            let courseDiv = document.createElement("div");
	            courseDiv.classList.add("filterOutput");
				if(course.name=="Introduction To LLM"){
					courseDiv.setAttribute("onclick",`window.location.href='LLMlevels.html'`);
				}
				if(course.name=="Introduction To Python"){
									courseDiv.setAttribute("onclick",`window.location.href='levels.html'`);
								}
	            courseDiv.id = `div${index}`;
	            courseDiv.innerText = course.name || "Unknown Course"; 
	            div.appendChild(courseDiv);
	        });
	    } else {
	        arrow.innerText = arrowClosed;
			for (let i=0; i<div.children.length; i++){
			     div.removeChild(div.lastChild);
			   }
	    }
	}
document.addEventListener("DOMContentLoaded", function() {
	fetch("controller/CourseServlet")
	.then(response => response.json())
	.then(data => {
	  let container = document.getElementById("container");
	let courseCount=1;
	
	  data.forEach(course => {
	      let courseInfo = document.createElement("div");
		  console.log(courseCount)
	      courseInfo.classList.add("cinfo");

	      let image = document.createElement("img");
	      image.style.height = "50%";
	      image.style.width = "100%";
	      image.src = course.url;
	      courseInfo.appendChild(image);

	      let innerDiv1 = document.createElement("div");
	      innerDiv1.classList.add("advance");
	      innerDiv1.innerText = "#"+course.genre;
	      courseInfo.appendChild(innerDiv1);

	      let innerDiv2 = document.createElement("div");
	      innerDiv2.classList.add("content");
	      innerDiv2.style.fontWeight = "bold";
	      innerDiv2.innerText = course.name;
	      courseInfo.appendChild(innerDiv2);

	      let innerDiv3 = document.createElement("div");
	      innerDiv3.classList.add("ctime");

	      let inner1 = document.createElement("div");
	      inner1.classList.add("tinfo");
	      inner1.innerHTML = "⏳&ensp;" + course.duration + " Weeks";
	      innerDiv3.appendChild(inner1);

	      let inner2 = document.createElement("div");
	      inner2.classList.add("tinfo");
	      inner2.innerHTML = "⭐&ensp;" + course.ratings;
	      innerDiv3.appendChild(inner2);

	      let inner3 = document.createElement("div");
	      inner3.classList.add("tinfo");
	      inner3.innerHTML = "👥&ensp;" + course.learners;
	      innerDiv3.appendChild(inner3);
	      courseInfo.appendChild(innerDiv3);
	  
	  let innerDiv4 = document.createElement("div");
	  innerDiv4.classList.add("preview");
	  
	  //let inner4 = document.createElement("div");
	      //inner4.classList.add("money");
	      //inner4.innerText = "🤖";
	      //innerDiv4.appendChild(inner4);

	      let inner5 = document.createElement("div");
	      inner5.classList.add("pre");
	  if(course.name=="Introduction To Python"){
	    inner5.addEventListener('click',()=>{
	      
	      fetch(`controller/courseProgress`, {
	          method: "POST",
	          headers: {
	              "Content-Type": "application/json"
	          },
	        body: JSON.stringify({"courseId":1}) ,
	        credentials:"include"
	      })
	      .then(response => response.json()) 
	      .then(data =>{ console.log(data)
	      window.location.href="levels.html"}) 
	      .catch(error => console.error("Error:", error));
	    })
	  }
	  if(course.name=="Introduction To LLM"){
	    inner5.addEventListener('click',()=>{
	      
	      fetch(`controller/courseProgress`, {
	          method: "POST",
	          headers: {
	              "Content-Type": "application/json"
	          },
	        body: JSON.stringify({"courseId":2}) ,
	        credentials:"include"
	      })
	      .then(response => response.json()) 
	      .then(data =>{ console.log(data)
	      window.location.href="LLMlevels.html"}) 
	      .catch(error => console.error("Error:", error));
	    })
	  }
	  courseCount++;
	      inner5.innerText = "Preview"
	      innerDiv4.appendChild(inner5);
	  courseInfo.appendChild(innerDiv4);
	  
	  container.appendChild(courseInfo);
	});
	})
	.catch(error => console.error("Error fetching data:", error));

	
	

	fetch('controller/CourseServlet') 
	    .then(response => response.json())
	    .then(data => {
	        window.filter = { 1: [], 2: [], 3: [], 4: [] }; 

	        data.forEach(course => {
	            filter[1].push(course);
	            if (course.genre === "Programming") filter[2].push(course);
	            if (course.genre === "Advance") filter[3].push(course);
	            if (course.genre === "Business") filter[4].push(course);
	        });
	    })
	    .catch(error => console.error("Error fetching courses:", error));

		
		
		
		const dotsGrid = document.getElementById('dotsGrid');
		       for (let i = 0; i < 80; i++) {
		           const dot = document.createElement('div');
		           dot.classList.add('dot');
		           dot.style.top = `${Math.random() * 100}%`;
		           dot.style.left = `${Math.random() * 100}%`;
		           dotsGrid.appendChild(dot);
		       }
			   
			   var streak=(atob(getCookie("streak")))
			   console.log("Streak:"+streak);
			   document.getElementsByClassName("burger")[0].innerHTML = `
			   <span class="streak">
			       <img src="fire.png" alt="Fire Icon" class="fire-icon">
			       <span class="streak-number">${streak}</span>
			   </span>	   `;
			       console.log("DOM is fully loaded!");
			   });

			  
				  function getCookie(name) {
				      const value = `; ${document.cookie}`;
				      const parts = value.split(`; ${name}=`);
				      if (parts.length === 2) {
				          return parts.pop().split(';').shift();
				      }
				      return null;
				  }

			      




