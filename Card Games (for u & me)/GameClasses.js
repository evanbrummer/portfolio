/*
 This file holds the class objects that can be used across different games
 */
class Card
{
	constructor(suit, rank, value)
	{
		this.suit = suit;
		this.rank = rank;
		this.value = value;
	}
}

class Deck
{
	constructor()
	{
		this.cards = [];
	}

	createDeck()
	{
		let suits = ['clubs', 'diamonds', 'hearts', 'spades'];
		let ranks = ['A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K'];
		let values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13];

		for (let i = 0; i < suits.length; i++)
		{
			for (let j = 0; j < ranks.length; j++)
			{
				this.cards.push(new Card(suits[i], ranks[j], values[j]));
			}
		}
	}

	shuffleDeck()
	{
		let location1, location2, tmp;
		for (let i = 0; i < 1000; i++)
		{
			location1 = Math.floor((Math.random() * this.cards.length));
			location2 = Math.floor((Math.random() * this.cards.length));
			tmp = this.cards[location1];
			this.cards[location1] = this.cards[location2];
			this.cards[location2] = tmp;
		}
	}

	drawCard()
	{
		let card;

		if (this.cards.length > 0)
		{
			card = this.cards.pop();
		}

		return card;
	}

	addCardsBackToDeck(cardsToAddBack) 
	{
		if (cardsToAddBack == Array) 
		{
			let prevCardsLength = this.cards.length;

			for (let i = 0; i < cardsToAddBack.length; i++) 
			{
				this.cards.unshift(cardsToAddBack[i]);
			}

			if (prevCardsLength == 0) this.shuffleDeck();

		}
		else
		{
			this.cards.unshift(cardsToAddBack);
		}
	}

	peekTopCard()
	{
		let card = null;

		if (this.cards.length > 0)
		{
			card = this.cards[this.cards.length - 1];
		}

		return card;
	}

	getSize()
	{
		return this.cards.length;
	}
}

class Player
{
	constructor(name, givenDeckIndex)
	{
		this.playerName = name;
		this.playerCards = [];
		this.deckIndex = givenDeckIndex;
	}

	
}