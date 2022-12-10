function sm_inc(arr, i) { // Evan's string incrementer
	arr[i] = (parseInt(arr[i]) + 1).toString();
	return arr;
}

var player1;
var player2;

class Board
{
    constructor()
	{
        this.cardsInMiddle = [];
        this.players = [];
    }
	
    start(playerOneName, playerTwoName)
	{
		var W_ach = ['n', 'n', 'n', 'n', 'n'];
		if (localStorage.getItem("W_localach") === null) {
			console.log("Local storage for W_ach does not exist");
			localStorage.setItem("W_localach", JSON.stringify(W_ach));
		} else {
			console.log("Local storage for W_ach exists");
			W_ach = JSON.parse(localStorage.getItem("W_localach"));
		}
		W_ach[0] = 'y'; localStorage.setItem("W_localach", JSON.stringify(W_ach)); // ACHIEVEMENT: Play a game
		
		var W_score = [0, 0, 0];
		if (localStorage.getItem("W_localscore") === null) {
			console.log("Local storage for W_localscore does not exist");
			localStorage.setItem("W_localscore", JSON.stringify(W_score));
		} else {
			console.log("Local storage for W_localscore exists");
			W_score = JSON.parse(localStorage.getItem("W_localscore"));
		}
		sm_inc(W_score, 0); localStorage.setItem("W_localscore", JSON.stringify(W_score)); // SCORE: Games played		
		
        this.players.push(new Player(playerOneName, 25));
        this.players.push(new Player(playerTwoName, 25));
        let d = new Deck();
        d.createDeck();
        d.shuffleDeck();    
        this.players[0].playerCards = d.cards.slice(0, 26);
        this.players[1].playerCards = d.cards.slice(26, 52);
		player1 = this.players[0]
		player2 = this.players[1]
    }
	
	cardUI(playerID, suit, rank)
	{
		var img = document.createElement("img");
		img.src = "../cards/" + suit + rank + ".png";
		img.width = "250";
		img.height = "325";
		var div = document.getElementById(playerID);
		var element = document.getElementById(playerID).children[0];
		div.replaceChild(img, element);
	}
	
	war(player1, player2, p1Idx, p2Idx)
	{		
		if(p1Idx < 2)
		{
			document.getElementById("game-info").innerHTML = "player " + player1.playerName + " didn't have enough cards for war! They lose!";
			player1.deckIndex = 0;
		}
		else if(p2Idx < 2)
		{
			document.getElementById("game-info").innerHTML = "player " + player2.playerName + " didn't have enough cards for war! They lose!";
			player2.deckIndex = 0;
		}
		else
		{
			//TODO
			if(player1.playerCards[p1Idx - 2].value > player2.playerCards[p2Idx - 2].value)
			{
				console.log("player " + player1.playerName + " wins the war against " + player2.playerName +
				" (" + player1.playerCards[p1Idx - 2].value + " vs. " + player2.playerCards[p2Idx - 2].value + ")");
				
				document.getElementById("game-info").innerHTML = "player " + player1.playerName + " wins the war against " + player2.playerName +
				" (" + player1.playerCards[p1Idx - 2].value + " vs. " + player2.playerCards[p2Idx - 2].value + ")"
				
				player1.playerCards.unshift(player2.playerCards[p2Idx - 2], player2.playerCards[p2Idx - 1], player2.playerCards[p2Idx]);
				player1.deckIndex += 3;
				
				player2.playerCards.pop();
				player2.playerCards.pop();
				player2.playerCards.pop();
				player2.deckIndex -= 3;
			}
			else if(player1.playerCards[p1Idx - 2].value < player2.playerCards[p2Idx - 2].value)
			{
				console.log("player " + player1.playerName + " lost the war against " + player2.playerName +
				" (" + player1.playerCards[p1Idx - 2].value + " vs. " + player2.playerCards[p2Idx - 2].value + ")");
				
				document.getElementById("game-info").innerHTML = "player " + player1.playerName + " lost the war against " + player2.playerName +
				" (" + player1.playerCards[p1Idx - 2].value + " vs. " + player2.playerCards[p2Idx - 2].value + ")"
				
				player2.playerCards.unshift(player1.playerCards[p1Idx - 2], player1.playerCards[p1Idx - 1], player1.playerCards[p1Idx]);
				player2.deckIndex += 3;
				
				player1.playerCards.pop();
				player1.playerCards.pop();
				player1.playerCards.pop();
				player1.deckIndex -= 3;
				
			}
			else
			{
				this.war(player1, player2, p1Idx - 2, p1Idx - 2);
			}
		}
	}
	
	play(player1, player2)
	{		
		var W_ach = ['n', 'n', 'n', 'n', 'n'];
		if (localStorage.getItem("W_localach") === null) {
			console.log("Local storage for W_ach does not exist");
			localStorage.setItem("W_localach", JSON.stringify(W_ach));
		} else {
			console.log("Local storage for W_ach exists");
			W_ach = JSON.parse(localStorage.getItem("W_localach"));
		}
		W_ach[1] = 'y'; localStorage.setItem("W_localach", JSON.stringify(W_ach)); // ACHIEVEMENT: Draw a card
		
		
		if(player1.deckIndex >= 51 || player2.deckIndex <= 0)
		{
			var W_ach = ['n', 'n', 'n', 'n', 'n'];
			if (localStorage.getItem("W_localach") === null) {
				console.log("Local storage for W_ach does not exist");
				localStorage.setItem("W_localach", JSON.stringify(W_ach));
			} else {
				console.log("Local storage for W_ach exists");
				W_ach = JSON.parse(localStorage.getItem("W_localach"));
			}
			W_ach[4] = 'y'; localStorage.setItem("W_localach", JSON.stringify(W_ach)); // ACHIEVEMENT: Win a game
			
			var W_score = [0, 0, 0];
			if (localStorage.getItem("W_localscore") === null) {
				console.log("Local storage for W_localscore does not exist");
				localStorage.setItem("W_localscore", JSON.stringify(W_score));
			} else {
				console.log("Local storage for W_localscore exists");
				W_score = JSON.parse(localStorage.getItem("W_localscore"));
			}
			sm_inc(W_score, 1); localStorage.setItem("W_localscore", JSON.stringify(W_score)); // SCORE: Games won		
			
			document.getElementById("game-info").innerHTML = player1.playerName + " won!"; 
		}
		else if(player2.deckIndex >= 51 || player2.deckIndex <= 0)
		{
			var W_ach = ['n', 'n', 'n', 'n', 'n'];
			if (localStorage.getItem("W_localach") === null) {
				console.log("Local storage for W_ach does not exist");
				localStorage.setItem("W_localach", JSON.stringify(W_ach));
			} else {
				console.log("Local storage for W_ach exists");
				W_ach = JSON.parse(localStorage.getItem("W_localach"));
			}
			W_ach[3] = 'y'; localStorage.setItem("W_localach", JSON.stringify(W_ach)); // ACHIEVEMENT: Lose a game

			var W_score = [0, 0, 0];
			if (localStorage.getItem("W_localscore") === null) {
				console.log("Local storage for W_localscore does not exist");
				localStorage.setItem("W_localscore", JSON.stringify(W_score));
			} else {
				console.log("Local storage for W_localscore exists");
				W_score = JSON.parse(localStorage.getItem("W_localscore"));
			}
			sm_inc(W_score, 2); localStorage.setItem("W_localscore", JSON.stringify(W_score)); // SCORE: Games lost				
			
			document.getElementById("game-info").innerHTML = player2.playerName + " won!";
		}
		else
		{	
			if(player1.playerCards[player1.deckIndex].value > player2.playerCards[player2.deckIndex].value)
			{	
				this.cardUI("p1-card", player1.playerCards[player1.deckIndex].suit, player1.playerCards[player1.deckIndex].rank);
				this.cardUI("p2-card", player2.playerCards[player2.deckIndex].suit, player2.playerCards[player2.deckIndex].rank);
				
				console.log("player " + player1.playerName + " wins against " + player2.playerName +
				" (" + player1.playerCards[player1.deckIndex].value + " vs. " + player2.playerCards[player2.deckIndex].value + ")");
				
				document.getElementById("game-info").innerHTML = "player " + player1.playerName + " wins against " + player2.playerName +
				" (" + player1.playerCards[player1.deckIndex].value + " vs. " + player2.playerCards[player2.deckIndex].value + ")";
				
				let temp = player1.playerCards[player1.deckIndex];
				player1.playerCards.unshift(player2.playerCards[player2.deckIndex], temp);
				player1.playerCards.pop();
				player1.deckIndex++;
				
				player2.playerCards.pop();
				player2.deckIndex--;
				
				document.getElementById("p1-info").innerHTML = player1.playerCards.length;
				document.getElementById("p2-info").innerHTML = player2.playerCards.length;
			}
			else if(player1.playerCards[player1.deckIndex].value < player2.playerCards[player2.deckIndex].value)
			{	
				this.cardUI("p1-card", player1.playerCards[player1.deckIndex].suit, player1.playerCards[player1.deckIndex].rank);
				this.cardUI("p2-card", player2.playerCards[player2.deckIndex].suit, player2.playerCards[player2.deckIndex].rank);
				
				console.log("player " + player1.playerName + " lost against " + player2.playerName +
				" (" + player1.playerCards[player1.deckIndex].value + " vs. " + player2.playerCards[player2.deckIndex].value + ")");
				
				document.getElementById("game-info").innerHTML = "player " + player1.playerName + " lost against " + player2.playerName +
				" (" + player1.playerCards[player1.deckIndex].value + " vs. " + player2.playerCards[player2.deckIndex].value + ")";
				
				let temp = player2.playerCards[player2.deckIndex];
				player2.playerCards.unshift(player1.playerCards[player1.deckIndex], temp);
				player2.playerCards.pop();
				player2.deckIndex++;
				
				player1.playerCards.pop();
				player1.deckIndex--;
				
				document.getElementById("p1-info").innerHTML = player1.playerCards.length;
				document.getElementById("p2-info").innerHTML = player2.playerCards.length;
			}
			else
			{	
				var W_ach = ['n', 'n', 'n', 'n', 'n'];
				if (localStorage.getItem("W_localach") === null) {
					console.log("Local storage for W_ach does not exist");
					localStorage.setItem("W_localach", JSON.stringify(W_ach));
				} else {
					console.log("Local storage for W_ach exists");
					W_ach = JSON.parse(localStorage.getItem("W_localach"));
				}
				W_ach[2] = 'y'; localStorage.setItem("W_localach", JSON.stringify(W_ach)); // ACHIEVEMENT: Tie two cards
				
				
				this.cardUI("p1-card", player1.playerCards[player1.deckIndex].suit, player1.playerCards[player1.deckIndex].rank);
				this.cardUI("p2-card", player2.playerCards[player2.deckIndex].suit, player2.playerCards[player2.deckIndex].rank);
				
				console.log("player " + player1.playerName + " tied against " + player2.playerName +
				". To war!");
				
				document.getElementById("game-info").innerHTML = "player " + player1.playerName + " tied against " + player2.playerName +
				". To war!";
				
				this.war(player1, player2, player1.deckIndex, player2.deckIndex);
			}
		}
	}
}

//gameplay

let gameBoard = new Board();
gameBoard.start('local', 'CPU');
console.log(gameBoard.players);
