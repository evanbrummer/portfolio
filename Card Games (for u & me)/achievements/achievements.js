/* SAMPLE UPDATE CODE:

var C_ach = ['n', 'n', 'n', 'n', 'n'];
if (localStorage.getItem("C_localach") === null) {
	console.log("Local storage for C_ach does not exist");
	localStorage.setItem("C_localach", JSON.stringify(C_ach));
} else {
	console.log("Local storage for C_ach exists");
	C_ach = JSON.parse(localStorage.getItem("C_localach"));
}
C_ach[#] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: 

*/


//localStorage.clear();
function confirmClear() {
	if ( confirm("Press OK to clear local storage.") == true) {
		localStorage.clear();
	}
}

function toClass(x) {
	if (x == 'y') {
		return "ach";
	}
	else {
		return "unach";
	}
} 

// Load SOLITAIRE achievements from local storage
var S_ach = ['n', 'n', 'n', 'n', 'n'];
if (localStorage.getItem("S_localach") === null) {
	console.log("Local storage for S_ach does not exist");
	localStorage.setItem("S_localach", JSON.stringify(S_ach));
} else {
	console.log("Local storage for S_ach exists");
	S_ach = JSON.parse(localStorage.getItem("S_localach"));
}
//console.log("S_ach[0] is " + S_ach[0]);


document.getElementById("S_playGame").className = toClass(S_ach[0]);
document.getElementById("S_dealCard").className = toClass(S_ach[1]);
document.getElementById("S_flipCard").className = toClass(S_ach[2]);
document.getElementById("S_moveCard").className = toClass(S_ach[3]);
document.getElementById("S_winGame").className = toClass(S_ach[4]);

// Load WAR achievements from local storage
var W_ach = ['n', 'n', 'n', 'n', 'n'];
if (localStorage.getItem("W_localach") === null) {
	console.log("Local storage for W_ach does not exist");
	localStorage.setItem("W_localach", JSON.stringify(W_ach));
} else {
	console.log("Local storage for W_ach exists");
	W_ach = JSON.parse(localStorage.getItem("W_localach"));
}

document.getElementById("W_playGame").className = toClass(W_ach[0]);
document.getElementById("W_drawCard").className = toClass(W_ach[1]);
document.getElementById("W_tieCards").className = toClass(W_ach[2]);
document.getElementById("W_loseGame").className = toClass(W_ach[3]);
document.getElementById("W_winGame").className = toClass(W_ach[4]);

// Load CRAZY EIGHTS achievements from local storage
var C_ach = ['n', 'n', 'n', 'n', 'n'];
if (localStorage.getItem("C_localach") === null) {
	console.log("Local storage for C_ach does not exist");
	localStorage.setItem("C_localach", JSON.stringify(C_ach));
} else {
	console.log("Local storage for C_ach exists");
	C_ach = JSON.parse(localStorage.getItem("C_localach"));
}

document.getElementById("C_playGame").className = toClass(C_ach[0]);
document.getElementById("C_dealHand").className = toClass(C_ach[1]);;
document.getElementById("C_changeSuit").className = toClass(C_ach[2]);
document.getElementById("C_loseGame").className = toClass(C_ach[3]);
document.getElementById("C_winGame").className = toClass(C_ach[4]);;
