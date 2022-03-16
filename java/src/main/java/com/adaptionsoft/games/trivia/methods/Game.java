package com.adaptionsoft.games.trivia.methods;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Game {
	private ArrayList<String> players = new ArrayList<>();
	private int[] places = new int[6];
	private int[] purses = new int[6];
	private boolean[] inPenaltyBox = new boolean[6];
	private final String CORRECT_ANSWER = "Answer was correct!!!!";

	private LinkedList<String> popQuestions = new LinkedList<>();
	private LinkedList<String> scienceQuestions = new LinkedList<>();
	private LinkedList<String> sportsQuestions = new LinkedList<>();
	private LinkedList<String> rockQuestions = new LinkedList<>();

	private int currentPlayer = 0;
	private boolean isGettingOutOfPenaltyBox;

	public Game() {
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
		}
	}

	public String createRockQuestion(int index) {
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean addPlayer(String playerName) {
		players.add(playerName);
		int playerIndex = howManyPlayers() - 1;
		places[playerIndex] = 0;
		purses[playerIndex] = 0;
		inPenaltyBox[playerIndex] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + howManyPlayers());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				movePlayerAndAskQuestion(roll);
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
			}
		} else {
			movePlayerAndAskQuestion(roll);
		}
	}

	private void movePlayerAndAskQuestion(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11)
			places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(players.get(currentPlayer)
				+ "'s new location is "
				+ places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	private void askQuestion() {
		try {
			if (currentCategory() == "Pop")
				System.out.println(popQuestions.removeFirst());
			if (currentCategory() == "Science")
				System.out.println(scienceQuestions.removeFirst());
			if (currentCategory() == "Sports")
				System.out.println(sportsQuestions.removeFirst());
			if (currentCategory() == "Rock")
				System.out.println(rockQuestions.removeFirst());
		} catch (NoSuchElementException e) {

		}
	}

	private String currentCategory() {
		int player = places[currentPlayer];
		if(player == 0 || player == 4 || player == 8){
			return "Pop";
		}else if(player == 1 || player == 5 || player == 9){
			return "Science";
		}else if(player == 2 || player == 6 || player == 10){
			return "Sports";
		}else{
			return "Rock";
		}		
	}

	public boolean wasCorrectlyAnswered() {
		boolean winner;
		if (inPenaltyBox[currentPlayer]) {
			if (isGettingOutOfPenaltyBox) {
				increasePurseForCurrentPlayer();
				winner = didPlayerWin();
				currentPlayer++;
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size())
					currentPlayer = 0;
				return true;
			}

		} else {
			increasePurseForCurrentPlayer();
			currentPlayer++;
			if (currentPlayer == players.size())
				currentPlayer = 0;
			winner = didPlayerWin();
			currentPlayer++;
			return winner;
		}
	}

	public void increasePurseForCurrentPlayer(){
		System.out.println(CORRECT_ANSWER);
				if (currentPlayer == players.size())
					currentPlayer = 0;
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;

		currentPlayer++;
		if (currentPlayer == players.size())
			currentPlayer = 0;
		return true;
	}

	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
