/* COPYPASTA TO INC: 

var C_score = [0, 0, 0];
if (localStorage.getItem("C_localscore") === null) {
	console.log("Local storage for C_localscore does not exist");
	localStorage.setItem("C_localscore", JSON.stringify(C_score));
} else {
	console.log("Local storage for C_localscore exists");
	C_score = JSON.parse(localStorage.getItem("C_localscore"));
}
sm_inc(C_score, #); localStorage.setItem("C_localscore", JSON.stringify(C_score)); // SCORE: Games played

*/



function confirmClear() {
	if ( confirm("Press OK to clear local storage.") == true) {
		localStorage.clear();
	}
}

function sm_inc(arr, i) {
	arr[i] = (parseInt(arr[i]) + 1).toString();
	return arr;
}

// Scores for SOLITAIRE
var S_score = [0, 0, 0];
if (localStorage.getItem("S_localscore") === null) {
	console.log("Local storage for S_localscore does not exist");
	localStorage.setItem("S_localscore", JSON.stringify(S_score));
} else {
	console.log("Local storage for S_localscore exists");
	S_score = JSON.parse(localStorage.getItem("S_localscore"));
}

document.getElementById("S_gp").innerHTML = S_score[0];
document.getElementById("S_w").innerHTML = S_score[1];
document.getElementById("S_l").innerHTML = S_score[2];

// Scores for WAR
var W_score = [0, 0, 0];
if (localStorage.getItem("W_localscore") === null) {
	console.log("Local storage for W_localscore does not exist");
	localStorage.setItem("W_localscore", JSON.stringify(W_score));
} else {
	console.log("Local storage for W_localscore exists");
	W_score = JSON.parse(localStorage.getItem("W_localscore"));
}

document.getElementById("W_gp").innerHTML = W_score[0];
document.getElementById("W_w").innerHTML = W_score[1];
document.getElementById("W_l").innerHTML = W_score[2];

// Scores for CRAZY EIGHTS
var C_score = [0, 0, 0];
if (localStorage.getItem("C_localscore") === null) {
	console.log("Local storage for C_localscore does not exist");
	localStorage.setItem("C_localscore", JSON.stringify(C_score));
} else {
	console.log("Local storage for C_localscore exists");
	C_score = JSON.parse(localStorage.getItem("C_localscore"));
}

document.getElementById("C_gp").innerHTML = C_score[0];
document.getElementById("C_w").innerHTML = C_score[1];
document.getElementById("C_l").innerHTML = C_score[2];