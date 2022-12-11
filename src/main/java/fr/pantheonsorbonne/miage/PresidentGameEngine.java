package fr.pantheonsorbonne.miage;

import fr.pantheonsorbonne.miage.exception.NoMoreCardException;
//import fr.pantheonsorbonne.miage.exception.NoMorePlayerException;
import fr.pantheonsorbonne.miage.game.Card;
import fr.pantheonsorbonne.miage.game.Deck;
import fr.pantheonsorbonne.miage.enums.RoleValue;
import java.util.*;
import java.util.stream.Collectors;

/**
 * this class is a abstract version of the engine, to be used locally on through the network
 */
public abstract class PresidentGameEngine {

    public static final int CARDS_IN_HAND_INITIAL_COUNT = 13;
    protected final int[] numberParty = new int[2];
    protected boolean party = false;
    private Queue<String> winnerPlayer = new LinkedList<>();

    /**
     * play a President game wit the provided players
     */
    public void play()  {
        //for ( int i=0; i<numberParty.length; i++ ) {
            //send the initial hand to every players
            for (String playerName : getInitialPlayers()) {
                //get random cards
                Card[] cards = Deck.getRandomCards(CARDS_IN_HAND_INITIAL_COUNT);
                // transform them to String
                String hand = Card.cardsToString(cards);
                //send them to this players
                giveCardsToPlayer(playerName, hand);
                System.out.println("cards have been partagé");
            }
            // make a queue with all the players
            List<String> players = new LinkedList<>();
            players.addAll(this.getInitialPlayers());
            System.out.println("players are in a the List");
            String firstPlayerInRound = "";
            //if (getFirstParty(numberParty)) {
                firstPlayerInRound = this.getPlayerWithQueenOFHeart();
                System.out.println("firstplayer to play, having QUEEN of HEART is : "+firstPlayerInRound);

            //} 
            /* else {
                firstPlayerInRound = this.getPresident(); 
                Collection<Card> presCards = this.getWorstCardsFromPlayer(firstPlayerInRound, 2);
                Collection<Card> trouCards = this.getBestCardsFromPlayer(this.getTrou(), 2);
                this.giveCardsToPlayer(trouCards, firstPlayerInRound);
                this.giveCardsToPlayer(presCards, this.getTrou());

                Collection<Card> vicePresCards = this.getWorstCardsFromPlayer(this.getVicePresident(), 1);
                Collection<Card> viceTrouCards = this.getBestCardsFromPlayer(this.getViceTrou(), 1);               
                this.giveCardsToPlayer(viceTrouCards, this.getVicePresident());
                this.giveCardsToPlayer(vicePresCards, this.getViceTrou());
            } */

            String currPlayer = firstPlayerInRound;
            List<Card> tapis = new LinkedList<>();
            System.out.println("the tapis is initialized, Lets START");
            int skipped = 0;

            while (this.getCurrentPlayerCount(players) >= 2) {
                List<Card> playedCardByPlayer = new LinkedList<>(); 
                try {
                    playedCardByPlayer = this.playerPlayCards(currPlayer, tapis);
                    System.out.println(currPlayer+ " decide to play: \n");
                    printCards(playedCardByPlayer);

                    tapis.addAll(playedCardByPlayer);
                    //printCards(tapis);

                    removeFromPlayerHand(currPlayer, playedCardByPlayer);

                } catch (NoMoreCardException e) {                         //quand le joueur a une main vide

                    System.out.println(currPlayer +" Has no more cards, he's out of the party, and he WON");
                    this.addFinishedPlayer(currPlayer);

                    players.remove(currPlayer);

                    currPlayer=this.getNextPlayer(currPlayer,players);
                    continue;
                }



                if (playedCardByPlayer.isEmpty()) {
                    skipped++;
                    System.out.println(currPlayer+" has skipped !");
                }


                
                if (skipped == this.getCurrentPlayerCount(players) - 1) {
                    tapis.removeAll(tapis);
                    skipped = 0;
                    System.out.println("everyone has skipped, the tapis is plié");
                    continue;
                }


                if (!this.isTapisFinished(tapis)) {
                    currPlayer = this.getNextPlayer(currPlayer,players);
                    System.out.println("next player to play is "+currPlayer);
                } else {
                    tapis.removeAll(tapis);
                    skipped = 0;
                }

            }            
        //}
        String winner = winnerPlayer.poll(); 
        //send him the gameover and leave
        declareWinner(winner);
        System.out.println(winner + " is the PRESIDENT! ");
        System.exit(0);
    }



    protected abstract void removeFromPlayerHand(String currPlayer, List<Card> playedCardByPlayer);



    /**
     * add the player to the collection of the player that have no more cards
     * @param currPlayer the player that has no more cards
     * @return 
     */
    protected abstract Queue<String> addFinishedPlayer(String currPlayer);

    /**
     *
     * @return the number of player still playing to the game
     */
    protected abstract int getCurrentPlayerCount(List<String> player);

    /**
     *
     * @param tapis the current tapis
     * @return true if no other card can be played (all 2)
     */
    protected abstract boolean isTapisFinished(List<Card> tapis);

    /**
     *
     * @param currPlayer last player to have played
     * @return the next player to play
     */
    protected abstract String getNextPlayer(String currPlayer, List<String> players);

    /**
     * @param currPlayer the player to play the cards
     * @param tapis      the current state of the tapis
     * @return a collection of cards played by the player, the c ollection is empty if the player cannot play
     */
    protected abstract List<Card> playerPlayCards(String currPlayer, List<Card> tapis) throws NoMoreCardException;

    protected abstract Collection<Card> getBestCardsFromPlayer(String trou, int i);

    protected abstract Collection<Card> getWorstCardsFromPlayer(String firstPlayerInRound, int i);

    protected abstract String getPlayerWithQueenOFHeart();

    protected abstract String getPresident();

    protected abstract String getViceTrou();

    protected abstract String getVicePresident();

    protected abstract String getTrou();

    protected void printCards(List<Card> cards) {
        System.out.println( cards.stream().map(c -> c.toFancyString()).collect(Collectors.joining(" ")));
        System.out.println();
    }

    protected abstract boolean getFirstParty(int [] numberParty);


    /**
     * provide the list of the initial players to play the game
     *
     * @return
     */
    protected abstract List<String> getInitialPlayers();

    /**
     * give some card to a player
     *
     * @param playerName the player that will receive the cards
     * @param hand       the cards as a string (to be converted later)
     */
    protected abstract void giveCardsToPlayer(String playerName, String hand);

    /**
     * Play a single round
     *
     * @param players             the queue containing the remaining players
     * @param firstPlayerInRound  the first contestant in this round
     * @param secondPlayerInRound the second contestant in this round
     * @param roundDeck           possible cards left over from previous rounds
     * @return true if we have a winner for this round, false otherwise
     */
    protected boolean playRound(Queue<String> players, String firstPlayerInRound, String secondPlayerInRound, Queue<Card> roundDeck) {
        return party;
    }

    /**
     * this method must be called when a winner is identified
     *
     * @param winner the final winner of the same
     */
    protected abstract void declareWinner(String winner);


    /**
     * give the winner of a round
     *
     * @param contestantA     a contestant
     * @param contestantB     another contestand
     * @param contestantACard its card
     * @param contestantBCard its card
     * @return the name of the winner or null if it's a tie
     */
    protected static String getWinner(String contestantA, String contestantB, Card contestantACard, Card contestantBCard) {
        if (contestantACard.getValue().getRank() > contestantBCard.getValue().getRank()) {
            return contestantA;
        } else if (contestantACard.getValue().getRank() < contestantBCard.getValue().getRank()) {
            return contestantB;
        }
        return null;
    }

    /**
     * give some card to a player
     *
     * @param playerName the player that will receive the cards
     * @param cards      the cards as a collection of cards
     */
    protected abstract void giveCardsToPlayer(Collection<Card> cards, String playerName);



}
