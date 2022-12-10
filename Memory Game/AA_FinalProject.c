/*
----Team 04----
Team member 1 "Evan"	| "20%"
Team member 2 "Akpobari"| "20%"
Team member 3 "Lydia"	| "20%"
Team member 4 "Ryan"	| "20%"
Team member 5 "Jimmy"	| "20%"
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <string.h>

// STRUCTS
typedef struct scoreInfo_struct
	{
	 int rank;
	 int score;
	 char ini[3];
	} scoreInfo; // structs for player data

// GLOBALS
const int MAX_SCORES = 10; // max scores for the scoreInfo struct

// PROTOTYPES
void circle_shape(int num);
int letterToNum(char inLet);
int NumToLetter(int digit);
void memoryRun(int *totScore, int *totTurn);
void create_high_scores();
void print_high_scores();
void sort_high_scores();
void insert_high_score(int *inScore);

// MAIN
int main(void)
{
	int *mainScore;
	int *mainTurn;
	char start;
	
	printf("Welcome to the Memory game.\nTo start the game press \"g\".\n");
	printf("In the game various shapes will appear with a circle around it.\n");
	printf("You must remember the shapes that are circled and repeat the order.\n");
	printf("Use w for up, s for down, d for right, and a for left.\n");
	mainScore = (int*)malloc(sizeof(int));
	mainTurn = (int*)malloc(sizeof(int));
	
	//if scores.txt is present, show table values otherwise print table
	FILE *scoreTest =  fopen("score.txt", "r");
	if (scoreTest == NULL) {
		printf("score.txt does not exist! Creating file...\n");
		fclose(scoreTest);
		create_high_scores(); // creates the score.txt file
		print_high_scores(); // prints the empty file format
	}
	else { // if it DOES exist...
		fclose(scoreTest);
		sort_high_scores(); // sort the high scores
		print_high_scores(); // print score.txt
	}

	printf("\nTo start the game, press \"g\": ");
	scanf(" %c", &start); // scans char 'g' to start the game
	if (start == 'g') { 
		memoryRun(mainScore,mainTurn); // RUNS THE GAME
	}
	else {
		exit(0); // exit if user doesn't put 'g'
	}
	
	printf("Sorry, you lose, at least you got to turn %d.\n", *mainTurn);
	printf("Your final score was %d. Try again and get even Higher!\n", *mainScore);
	
	insert_high_score(mainScore);

	return 0;
}

// FUNCTIONS

void insert_high_score(int *inScore) {
	FILE *scanScore = fopen("score.txt", "r");
	if (scanScore == NULL) {
		printf("ERROR: Could not open score.txt!\n");
		return;
	}
	scoreInfo scanScores[MAX_SCORES];
	for (int i = 0; i < MAX_SCORES; i++) { // stores scores to check
		fscanf(scanScore, "%d %d %s", &scanScores[i].rank, &scanScores[i].score, scanScores[i].ini);
	}
	fclose(scanScore);
	
	char newIni[3];
	char useless[5];
	if (*inScore > scanScores[9].score) { // if user score beats bottom score...
		fgets(useless, 5, stdin); // cleans out extra chars that the game stores
		printf("Congratulations! You're on the leaderboard!\n");
		printf("Enter 3 initials: ");
		
		scanf(" %s", newIni); // scans for the initials
		
		scanScores[9].score = *inScore; // replaces 10th score
		strcpy(scanScores[9].ini, newIni); // replaces 10th initials
		
		FILE *newScore = fopen("score.txt", "w");
		for (int i = 0; i < MAX_SCORES; i++) { // rewrites the document
			fprintf(newScore, "%d\t%d\t%s\n", scanScores[i].rank, scanScores[i].score, scanScores[i].ini);
		}
		fclose(newScore);
		
		sort_high_scores(); // re-sort
		print_high_scores(); // print so user can bask in victory
	}
	
}

void create_high_scores() {
	FILE *createScore = fopen("score.txt", "w");
	for (int i = 0; i < MAX_SCORES; i++) { // creates and fills new score.txt
		fprintf(createScore, "%d\t0\t---\n", i+1);
	}
	fclose(createScore);
}

void print_high_scores() {
	FILE *readScore = fopen("score.txt", "r");
	scoreInfo highScores[MAX_SCORES];
	
	printf("\nRANK\tSCORE\tINITIALS\n");
	
	for (int i = 0; i < MAX_SCORES; i++) { // scans score.txt into a struct array
		fscanf(readScore, "%d %d %s", &highScores[i].rank, &highScores[i].score, highScores[i].ini);
	}
	for (int j = 0; j < MAX_SCORES; j++) { // prints from the struct array
		printf("%d\t%d\t%s\n", highScores[j].rank, highScores[j].score, highScores[j].ini);
	}
	fclose(readScore);
}

void sort_high_scores() {
	printf("Sorting high scores...\n");
	
	FILE *readScoreToSort = fopen("score.txt", "r");
	scoreInfo scanScores[MAX_SCORES];
	
	if (readScoreToSort == NULL) {
		printf("ERROR: Unable to sort score.txt!\n");
		return;
	}
	
	for (int i = 0; i < MAX_SCORES; i++) { // scans score.txt into a struct array
		fscanf(readScoreToSort, "%d %d %s", &scanScores[i].rank, &scanScores[i].score, scanScores[i].ini);
	}
	
	fclose(readScoreToSort);
	
	int tempHigh = 0;
	char tempIni[4];
	int tempRank = 9;
	int rankCtr;
	
	scoreInfo sortScores[MAX_SCORES];
	
	for (rankCtr = 0; rankCtr < MAX_SCORES; rankCtr++) { // for rank 1-10...
		for (int j = 0; j < MAX_SCORES; j++) {
			if (scanScores[j].score > tempHigh) { // find and store the highest score
				tempHigh = scanScores[j].score;
				tempRank = j;
				strcpy(tempIni, scanScores[j].ini);
			}
		}
		sortScores[rankCtr].score = tempHigh; // sort highest score to the top spot
		strcpy(sortScores[rankCtr].ini, tempIni);
		
		scanScores[tempRank].score = 0; // set high score to 0 so it doesn't get re-counted
		strcpy(scanScores[tempRank].ini, "---"); // clear former high score's initials
		
		strcpy(tempIni, "---"); // reset the temp initials
		tempHigh = 0; // reset the temp high score
	}
	
	FILE *sortedScores = fopen("score.txt", "w");
	for (int i = 0; i < MAX_SCORES; i++) { // re-write the file with sorted scores
		fprintf(sortedScores, "%d\t%d\t%s\n", i+1, sortScores[i].score, sortScores[i].ini);
	}
	fclose(sortedScores);
}

void memoryRun(int *totScore, int *totTurn)
{
	int shapeCircled;
	int turnCounter;
	int scoreCounter;
	int next;
	int letNum;
	char letter;
	int i;
	int pattern[5];
	srand(time(NULL));
	next = 0;
	//shapeCircled = (rand()%4) + 1;
	//circle_shape(shapeCircled);
	turnCounter = 1;
	scoreCounter = 0;
	char go;
	
	
	// The 6 does not mean anything it is arbitrary
	while(next != 6)
	{
		//This statement prints out the turn and score for each iteration of the while loop 
		printf("Turn: %d\nScore: %d\n",turnCounter,scoreCounter);
		//chooses a number between 1 and 4;

		
		for(i = 0; i<5; i++)
		{
			shapeCircled = (rand()%4) + 1;
			//stores the random from 1 to 4 into an array
			pattern[i] = shapeCircled;
			//puts the random between 1 - 4 to circle_shape function where the number input will out put the memory board
			circle_shape(shapeCircled);
			printf("Press any key to continue\n");
			scanf(" %c",&go);
			
		}
		/* printf("PRINTING PATTERN MEMORY:\n");
		for (i = 0; i<5; i++)
		{
			printf("%d",pattern[i]);
		} */ // i think this was for debugging
		printf("\nOkay, what was the order? (Use w,a,s,d)\n");
		for(i = 0; i < 5; i++)
		{
		//printf("%d time: ",i+1); //took this out for QoL
		scanf(" %c", &letter);
		printf("%c\n", letter);
		letNum = letterToNum(letter);
		if(letNum == pattern[i])
		{
			
			scoreCounter++; 
			
		}
		else
		{
			printf("Nope the correct answer was:\n");
			for (int i = 0; i < 5; i++) {
				printf("%c ", NumToLetter(pattern[i]));
			}
			printf("\n");
			
			next = 6;
			*totScore = scoreCounter;
			*totTurn = turnCounter;
			return;
		}
		}
		
		turnCounter++;
		printf("Good job, get ready for the next round\n3...\n2...\n1...\n");
	}
	

}

//Inputs the number from the pattern array and prints out a character with the corresponding digit
int NumToLetter(int digit)
{
	char dlet; // converts the number to a letter for each case
	switch(digit)
	{
		case 1 :
		dlet = 'w';
		break;
		case 2 :
		dlet = 'a';
		break; 
		case 3:
		dlet = 'd'; 
		break;
		case 4:
		dlet = 's';
		break;
		default: 
		printf("Invalid Entry\n");
		
	}
	return dlet;
}

//returns the letter input into numbers in order to correspond with the random number between 1 - 4
int letterToNum(char inLet)
{
	//printf("inLet in letter to num function is %c",inLet);
	int num; 
	switch(inLet) // converts letter to a number for each case
	{
		case 'w' :
		num = 1;
		break;
		case 'a' :
		num = 2;
		break; 
		case 'd':
		num = 3; 
		break;
		case 's':
		num = 4;
		break;
		default: 
		printf("Invalid Entry\n");
		
	}
	//printf("NUM IS: %d",num);
	return num;
}

//prints out the gameboard with the random circle around it.
void circle_shape(int num)
{
	int shapeCircled;
	shapeCircled = num;
	//if triangle is circled
	if (shapeCircled == 1)
	{
		printf("\t\t\t    _____________\n");
		printf("\t\t\t   |      ^      |\n");
		printf("\t\t\t   |     ^ ^     |\n");
		printf("\t\t\t   |    ^   ^    |\n");
		printf("\t\t\t   |   ^     ^   |\n");
		printf("\t\t\t   |  ^       ^  |\n");
		printf("\t\t\t   | ^         ^ |\n");
		printf("\t\t\t   |^^^^^^^^^^^^^|\n");
		printf("\t\t\t    _____________\n\n");
		
	}
	else
	{
		printf("\n");
		printf("\t\t\t\t      ^\n");
		printf("\t\t\t\t     ^ ^\n");
		printf("\t\t\t\t    ^   ^\n");
		printf("\t\t\t\t   ^     ^\n");
		printf("\t\t\t\t  ^       ^\n");
		printf("\t\t\t\t ^         ^\n");
		printf("\t\t\t\t^^^^^^^^^^^^^\n");
		printf("\n\n");
	}

	//SQUARE AND CIRCLE
  // if square is circled
	if (shapeCircled == 2)
	{
		printf("    _____________\n");
		printf("   |HHHHHHHHHHHHH|   \t\t\t\t   OOOOOOO\n");
		printf("   |H           H|   \t\t\t\t O         O\n");
		printf("   |H           H|   \t\t\t\tO           O\n");
		printf("   |H           H|   \t\t\t\tO           O\n");
		printf("   |H           H|   \t\t\t\tO           O\n");
		printf("   |H           H|   \t\t\t\t O         O\n");
		printf("   |HHHHHHHHHHHHH|   \t\t\t\t   OOOOOOO\n");
		printf("    _____________\n\n");
		
	}
   //If Circle is circled 
	else if (shapeCircled == 3)
	{
		printf("\t             \t\t\t\t    _____________\n");
		printf("\tHHHHHHHHHHHHH\t\t\t\t   |   OOOOOOO   |\n");
		printf("\tH           H\t\t\t\t   | O         O |\n");
		printf("\tH           H\t\t\t\t   |O           O|\n");
		printf("\tH           H\t\t\t\t   |O           O|\n");
		printf("\tH           H\t\t\t\t   |O           O|\n");
		printf("\tH           H\t\t\t\t   | O         O |\n");
		printf("\tHHHHHHHHHHHHH\t\t\t\t   |   OOOOOOO   |\n");
		printf("\t             \t\t\t\t    _____________\n\n");
		
	}

	else
	{
		printf("\n");
		printf("\tHHHHHHHHHHHHH\t\t\t\t\t   OOOOOOO\n");
		printf("\tH           H\t\t\t\t\t O         O\n");
		printf("\tH           H\t\t\t\t\tO           O\n");
		printf("\tH           H\t\t\t\t\tO           O\n");
		printf("\tH           H\t\t\t\t\tO           O\n");
		printf("\tH           H\t\t\t\t\t O         O\n");
		printf("\tHHHHHHHHHHHHH\t\t\t\t\t   OOOOOOO\n");
		printf("\n\n");
	}

	//X
	// if x is circled
	if (shapeCircled == 4)
	{
		printf("\t\t\t\t  _____________\n");
		printf("\t\t\t\t |X           X|\n");
		printf("\t\t\t\t |  X       X  |\n");
		printf("\t\t\t\t |    X   X    |\n");
		printf("\t\t\t\t |      X      |\n");
		printf("\t\t\t\t |    X   X    |\n");
		printf("\t\t\t\t |  X       X  |\n");
		printf("\t\t\t\t |X           X|\n");
		printf("\t\t\t\t  _____________\n\n");
		
	}
	else
	{
		int i, j, N;
    int count;

	N = 4;

    count = N * 2 - 1;

    for(i=1; i<=count; i++)
    {
		printf("\t\t\t\t");
        for(j=1; j<=count; j++)
        {
			printf(" ");
            if(j==i || (j==count - i + 1))
            {
                printf("X");
            }
            else
            {
                printf(" ");
            }
        }

        printf("\n");
    }

	}
	}



