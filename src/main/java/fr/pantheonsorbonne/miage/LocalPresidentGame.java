package fr.pantheonsorbonne.miage;

import fr.pantheonsorbonne.miage.enums.RoleValue;
import fr.pantheonsorbonne.miage.exception.NoMoreCardException;
import fr.pantheonsorbonne.miage.game.Card;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class implements the President game locally
 */
public class LocalPresidentGame extends PresidentGameEngine {

    final List<String> initialPlayers;
    public final Map<String, List<Card>> playerCards = new HashMap<>();
    final Map<String, RoleValue> playerRole = new HashMap<>();
    Queue<String> finishedPlayer = new LinkedList<>();


    public LocalPresidentGame(List<String> players) {
        this.initialPlayers = players;
        for (String player : players) {
            playerCards.put(player, new ArrayList<>());

        }
    }

    public static void main(String... args) {
        LocalPresidentGame localPresidentGame = new LocalPresidentGame(
                List.of("Joueur1", "Joueur2", "Joueur3", "Joueur4"));
        localPresidentGame.play();

    }

    @Override
    protected List<String> getInitialPlayers() {
        return this.initialPlayers;
    }

    @Override
    protected void giveCardsToPlayer(String playerName, String hand) {
        List<Card> cards = Arrays.asList(Card.stringToCards(hand));
        this.giveCardsToPlayer(cards, playerName);
    }

    @Override
    protected void declareWinner(String winner) {
        System.out.println(winner + " has won!");
    }

    @Override
    protected Card getCardOrGameOver(Collection<Card> leftOverCard, String cardProviderPlayer,
            String cardProviderPlayerOpponent) {

        if (!this.playerCards.containsKey(cardProviderPlayer) || this.playerCards.get(cardProviderPlayer).isEmpty()) {
            this.playerCards.get(cardProviderPlayerOpponent).addAll(leftOverCard);
            this.playerCards.remove(cardProviderPlayer);
            return null;
        } else {
            return ((Queue<Card>) this.playerCards.get(cardProviderPlayer)).poll();
        }
    }

    @Override
    protected void giveCardsToPlayer(Collection<Card> roundStack, String winner) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(roundStack);
        Collections.shuffle(cards);
        this.playerCards.get(winner).addAll(cards);
    }

    private final static Card DAME_COEUR = Card.valueOf("QH");

    @Override
    protected Queue<String> addFinishedPlayer(String currPlayer) {
        this.finishedPlayer.offer(currPlayer);
        return this.finishedPlayer;
    }
    

    @Override
    protected int getCurrentPlayerCount(List<String> players) {
        return players.size();
    }

    @Override
    protected boolean isTapisFinished(List<Card> tapis) {
        if(tapis.contains(Card.valueOf("2H"))){
            return true;
        }
        if(tapis.contains(Card.valueOf("2D"))){
            return true;
        }
        if(tapis.contains(Card.valueOf("2S"))){
            return true;
        }
        if(tapis.contains(Card.valueOf("2C"))){
            return true;
        }
        return false;
    }

    @Override
    protected String getNextPlayer(String currPlayer, List<String> players) {

        String currPlayerInRound;
        String nextPlayerInRound;
        do {
            // take the first player form the queue
            currPlayerInRound = players.remove(0);
            // and put it immediately at the end
            players.add(currPlayerInRound);
            // take also the next without retriving it
            nextPlayerInRound = players.get(0);

        } while (!Objects.equals(currPlayerInRound, currPlayer));

        return nextPlayerInRound;
    }

    @Override
    protected List<Card> playerPlayCards(String currPlayer, List<Card> tapis) throws NoMoreCardException {
        List<Card> playersHand = this.playerCards.get(currPlayer);
        if (tapis.isEmpty()) {
            if ( ifCarre( playersHand )) {
                return this.cardCarre(currPlayer, playersHand);
            } else if ( ifBrelon( playersHand )) {
                return this.cardBrelon(currPlayer, playersHand);
            } else if ( ifPair( playersHand )) {
                return this.cardPair(currPlayer, playersHand);
            } else if (!playersHand.isEmpty()) {
                //playersHand.removeAll(this.getBestCardsFromPlayer(currPlayer, 1));
                return this.getBestCardsFromPlayer(currPlayer, 1);
            }
        } else {
            
            List<Card> cardPlayedByPlayer = new ArrayList<>();
            Card lastCardInTapis = tapis.get(tapis.size()-1);
                for (Card c : playersHand) {
                    if (c.getValue().getRank() >= lastCardInTapis.getValue().getRank()) {
                        cardPlayedByPlayer.add(c);
                       }
                }
                while(cardPlayedByPlayer.size()!=1 && !cardPlayedByPlayer.isEmpty()){
                    Card cardMin=cardPlayedByPlayer.get(0);
                    for(int i=1;i<cardPlayedByPlayer.size();i++){
                            if(cardPlayedByPlayer.get(i).getValue().getRank()<=cardMin.getValue().getRank()){
                                Card cardAux=cardPlayedByPlayer.get(i);
                                cardPlayedByPlayer.remove(cardMin);
                                cardMin= cardAux;
                            }
                            else{
                                cardPlayedByPlayer.remove(cardPlayedByPlayer.get(i));
                            }
                    }
                }
            return cardPlayedByPlayer;
        }
        return null;
    }

    @Override
    protected List<Card> getBestCardsFromPlayer(String player, int countCard) {
        List<Card> bestCard = new ArrayList<>();
        List<Card> playersHand = this.playerCards.get(player);
        int i=0;
        while (i <countCard ) {
            i++;
            Card currBestCard = playersHand.get(0);
            for (Card c : playersHand) {
                if (c.getValue().getRank() >= currBestCard.getValue().getRank()) {
                    currBestCard = c;
                }
            }
            bestCard.add(currBestCard);
        }
        return bestCard;
    }

    @Override
    protected List<Card> getWorstCardsFromPlayer(String player, int countCard) {
        List<Card> badCard = new ArrayList<>();
        List<Card> playersHand = this.playerCards.get(player);
        int i=0;
        while (i <countCard) {
            i++;
            Card currBadCard = playersHand.get(0);
            for (Card c : playersHand) {
                if (c.getValue().getRank() < currBadCard.getValue().getRank()) {
                    currBadCard = c;
                }
            }
            badCard.add(currBadCard);
        }
        return badCard;
    }


    @Override
    protected String getPlayerWithQueenOFHeart() {
        for (String playerName : this.playerCards.keySet()) {
            if (this.playerCards.get(playerName).contains(DAME_COEUR)) {
                return playerName;
            }
        }
        throw new RuntimeException();
    }

    @Override
    protected String getPresident() {
        return this.getRole(RoleValue.PRESIDENT);
    }

    @Override
    protected String getVicePresident() {
        return this.getRole(RoleValue.VICE_PRESIDENT);
    }

    @Override
    protected String getViceTrou() {
        return this.getRole(RoleValue.VICE_TROU);
    }

    @Override
    protected String getTrou() {
        return getRole(RoleValue.TROU);
    }

    public String getRole(RoleValue role) {
        for (String playerName : this.playerRole.keySet()) {
            if (this.playerRole.get(playerName).equals(role)) {
                return playerName;
            }
        }
        throw new RuntimeException();
    }


    @Override
    protected boolean getFirstParty(int[] numberParty) {
        for (int i = 0; i < numberParty.length; i++) {
            if (i == 0) {
                party = true;
            }
            party = false;
        }
        return party;
    }

    @Override
    protected void printCards(List<Card> cards) {
        System.out.println( cards.stream().map(c -> c.toFancyString()).collect(Collectors.joining(" ")));
        System.out.println();
    }

    @Override
    protected void printCards( String playerName) {
        System.out.println( this.playerCards.get(playerName).stream().map(c -> c.toFancyString()).collect(Collectors.joining(" ")));
        System.out.println();
    }


    public List<Card> cardPair(String player, List<Card> playersHand) {
        playersHand = this.playerCards.get(player);
        List<Card> playerCardPair = new ArrayList<>();
        
            for (int i = 0; i < playersHand.size(); i++) {
                for (int j = i + 1; j < playersHand.size(); j++) {
                    if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                        playerCardPair.add(playersHand.get(i));
                        playerCardPair.add(playersHand.get(j));
                        if (playerCardPair.size()==2) {
                            return playerCardPair;
                        }
                    }
                }
            }
            return playerCardPair;
          
    }

    public List<Card> cardBrelon(String player, List<Card> playersHand) {
        playersHand = this.playerCards.get(player);
        List<Card> playerCardBrelon = new ArrayList<>();

        for (int i = 0; i < playersHand.size(); i++) {
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    playerCardBrelon.add(playersHand.get(j));
                    if (!playerCardBrelon.contains(playersHand.get(i))) {
                        if (playerCardBrelon.contains(playersHand.get(j))) {
                            playerCardBrelon.add(playersHand.get(i));
                            playerCardBrelon.remove(playersHand.get(j));
                            if (playerCardBrelon.size()==3){
                                return playerCardBrelon;
                            }
                        }
                    }
                }
            }
        }
        return playerCardBrelon; 
        
    }

    public List<Card> cardCarre(String player, List<Card> playersHand) {
        playersHand = this.playerCards.get(player);
        List<Card> playerCardCarre = new ArrayList<>();
        for (int i = 0; i < playersHand.size(); i++) {
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    if (!(playerCardCarre.contains(playersHand.get(i)))
                            && !(playerCardCarre.contains(playersHand.get(j)))) {
                        playerCardCarre.add(playersHand.get(j));
                        playerCardCarre.add(playersHand.get(i));
                    }
                }
            }
        }
        return playerCardCarre;

    }

    public boolean ifCarre(List<Card> playersHand ) {
        int countMax = 1;
        for (int i = 0; i < playersHand.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    count++;
                }
            }
            if (count > countMax) {
                countMax = count;
            }
        }
        if (countMax == 4) {
            return true;
        }
        return false;
    }

    public boolean ifBrelon(List<Card> playersHand) {
        int countMax = 1;
        for (int i = 0; i < playersHand.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    count++;
                }
            }
            if (count > countMax) {
                countMax = count;
            }
        }
        if (countMax == 3) {
            return true;
        }
        return false;
    }
    

    public boolean ifPair(List<Card> playersHand) {
        int countMax = 1;
        for (int i = 0; i < playersHand.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    count++;
                }
            }
            if (count > countMax) {
                countMax = count;
            }
        }
        if (countMax == 2) {
            return true;
        }
        return false;
    }


    @Override
    public void removeFromPlayerHand(String currPlayer, List<Card> playedCardByPlayer) {
        this.playerCards.get(currPlayer).removeAll(playedCardByPlayer);
    }

    @Override
    protected boolean pollPlayerWithNoMoreCards(String currPlayer, List<String> players) {
        if ( this.playerCards.get(currPlayer).isEmpty() ) {
            players.remove(currPlayer);
            return true;
        }
        return false;

    }

}
