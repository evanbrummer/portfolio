//var card = document.querySelector('.card');
//card.addEventListener('click', function ()
//{
//    card.classList.toggle('is-flipped');
//});

labelMsg = document.getElementById("gameMessages");
labelMsgCPU = document.getElementById("gameMessagesCPU");
p1Msg = document.getElementById("p1messages");
turnMsg = document.getElementById("CPUmessages");
drawMsg = document.getElementById("drawMsg");

// Game Status Enum
const GAME_STATUS = {"CONTINUE":1, "WON":2, "LOST":3, "TIE":4};
Object.freeze(GAME_STATUS);

var gameStatus = null;
var selectedCard = null;
var selectedSuit = null;
var newSuit = "";
var cardInTable = null;
var drawnCard = null;
var nextTurn = null;
var currentTurn = null;
var computerTurn = false;
var playerTurn = true;

function sm_inc(arr, i) { // Evan's string incrementer
	arr[i] = (parseInt(arr[i]) + 1).toString();
	return arr;
}

var C_ach = ['n', 'n', 'n', 'n', 'n'];
if (localStorage.getItem("C_localach") === null) {
	console.log("Local storage for C_ach does not exist");
	localStorage.setItem("C_localach", JSON.stringify(C_ach));
} else {
	console.log("Local storage for C_ach exists");
	C_ach = JSON.parse(localStorage.getItem("C_localach"));
}
C_ach[0] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: Play a game

var C_score = [0, 0, 0];
if (localStorage.getItem("C_localscore") === null) {
	console.log("Local storage for C_localscore does not exist");
	localStorage.setItem("C_localscore", JSON.stringify(C_score));
} else {
	console.log("Local storage for C_localscore exists");
	C_score = JSON.parse(localStorage.getItem("C_localscore"));
}
sm_inc(C_score, 0); localStorage.setItem("C_localscore", JSON.stringify(C_score)); // SCORE: Games played

function deal()
{
	var C_ach = ['n', 'n', 'n', 'n', 'n'];
	if (localStorage.getItem("C_localach") === null) {
		console.log("Local storage for C_ach does not exist");
		localStorage.setItem("C_localach", JSON.stringify(C_ach));
	} else {
		console.log("Local storage for C_ach exists");
		C_ach = JSON.parse(localStorage.getItem("C_localach"));
	}
	C_ach[1] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: Deal the hand
	
	document.getElementById("dealButton").style.display = "none";
    initializeObjectsAndStart();
}

function initializeObjectsAndStart()
{
        gameStatus = GAME_STATUS.CONTINUE;

        // Create two players; one computer and local player
        let localPlayer = new Crazy8sPlayer("Player");
        let computer = new Crazy8sPlayer("Computer");
    
        // Create a deck of 52 unique, shuffled cards
        let deck = new Deck()
        deck.createDeck();
        deck.shuffleDeck();
    
        let rules = new Rules("Crazy Eights");
        let helperUI = new UIHelper();
    
        // Deal cards
        for (c = 0; c < rules.getCardsToDeal(); c++)
        {
            localPlayer.takeCardFromTopOfDeck(deck);
            computer.takeCardFromTopOfDeck(deck);
        }
    
        helperUI.placeCardsToTableUI(localPlayer.playerCards, "p1Cards", true);
        helperUI.placeCardsToTableUI(computer.playerCards, "CPUCards", false);
    
        // Initialize the up card
        let discardedCards = [];
        let topCard = deck.drawCard();
    
        while (topCard.value == 8)
        {
            deck.addCardsBackToDeck(topCard);
            topCard = deck.drawCard();
        }
    
        discardedCards.push(topCard);
        helperUI.placeCardsToTableUI(topCard, "deck", false);

        cardInTable = topCard;

        nextTurn = computer;
        currentTurn = localPlayer;

        localPlayer.setSkipStatus(false);
        computer.setSkipStatus(false);
        
        localPlayer.updatePlayableCards();

        continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
}

async function waitForUserToDrawCards(deck, currentPlayer, helperUI)
{
    const waitForUserToDrawCard = async (helperUI) => 
    {
        helperUI.addDrawCardEvent(deck);

        return await new Promise(resolve => 
        {
            let interval = setInterval(() => 
            {
                if (drawnCard != null) {
                    clearInterval(interval);
                    let temp = drawnCard;
                    drawnCard = null;
                    resolve(temp);
                }
            }, 100);
        })
    }

    await new Promise((resolve) => 
        {
            let interval = setInterval(() => 
            {
                if (deck.getSize() != 0 && currentPlayer.getPlayableCards().length == 0) 
                {
                    drawMsg.innerHTML = "No cards to play. Draw from deck";
                    
                    waitForUserToDrawCard(helperUI, deck).then(drawnCard => 
                        {
                            drawCard(drawnCard, helperUI, currentPlayer, deck)
                            currentPlayer.updatePlayableCards()
                        })
                }
                else if (deck.getSize() != 0 && currentPlayer.getPlayableCards().length != 0)
                {
                    drawMsg.innerHTML = "";
                    clearInterval(interval);
                    resolve();
                }
                else if (deck.getSize() == 0)
                {
                    drawMsg.innerHTML = "No cards in deck";
                    clearInterval(interval);
                    resolve();
                }
            }, 100)
        })

    return;
}


function continueTurn(gameStatus, localPlayer, computer, deck, rules, helperUI, discardedCards)
{
    if (gameStatus != GAME_STATUS.CONTINUE)
    {
        return;
    }

    if (currentTurn.playerName == "Player")
    {

        localPlayer.setSkipStatus(false);

        if (!localPlayer.isSkipped())
        {
            const waitForPlayerTurn = async() =>
            {
                return await new Promise((resolve) => 
                    {
                        if (currentTurn.playerName == "Player" && playerTurn == true)
                        {
                            drawMsg.innerHTML = "";
                            turnMsg.innerHTML = "Player's turn";

                            playerTurn = false;
                            resolve();
                        }
                    })
            }

            waitForPlayerTurn().then(() => {
                waitForUserToDrawCards(deck, localPlayer, helperUI).then(() => {
                    waitForUserToSelectCard().then(cardChoice => {
                        playCard(cardChoice, helperUI, discardedCards, localPlayer);
                        //continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
    
                        let localPlayableCards = localPlayer.getPlayableCards();
                        console.log("Local playable cards: ", localPlayableCards);
                        console.log("Discarded Cards", discardedCards);
                        console.log("Top Card: ", cardInTable);
                        console.log("Discarded Cards", discardedCards); 
                    })
                })
            })
            
            
            //waitForUserToSelectCard(localPlayer, helperUI, discardedCards, deck);
            //waitForUserToDrawCards(deck, localPlayer, helperUI);
            continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
            
        }
        else
        {
            continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
        }
    }
    else // current player is Computer
    {
        computer.setSkipStatus(false);

        if (!computer.isSkipped())
        {
            const waitForComputerTurn = async() =>
            {
                return await new Promise((resolve) => 
                    {
                        if (currentTurn.playerName == "Computer" && computerTurn == true)
                        {
                            drawMsg.innerHTML = "";
                            turnMsg.innerHTML = "Computer's turn";

                            computerTurn = false;
                            resolve();
                        }
                    })
            }

            const waitForComputerToDrawCards = async () => 
            {
                return await new Promise(resolve => 
                {
                    let interval = setInterval(() => 
                    {
                        let computerPlayableCards = computer.getPlayableCards();
                        if (computerPlayableCards.length == 0) 
                        {
                            drawCard(deck.peekTopCard(), helperUI, computer, deck);
                            computer.updatePlayableCards();
                        }
                        else
                        {
                            clearInterval(interval);
                            resolve();
                        }

                    }, 1500);
                })
            }

            waitForComputerTurn().then(() => 
            {
                helperUI.removeCardUIEvents(localPlayer.playerCards);
                computer.updatePlayableCards();
                
                waitForComputerToDrawCards().then(() =>{
                    let computerPlayableCards = computer.getPlayableCards();
                    playCard(computerPlayableCards[0], helperUI, discardedCards, computer);
                    localPlayer.updatePlayableCards();

                    //continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
                });
            });

            continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
        }
        else
        {
            continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards);
        }
    }
}


async function continueGame(localPlayer, computer, deck, rules, helperUI, discardedCards)
{
    await getGameStatus(deck, localPlayer, computer).then(gameStatus => continueTurn(gameStatus, localPlayer, computer, deck, rules, helperUI, discardedCards));
    
    if (localPlayer.getNumCardsInHand() == 0) 
    {
		var C_ach = ['n', 'n', 'n', 'n', 'n'];
		if (localStorage.getItem("C_localach") === null) {
			console.log("Local storage for C_ach does not exist");
			localStorage.setItem("C_localach", JSON.stringify(C_ach));
		} else {
			console.log("Local storage for C_ach exists");
			C_ach = JSON.parse(localStorage.getItem("C_localach"));
		}
		C_ach[4] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: Win a game
		
		var C_score = [0, 0, 0];
		if (localStorage.getItem("C_localscore") === null) {
			console.log("Local storage for C_localscore does not exist");
			localStorage.setItem("C_localscore", JSON.stringify(C_score));
		} else {
			console.log("Local storage for C_localscore exists");
			C_score = JSON.parse(localStorage.getItem("C_localscore"));
		}
		sm_inc(C_score, 1); localStorage.setItem("C_localscore", JSON.stringify(C_score)); // SCORE: Games won		

        gameStatus = GAME_STATUS.WON;
        labelMsg.innerHTML = "Player wins - they were able to get rid of their cards first!";
        //helperUI.resetGame();
    }
    else if (computer.getNumCardsInHand() == 0) 
    {
		var C_ach = ['n', 'n', 'n', 'n', 'n'];
		if (localStorage.getItem("C_localach") === null) {
			console.log("Local storage for C_ach does not exist");
			localStorage.setItem("C_localach", JSON.stringify(C_ach));
		} else {
			console.log("Local storage for C_ach exists");
			C_ach = JSON.parse(localStorage.getItem("C_localach"));
		}
		C_ach[3] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: Lose a game    

		var C_score = [0, 0, 0];
		if (localStorage.getItem("C_localscore") === null) {
			console.log("Local storage for C_localscore does not exist");
			localStorage.setItem("C_localscore", JSON.stringify(C_score));
		} else {
			console.log("Local storage for C_localscore exists");
			C_score = JSON.parse(localStorage.getItem("C_localscore"));
		}
		sm_inc(C_score, 1); localStorage.setItem("C_localscore", JSON.stringify(C_score)); // SCORE: Games lost		
		
		gameStatus = GAME_STATUS.LOST;
        labelMsg.innerHTML = "Computer wins - they were able to get rid of their cards first!";
        //helperUI.resetGame();
    }
    else if (deck.getSize() == 0 && localPlayer.isSkipped() && computer.isSkipped()) // There are no more cards in deck so determine winner
    {
            let playerPoints = 0;
            let computerPoints = 0;
    
            localPlayer.playerCards.forEach(card => {
                playerPoints += card.value;
            });

            computer.playerCards.forEach(card => {
                computerPoints += card.value;
            });

            if (playerPoints > computerPoints) 
            {
                gameStatus = GAME_STATUS.WON;
                labelMsg.innerHTML = "Player wins!";
                //helperUI.resetGame();
            }
            else if (playerPoints < computerPoints) 
            {
                gameStatus = GAME_STATUS.LOST;
                labelMsg.innerHTML = "Computer wins!";
                //helperUI.resetGame();
            }
            else
            {
                gameStatus = GAME_STATUS.TIE;
                labelMsg.innerHTML = "It's a tie!";
                //helperUI.resetGame();
            }
    }
    else
    {
        const changeTurns = async (localPlayer, computer) => 
        {
            return await new Promise(resolve => 
            {
                let interval = setInterval(() => 
                {
                    if (nextTurn == currentTurn) 
                    {
                        clearInterval(interval);

                        if (currentTurn.playerName == "Computer")
                        {
                            currentTurn = localPlayer;
                            playerTurn = true;
                        }
                        else if (currentTurn.playerName == "Player")
                        {
                            currentTurn = computer;
                            computerTurn = true;
                        }

                        resolve();
                    }
                }, 100);
            })
        }

        changeTurns(localPlayer, computer);
    }
    
    return;
}

function getGameStatus(deck, localPlayer, computer)
{
    return new Promise
        (resolve => 
        {
            let interval = setInterval(() => 
            {
                if (gameStatus == GAME_STATUS.CONTINUE) 
                {
                    clearInterval(interval);
                    resolve(gameStatus);
                }
                else if (deck.getSize() == 0 && localPlayer.isSkipped() && computer.isSkipped()) 
                {
                    //gameStatus = Rules.determineWinner(localPlayer, computer);

                    // if (gameStatus == GAME_STATUS.WON) {
                    //     labelMsg.innerHTML = "Player wins!- their card total is less than the Computer's card total.";
                    // }
                    // else if (gameStatus == GAME_STATUS.LOST) {
                    //     labelMsg.innerHTML = "Computer wins!- their card total is less than the Player's card total.";
                    // }
                    // else {
                    //     gameStatus = GAME_STATUS.TIE;
                    //     labelMsg.innerHTML = "Tie!- Player's card total and Computer's card total are the same.";
                    // }
                }
            }
        , 100);   
        });
}

function playCard(cardChoice, helperUI, discardedCards, currentPlayer)
{
        currentPlayer.removeFromHand(cardChoice);
        helperUI.playCardUI(cardChoice);
        discardedCards.push(cardChoice);
         
        newSuit = "";
        cardInTable = cardChoice;

        labelMsg.innerHTML = "";
        labelMsgCPU.innerHTML = "";
        drawMsg.innerHTML = "";

        currentPlayer.updatePlayableCards();

        // When an 8 is placed
        if (cardInTable.value == 8 && currentPlayer.playerCards.length != 0) 
        {
            helperUI.showSuitOptions(currentPlayer);

            if (currentPlayer.playerName == "Computer")
            {
                setNewSuit("spades", helperUI, currentPlayer);
            }
            else
            {
				var C_ach = ['n', 'n', 'n', 'n', 'n'];
				if (localStorage.getItem("C_localach") === null) {
					console.log("Local storage for C_ach does not exist");
					localStorage.setItem("C_localach", JSON.stringify(C_ach));
				} else {
					console.log("Local storage for C_ach exists");
					C_ach = JSON.parse(localStorage.getItem("C_localach"));
				}
				C_ach[2] = 'y'; localStorage.setItem("C_localach", JSON.stringify(C_ach)); // ACHIEVEMENT: Change the suit

                waitForSelectedSuit(helperUI, currentPlayer);
            }
        }
        else
        {
            nextTurn = currentPlayer;
        }
}

async function waitForSelectedSuit(helperUI, currentPlayer)
{
    helperUI.removeCardUIEvents(currentPlayer.playerCards);

    await new Promise(resolve => {
        let interval = setInterval(() => 
                    {
                        drawMsg.innerHTML = "";

                        if (selectedSuit != null) 
                        {
                            clearInterval(interval);
                            let temp = selectedSuit;
                            selectedSuit = null;
                            resolve(temp);
                        }
                    }, 100);
    }).then(selectedSuit => setNewSuit(selectedSuit, helperUI, currentPlayer))

    return;
}

function setNewSuit(selectedSuit, helperUI, currentPlayer)
{
    newSuit = selectedSuit;

    if (currentPlayer.playerName == "Computer")
    {
        setTimeout(() => {
            helperUI.hideSuitOptions(currentPlayer, selectedSuit);
                }, 1000)
    }
    else
    {
        helperUI.hideSuitOptions(currentPlayer, selectedSuit);
    }
    
    currentPlayer.updatePlayableCards();
    nextTurn = currentPlayer;
    console.log(newSuit);
}

async function waitForUserToSelectCardd(localPlayer, helperUI, discardedCards, deck)
{
    await getCard().then((cardChoice) => {playCard(cardChoice, helperUI, discardedCards, localPlayer)})

    if (deck.getSize() == 0) 
    {
        localPlayer.setSkipStatus(true);
        labelMsg.innerHTML = "No cards to play. No cards to draw from deck. Skipping " + localPlayer.playerName + "'s turn."
    }
}


function drawCard(drawnCard, helperUI, currentPlayer, deck)
{
    if (deck.getSize() > 0)
    {
        let divID = currentPlayer.playerName == "Player" ? "p1Cards" : "CPUCards";
        helperUI.placeCardsToTableUI(drawnCard, divID, currentPlayer.playerName == "Player" ? true : false);
        helperUI.removeDrawCardEvent();

        currentPlayer.takeCardFromTopOfDeck(deck);
        currentPlayer.updatePlayableCards();
        drawMsg.innerHTML = "";
    }
    else
    {
        drawMsg.innerHTML = "Deck is empty";
        helperUI.removeDeckSource();
    }
}

async function waitForUserToSelectCard()
{
    return await new Promise
        (resolve => 
        {
            let interval = setInterval(() => 
            {
                if (selectedCard != null) 
                {
                    clearInterval(interval);
                    let temp = selectedCard;
                    selectedCard = null;
                    resolve(temp);
                }
            }, 100);   
        });
}

class UIHelper
{
    constructor(){}

	#makeCard(card, toElementID, playerCard)
	{
        let cardTemplate = document.getElementById("card_image");

        if (cardTemplate == null)
        {
            cardTemplate = document.createElement('img');
            cardTemplate.src = "";
            cardTemplate.classList.add("card_image");
            cardTemplate.classList.add("template");
            
            document.body.appendChild(cardTemplate);
        }

		let card_image = cardTemplate.cloneNode(true);
		card_image.src = "../cards/" + card.suit + card.rank + ".png";
        card_image.id = "card" + card.suit + card.rank;

        if (!playerCard) 
        {
            card_image.classList.add("no_hover");

            if (toElementID == "CPUCards")
            {
                card_image.src = "../cards/cardBack.png";
            }
        }
        else 
        {
            card_image.classList.add('card_hover');
        }

		// remove template class to make card visible
		card_image.classList.remove("template");

		// append card to the element of the given element with ID elementID
		document.getElementById(toElementID).append(card_image);
	}

    placeCardsToTableUI(cards, toElementID, isPilePlayable)
    {
        if (Array.isArray(cards))
        {
            cards.forEach(card => {
                this.#makeCard(card, toElementID, isPilePlayable);
            });
        }
        else
        {
            this.#makeCard(cards, toElementID, isPilePlayable);
        }
    }

    setCardUIEvents(card, isPlayable)
    {
        let cardImageID = "card" + card.suit + card.rank;
        let cardImage = document.getElementById(cardImageID);
        
        cardImage.classList.remove('no_hover');

        cardImage.onclick = null;
        cardImage.onclick = function()
        {
            cardImage.classList.remove('shake');
            cardImage.classList.remove('card_hover');
            cardImage.classList.remove('no_hover');

            window.requestAnimationFrame(function() 
            {
                if (!isPlayable) cardImage.classList.add('shake');
                cardImage.classList.add('card_hover');
            });

            if (isPlayable)
            {
                selectedCard = card;
            }
        }
    }

    removeCardUIEvents(cards)
    {
        cards.forEach(card => {
            let cardImageID = "card" + card.suit + card.rank;
            let cardImage = document.getElementById(cardImageID);
            cardImage.classList.add("no_hover");
        });
    }

    addDrawCardEvent(deck)
    {
        let deckSource = document.getElementById("deckSource");

        deckSource.onclick = null;
        deckSource.onclick = function()
        {
            drawnCard = deck.peekTopCard();
        }

    }

    removeDrawCardEvent()
    {
        let deckSource = document.getElementById("deckSource");
        deckSource.onclick = null;
    }

    playCardUI(cardToPutOnPile)
    {
        let cardToPutOnPileImage = document.getElementById("card" + cardToPutOnPile.suit + cardToPutOnPile.rank);

        if (cardToPutOnPileImage.src.endsWith("/cards/cardBack.png"))
        {
            cardToPutOnPileImage.src = "../cards/" + cardToPutOnPile.suit + cardToPutOnPile.rank + ".png";
        }

        let cardOnPileImage = document.getElementById("card" + cardInTable.suit + cardInTable.rank);

        if (cardToPutOnPileImage != null && cardOnPileImage != null)
        {
            // remove event
            cardToPutOnPileImage.classList.remove('shake');
            cardToPutOnPileImage.classList.add('no_hover');
            cardToPutOnPileImage.onclick = null;

            // remove from hand
            cardToPutOnPileImage.parentNode.removeChild(cardToPutOnPileImage);

            // add to pile
            cardOnPileImage.parentNode.appendChild(cardToPutOnPileImage);

            // remove old card
            cardOnPileImage.parentNode.removeChild(cardOnPileImage);
        }
    }

    showSuitOptions(currentPlayer)
    {
        let symbolDiv = document.createElement('div');
        symbolDiv.id = "symbolDiv";

        let symbolHeart = document.getElementById("symbolHeart");

        if (symbolHeart == null)
        {
            symbolHeart = document.createElement('img');
            symbolHeart.src = "../cards/hearts.png";
            symbolHeart.id = "symbolHeart";
            symbolHeart.classList.add("symbol");

            let newSuit = "hearts";
            symbolHeart.onclick = null;
            symbolHeart.onclick = function()
            {
                selectedSuit = newSuit;
            }
        
            symbolDiv.append(symbolHeart);
        }

        let symbolSpades = document.getElementById("symbolSpades");

        if (symbolSpades == null)
        {
            symbolSpades = document.createElement('img');
            symbolSpades.src = "../cards/spades.png";
            symbolSpades.id = "symbolSpades";
            symbolSpades.classList.add("symbol");

            let newSuit = "spades";
            symbolSpades.onclick = null;
            symbolSpades.onclick = function()
            {
                selectedSuit = newSuit;
            }
            
            symbolDiv.append(symbolSpades);
        }

        let symbolClubs = document.getElementById("symbolClubs");

        if (symbolClubs == null)
        {
            symbolClubs = document.createElement('img');
            symbolClubs.src = "../cards/clubs.png";
            symbolClubs.id = "symbolClubs";
            symbolClubs.classList.add("symbol");

            let newSuit = "clubs";
            symbolClubs.onclick = null;
            symbolClubs.onclick = function()
            {
                selectedSuit = newSuit;
            }
            
            symbolDiv.append(symbolClubs);
        }

        let symbolDiamonds = document.getElementById("symbolDiamonds");

        if (symbolDiamonds == null)
        {
            symbolDiamonds = document.createElement('img');
            symbolDiamonds.src = "../cards/diamonds.png";
            symbolDiamonds.id = "symbolDiamonds";
            symbolDiamonds.classList.add("symbol");

            let newSuit = "diamonds";
            symbolDiamonds.onclick = null;
            symbolDiamonds.onclick = function()
            {
                selectedSuit = newSuit;
            }
            
            symbolDiv.append(symbolDiamonds);
        }

        if (currentPlayer.playerName == "Computer")
        {
            labelMsgCPU.append(symbolDiv);
        }
        else
        {
            labelMsg.append(symbolDiv);
        }
    }

    hideSuitOptions(currentPlayer, selectedSuit)
    {
        let symbolDiv = document.getElementById("symbolDiv");

        if (symbolDiv != null)
        {
            symbolDiv.childNodes.forEach(x => x.onclick = null);
            symbolDiv.remove();
        }

        if (currentPlayer.playerName == "Computer")
        {
            labelMsgCPU.innerHTML = currentPlayer.playerName + " has selected " + selectedSuit;
        }
        else
        {
            labelMsg.innerHTML = currentPlayer.playerName + " has selected " + selectedSuit;
        }
    }

    removeDeckSource()
    {
        let deckSource = document.getElementById("deckSource");
        deckSource.style.display = "none";   
    }

    resetGame()
    {
        let p1CardsDiv = document.getElementById("p1Cards");
        p1CardsDiv.innerHTML = "";

        let CPUCardsDiv = document.getElementById("CPUCards");
        CPUCardsDiv.innerHTML = "";

        let cardOnPileImage = document.getElementById("card" + cardInTable.suit + cardInTable.rank);
        cardOnPileImage.parentNode.removeChild(cardOnPileImage);

        p1Msg.innerHTML = "";
        turnMsg.innerHTML = "";
        drawMsg.innerHTML = "";
        labelMsg.innerHTML = "";
        labelMsgCPU.innerHTML = "";

        gameStatus = null;
        selectedCard = null;
        selectedSuit = null;
        newSuit = "";
        cardInTable = null;
        drawnCard = null;
        nextTurn = null;
        currentTurn = null;
        computerTurn = false;

        let dealButton = document.getElementById("dealButton");

        if (dealButton.style.display === "none") 
        {
            dealButton.style.display = "inline-block";
        }
    }

}

class Crazy8sPlayer extends Player
{
    #skipStatus = false;
    #playableCards = [];
    #helperUI = new UIHelper();

    constructor(name)
    {
        super(name, 7);
    }

    removeFromHand(card)
    {
        let i = this.playerCards.indexOf(card);
        this.playerCards.splice(i, 1);
    }

    takeCardFromTopOfDeck(deck)
    {
        this.playerCards.push(deck.drawCard());
    }

    setSkipStatus(status)
    {
        this.#skipStatus = status;
    }

    isSkipped()
    {
        return this.#skipStatus;
    }

    getPlayableCards()
    {
        return this.#playableCards;
    }

    updatePlayableCards()
    {
        this.#playableCards.length = 0; // clear playable cards array

        this.playerCards.forEach(card => 
        {
            if (!this.isCardPlayable(card))
            {
                if (this.playerName != "Computer") 
                    this.#helperUI.setCardUIEvents(card, false);
            }
            else // playable card
            {
                this.#playableCards.push(card);

                if (this.playerName != "Computer") 
                    this.#helperUI.setCardUIEvents(card, true);
            }
        });
    }

    // Move to RULES
    isCardPlayable(card) 
    {
        if (card.value == 8) 
        {
            return true;
        }
        else if (newSuit.length > 0 && card.suit == newSuit) 
        {
            return true;
        }
        else if ((newSuit.length == 0) && ((card.value == cardInTable.value) || card.suit == cardInTable.suit)) 
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    getNumCardsInHand()
    {
        return this.playerCards.length;
    }

}

class Rules
{
    #cardsToDeal;

    constructor(game)
    {
        if (game == "Crazy Eights")
        {
            this.#cardsToDeal = 7;
        }
    }

    getCardsToDeal()
    {
        return this.#cardsToDeal;
    }
}
