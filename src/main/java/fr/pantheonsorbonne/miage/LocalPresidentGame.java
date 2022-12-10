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
    final Map<String, ArrayList<Card>> playerCards = new HashMap<>();
    final Map<String, RoleValue> playerRole = new HashMap<>();

    public LocalPresidentGame(Set<String> initialPlayers) {
        this.initialPlayers = initialPlayers;
        for (String player : initialPlayers) {
            playerCards.put(player, new ArrayList<>());
            
        }
    }

    public static void main(String... args) {
        LocalPresidentGame localPresidentGame = new LocalPresidentGame(Set.of("Joueur1", "Joueur2", "Joueur3", "Joueur4"));
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
        Queue<String> finishedPlayer = new LinkedList<>();
        finishedPlayer.offer(currPlayer);
        return finishedPlayer;
    }

    @Override
    protected int getCurrentPlayerCount(Queue<String> players) {
        return players.size();
    }


    @Override
    protected boolean isTapisFinished(Collection<Card> tapis) {
        Object[] tapisOuvert = tapis.toArray();
        Card lastCardInTapis = (Card) tapisOuvert[-1];
        if (lastCardInTapis.getValue().getRank() == 2){
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
    protected Collection<Card> playerPlayCards(String currPlayer, Collection<Card> tapis) throws NoMoreCardException {
        List<Card> playersHand = this.playerCards.get(currPlayer);
        if (tapis.isEmpty()) {
            if(ifCarre(currPlayer)){
                return this.cardCarre(currPlayer, playersHand);
            }
            else if(ifBrelon(currPlayer)){
                return this.cardBrelon(currPlayer, playersHand);
            }
            else if(ifPair(currPlayer)){
                return this.cardPair(currPlayer, playersHand);
            }
            else{
                return this.getBestCardsFromPlayer(currPlayer, 1);
            }
        }
        else{
            List<Card> cardPlayedByPlayer= new ArrayList<>();
            Card lastCardInTapis = null;
            for (Card element : tapis){
                element =lastCardInTapis;
            }
            for (Card c : playersHand){
                if (c.getValue().getRank() > lastCardInTapis.getValue().getRank()){
                    cardPlayedByPlayer.add(c);

                }
            }
            return  cardPlayedByPlayer;
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
    protected boolean getFirstParty(int [] numberParty) {
        for (int i = 0; i < numberParty.length; i++) {
            if (i == 0) {
                party=true;
            }
            party=false;
        }
        return party;
    }


    public Collection<Card> cardPair(String player, List<Card> playersHand){
        playersHand = this.playerCards.get(player);
        List<Card> playerCardPair = new ArrayList<>();
        for(int i = 0;i<playersHand.size();i++){ 
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){
                    playerCardPair.add(playersHand.get(i));
                    playerCardPair.add(playersHand.get(j));
                }
            }
        }
        return playerCardPair;
        
    } 
    public Collection<Card> cardBrelon(String player, List<Card> playersHand){
        playersHand = this.playerCards.get(player);
        List<Card> playerCardBrelon = new ArrayList<>();
        for(int i = 0;i<playersHand.size();i++){ 
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){
                    playerCardBrelon.add(playersHand.get(j));
                    if(!playerCardBrelon.contains(playersHand.get(i))){
                        if(playerCardBrelon.contains(playersHand.get(j))){
                        playerCardBrelon.add(playersHand.get(i));
                        playerCardBrelon.remove(playersHand.get(j));
                        }
                    }
                }
            }
        }
        return playerCardBrelon;
        
    } 
    public Collection<Card> cardCarre(String player, List<Card> playersHand){
        playersHand = this.playerCards.get(player);
        List<Card> playerCardCarre = new ArrayList<>();
        for(int i = 0;i<playersHand.size();i++){ 
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){
                    if(!(playerCardCarre.contains(playersHand.get(i))) && !(playerCardCarre.contains(playersHand.get(j)))){                   
                        playerCardCarre.add(playersHand.get(j));
                        playerCardCarre.add(playersHand.get(i));
                    }
                }
            }
        }
        return playerCardCarre;
        
    }    

    public boolean ifCarre(String player){
        List<Card> playersHand = this.playerCards.get(player);
        int countMax = 1; 
        for(int i = 0;i<playersHand.size();i++){ 
            int count = 1;
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){ 
        			count++; 
        		} 
        	} 
            if (count>countMax){
                countMax=count;
            }
        }
        if(countMax == 4) {
            return true;
        }
        return false;
    }

    public boolean ifBrelon(String player){
        List<Card> playersHand = this.playerCards.get(player);
        int countMax = 1; 
        for(int i = 0;i<playersHand.size();i++){ 
            int count = 1;
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){ 
        			count++; 
        		} 
        	} 
            if (count>countMax){
                countMax=count;
            }
        }        
        if (countMax == 3) { 
            return true;  
        }
        return false;
    }

    public boolean ifPair(String player){
        List<Card> playersHand = this.playerCards.get(player);
        int countMax = 1; 
        for(int i = 0;i<playersHand.size();i++){ 
            int count = 1;
        	for(int j = i+1;j<playersHand.size();j++){ 
        		if(playersHand.get(i).getValue().equals(playersHand.get(j).getValue())){ 
        			count++; 
        		} 
        	} 
            if (count>countMax){
                countMax=count;
            }
        }
        if (countMax == 2) { 

            return true; 
        } 
        return false;
    }


}
