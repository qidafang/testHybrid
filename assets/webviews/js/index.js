//html按钮点击触发
function theBtnOnClicked(){
	//调用java方法
	javaObject.javaDoIt("427studio");
}

//要用来被java程序调用的js方法
function jsDoIt(str){
	document.getElementById('theBtn').innerText += str;
}