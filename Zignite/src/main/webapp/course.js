function changeTab(selectedTab){
  document.querySelectorAll(".cnav").forEach(tab => {
      tab.classList.remove("active");
  });
  selectedTab.classList.add("active");
}
//Course 
fetch("controller/CourseServlet")
.then(response => response.json())
.then(data => {
let container = document.getElementById("container");
let courseCount=1;
  data.forEach(course => {
      let courseInfo = document.createElement("div");
      courseInfo.classList.add("cinfo");

      let image = document.createElement("img");
      image.style.height = "50%";
      image.style.width = "100%";
      image.src = course.url;
      courseInfo.appendChild(image);

      let innerDiv1 = document.createElement("div");
      innerDiv1.classList.add("advance");
      innerDiv1.innerText = course.genre;
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



// function arrowchange(x){
//   arrow = "▼"
//   document.getElementById("arr"+x).innerText = `${arrow}`;
// }

// function normalPosition(y){
//   arrow = "▶"
//   document.getElementById("arr"+y).innerText = `${arrow}`;
// }

let filter = {
  1 : ["Introduction to Python","Introduction to LLM"],
  2 : ["Introduction to LLM"],
  3 : ["Introduction to Python"],
  4 : ["No Course Available here"]
}

function changearrow(x,id){
  div = document.getElementById(id);
  let namearr = [];
  let arrow1 = "▶"
  let arrow2 = "▼"
  if (document.getElementById("arr"+x).innerText == `${arrow1}`){
    document.getElementById("arr"+x).innerText = `${arrow2}`
    for (let i=0; i<filter[x].length; i++){
        namearr[i] = "div"+i;
    }
    for (let i=0; i<filter[x].length; i++){
      namearr[i] = document.createElement("div");
      namearr[i].setAttribute("class","filterOutput");
      namearr[i].setAttribute("id", `${namearr[i]}`);
      namearr[i].innerText = filter[x][i]+"";
      div.appendChild(namearr[i]);
  }
  }
  else{
    document.getElementById("arr"+x).innerText = `${arrow1}`;
    for (let i=0; i<div.children.length; i++){
      div.removeChild(div.lastChild);
    }
  }
}

