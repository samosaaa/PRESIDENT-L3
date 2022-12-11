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

    final Set<String> initialPlayers;
    public final Map<String, List<Card>> playerCards = new HashMap<>();
    final Map<String, RoleValue> playerRole = new HashMap<>();
    Queue<String> finishedPlayer = new LinkedList<>();


    public LocalPresidentGame(Set<String> initialPlayers) {
        this.initialPlayers = initialPlayers;
        for (String player : initialPlayers) {
            playerCards.put(player, new ArrayList<>());

        }
    }

    public static void main(String... args) {
        LocalPresidentGame localPresidentGame = new LocalPresidentGame(
                Set.of("Joueur1", "Joueur2", "Joueur3", "Joueur4"));
        localPresidentGame.play();

    }

    @Override
    protected Set<String> getInitialPlayers() {
        return this.initialPlayers;
    }

    @Override
    protected void giveCardsToPlayer(String playerName, String hand) {
        List<Card> cards = Arrays.asList(Card.stringToCards(hand));
        this.giveCardsToPlayer(cards, playerName);
    }

    @Override
    protected boolean playRound(Queue<String> players, String playerA, String playerB, Queue<Card> roundDeck) {
        System.out.println("New round:");
        System.out
                .println(
                        this.playerCards
                                .keySet().stream().filter(p -> !this.playerCards.get(p).isEmpty()).map(
                                        p -> p + " has "
                                                + this.playerCards.get(p).stream().map(c -> c.toFancyString())
                                                        .collect(Collectors.joining(" ")))
                                .collect(Collectors.joining("\n")));
        System.out.println();
        return super.playRound(players, playerA, playerB, roundDeck);

    }

    @Override
    protected void declareWinner(String winner) {
        System.out.println(winner + " has won!");
    }



    @Override
    protected void giveCardsToPlayer(Collection<Card> roundStack, String winner) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(roundStack);
        Collections.shuffle(cards);
        this.playerCards.get(winner).addAll(cards);
    }

    @Override
    protected Card getCardFromPlayer(String player) throws NoMoreCardException {
        if (this.playerCards.get(player).isEmpty()) {
            throw new NoMoreCardException();
        } else {
            return ((Queue<Card>) this.playerCards.get(player)).poll();
        }
    }

    private final static Card DAME_COEUR = Card.valueOf("QH");

    @Override
    protected Queue<String> addFinishedPlayer(String currPlayer) {
        this.finishedPlayer.offer(currPlayer);
        return finishedPlayer;
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
    protected String getNextPlayer(String currPlayer) {
        Queue<String> playersIn = new LinkedList<>();
        playersIn.addAll(this.getInitialPlayers());

        String currPlayerInRound;
        String nextPlayerInRound;
        do {
            // take the first player form the queue
            currPlayerInRound = playersIn.poll();
            // and put it immediately at the end
            playersIn.offer(currPlayerInRound);
            // take also the next without retriving it
            nextPlayerInRound = playersIn.peek();

        } while (!Objects.equals(currPlayerInRound, currPlayer));

        return nextPlayerInRound;
    }

    @Override
    protected List<Card> playerPlayCards(String currPlayer, List<Card> tapis) throws NoMoreCardException {
        List<Card> playersHand = this.playerCards.get(currPlayer);
        if (tapis.isEmpty() && !playersHand.isEmpty()) {
            if (ifCarre( playersHand )) {
                //playersHand.removeAll(this.cardCarre(currPlayer, playersHand));
                return this.cardCarre(currPlayer, playersHand);
            } else if ( ifBrelon( playersHand )) {
                //playersHand.removeAll(this.cardBrelon(currPlayer, playersHand));
                return this.cardBrelon(currPlayer, playersHand);
            } else if ( ifPair( playersHand )) {
                //playersHand.removeAll(this.cardPair(currPlayer, playersHand));
                return this.cardPair(currPlayer, playersHand);
            } else {
                //playersHand.removeAll(this.getBestCardsFromPlayer(currPlayer, 1));
                return this.getBestCardsFromPlayer(currPlayer, 1);
            }
        } else {
            if (playersHand.isEmpty()) {
                System.out.println(currPlayer +" Has no more cards in hand, he's out of the party");

            }

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
    }

    @Override
    protected List<Card> getBestCardsFromPlayer(String player, int countCard) {
        List<Card> bestCard = new ArrayList<>();
        List<Card> playersHand = this.playerCards.get(player);
        for (int i = 0; i < countCard; i++) {
            
            Card currBestCard = playersHand.get(0);
            for (Card c : playersHand) {
                if (c.getValue().getRank() > currBestCard.getValue().getRank()) {
                    currBestCard = c;
                }
            }
            bestCard.add(currBestCard);
        }
        return bestCard;
    }

    @Override
    protected Collection<Card> getWorstCardsFromPlayer(String player, int countCard) {
        List<Card> badCard = new ArrayList<>();
        List<Card> playersHand = this.playerCards.get(player);
        for (int i = 0; i < countCard; i++) {
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

    public List<Card> cardPair(String player, List<Card> playersHand) {
        playersHand = this.playerCards.get(player);
        List<Card> playerCardPair = new ArrayList<>();
        for (int i = 0; i < playersHand.size(); i++) {
            for (int j = i + 1; j < playersHand.size(); j++) {
                if (playersHand.get(i).getValue().equals(playersHand.get(j).getValue())) {
                    playerCardPair.add(playersHand.get(i));
                    playerCardPair.add(playersHand.get(j));
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


}
