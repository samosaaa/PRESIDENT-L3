package fr.pantheonsorbonne.miage;

import fr.pantheonsorbonne.miage.exception.NoMoreCardException;
import fr.pantheonsorbonne.miage.game.Card;
import fr.pantheonsorbonne.miage.model.Game;
import fr.pantheonsorbonne.miage.model.GameCommand;

import java.util.*;

/**
 * This class implements the President game with the network engine
 */
public class PresidentGameNetworkEngine extends PresidentGameEngine {
    private static final int PLAYER_COUNT = 3;

    private final HostFacade hostFacade;
    private final Set<String> players;
    private final Game President;

    public PresidentGameNetworkEngine(HostFacade hostFacade, Set<String> players, fr.pantheonsorbonne.miage.model.Game President) {
        this.hostFacade = hostFacade;
        this.players = players;
        this.President = President;
    }

    public static void main(String[] args) {
        //create the host facade
        HostFacade hostFacade = Facade.getFacade();
        hostFacade.waitReady();

        //set the name of the player
        hostFacade.createNewPlayer("Host");

        //create a new game of President
        fr.pantheonsorbonne.miage.model.Game President = hostFacade.createNewGame("President");

        //wait for enough players to join
        hostFacade.waitForExtraPlayerCount(PLAYER_COUNT);

        PresidentGameEngine host = new PresidentGameNetworkEngine(hostFacade, President.getPlayers(), President);
        host.play();


    }

    /**
     * get the set of players initially in the game
     *
     * @return
     */
    @Override
    protected Set<String> getInitialPlayers() {
        return this.President.getPlayers();
    }

    /**
     * give this hand (as string) the the provided player
     *
     * @param playerName name of the player to receive the cards
     * @param hand       the cards as Strings
     */
    @Override
    protected void giveCardsToPlayer(String playerName, String hand) {
        hostFacade.sendGameCommandToPlayer(President, playerName, new GameCommand("cardsForYou", hand));
    }


    @Override
    protected void declareWinner(String winner) {
        hostFacade.sendGameCommandToPlayer(President, winner, new GameCommand("gameOver", "win"));
    }

    /**
     * Try to get a card from the player. If it fails, give roundStack to the other player
     *
     * @param leftOverCard               current cards at stake
     * @param cardProviderPlayer         the player (to provide a card)
     * @param cardProviderPlayerOpponent its opponent (to receive the stack if contestantA does not have cards anymore)
     * @return the card from contestant A or null if contetant A is gameover
     */
    @Override
    protected Card getCardOrGameOver(Collection<Card> leftOverCard, String cardProviderPlayer, String cardProviderPlayerOpponent) {

        try {
            return getCardFromPlayer(cardProviderPlayer);
        } catch (NoMoreCardException nmc) {
            //contestant A is out of cards
            //we send him a gameover
            hostFacade.sendGameCommandToPlayer(President, cardProviderPlayer, new GameCommand("gameOver"));
            //remove him from the queue so he won't play again
            players.remove(cardProviderPlayer);
            //give back all the cards for this round to the second players
            hostFacade.sendGameCommandToPlayer(President, cardProviderPlayerOpponent, new GameCommand("cardsForYou", Card.cardsToString(leftOverCard.toArray(new Card[leftOverCard.size()]))));
            return null;
        }

    }

    /**
     * give this stack of card to the winner player
     *
     * @param roundStack a stack of card at stake
     * @param winner     the winner
     */
    @Override
    protected void giveCardsToPlayer(Collection<Card> roundStack, String winner) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(roundStack);
        //shuffle the round deck so we are not stuck
        Collections.shuffle(cards);
        hostFacade.sendGameCommandToPlayer(President, winner, new GameCommand("cardsForYou", Card.cardsToString(cards.toArray(new Card[cards.size()]))));
    }

    /**
     * we get a card from a player, if possible.
     * <p>
     * If the player has no more card, throw an exception
     *
     * @param player the name of the player
     * @return a card from a player
     * @throws NoMoreCardException if player has no more card.
     */
    @Override
    protected Card getCardFromPlayer(String player) throws NoMoreCardException {
        hostFacade.sendGameCommandToPlayer(President, player, new GameCommand("playACard"));
        GameCommand expectedCard = hostFacade.receiveGameCommand(President);
        if (expectedCard.name().equals("card")) {
            return Card.valueOf(expectedCard.body());
        }
        if (expectedCard.name().equals("outOfCard")) {
            throw new NoMoreCardException();
        }
        //should not happen!
        throw new RuntimeException("invalid state");

    }

    @Override
    protected String getPlayerWithQueenOFHeart() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Queue<String> addFinishedPlayer(String currPlayer) {
        return null;
        // TODO Auto-generated method stub
        
    }

    @Override
    protected int getCurrentPlayerCount(Queue<String> player) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected boolean isTapisFinished(Collection<Card> tapis) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String getNextPlayer(String currPlayer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection<Card> playerPlayCards(String currPlayer, Collection<Card> tapis) throws NoMoreCardException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection<Card> getBestCardsFromPlayer(String trou, int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection<Card> getWorstCardsFromPlayer(String firstPlayerInRound, int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getPresident() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getViceTrou() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getVicePresident() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getTrou() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean getFirstParty(int[] numberParty) {
        // TODO Auto-generated method stub
        return false;
    }

}
