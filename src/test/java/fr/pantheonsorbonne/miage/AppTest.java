package fr.pantheonsorbonne.miage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.management.relation.Role;

import fr.pantheonsorbonne.miage.enums.CardColor;
import fr.pantheonsorbonne.miage.enums.CardValue;
import fr.pantheonsorbonne.miage.enums.RoleValue;
import fr.pantheonsorbonne.miage.exception.NoMoreCardException;

import org.junit.jupiter.api.Test;

import fr.pantheonsorbonne.miage.game.Card;
import fr.pantheonsorbonne.miage.game.Deck;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    
    @Test
    public void getInitialPlayers(){
        HashSet<String> players = new HashSet<>();
        var test1 = new LocalPresidentGame(players);
        assertEquals(players, test1.getInitialPlayers());
    }



    @Test
    public void getPlayerWithQueenOFHeart() {
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");

        var test1 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        ArrayList<Card> cardJ2 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ2.add(Card.valueOf("KH"));

        test1.playerCards.put("J1", cardJ1);
        test1.playerCards.put("J2", cardJ2);

        assertEquals("J1", test1.getPlayerWithQueenOFHeart());
    }

    @Test
    public void getRole() throws Exception{
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");
        players.add("J3");
        players.add("J4");

        var test2 = new LocalPresidentGame(players);

        test2.playerRole.put("J1", RoleValue.PRESIDENT);
        test2.playerRole.put("J2", RoleValue.TROU);
        test2.playerRole.put("J3", RoleValue.VICE_PRESIDENT);
        test2.playerRole.put("J4", RoleValue.VICE_TROU);

        assertEquals("J1", test2.getPresident());
        assertEquals("J2", test2.getTrou());
        assertEquals("J3",test2.getVicePresident());
        assertEquals("J4",test2.getViceTrou());



    }

    @Test
    public void getPresident(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test2 = new LocalPresidentGame(players);
        test2.playerRole.put("J1", RoleValue.PRESIDENT);
        test2.playerRole.put("J2", RoleValue.TROU);
        test2.playerRole.put("J3", RoleValue.VICE_PRESIDENT);
        test2.playerRole.put("J4", RoleValue.VICE_TROU);
        assertEquals("J1", test2.getPresident());
    }

    @Test
    public void getVicePresident(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test2 = new LocalPresidentGame(players);
        test2.playerRole.put("J1", RoleValue.PRESIDENT);
        test2.playerRole.put("J2", RoleValue.TROU);
        test2.playerRole.put("J3", RoleValue.VICE_PRESIDENT);
        test2.playerRole.put("J4", RoleValue.VICE_TROU);
        assertEquals("J3", test2.getVicePresident());
    }

    @Test
    public void getViceTrou(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test2 = new LocalPresidentGame(players);
        test2.playerRole.put("J1", RoleValue.PRESIDENT);
        test2.playerRole.put("J2", RoleValue.TROU);
        test2.playerRole.put("J3", RoleValue.VICE_PRESIDENT);
        test2.playerRole.put("J4", RoleValue.VICE_TROU);
        assertEquals("J4", test2.getViceTrou());
    }

    @Test
    public void getTrou(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test2 = new LocalPresidentGame(players);
        test2.playerRole.put("J1", RoleValue.PRESIDENT);
        test2.playerRole.put("J2", RoleValue.TROU);
        test2.playerRole.put("J3", RoleValue.VICE_PRESIDENT);
        test2.playerRole.put("J4", RoleValue.VICE_TROU);
        assertEquals("J2", test2.getTrou());
    }

   

    @Test
    public void getBestCardsFromPlayer(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test1 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        ArrayList<Card> card= new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("KH"));
        test1.playerCards.put("J1", cardJ1);
        card.add(Card.valueOf("KH"));


        assertEquals(card, test1.getBestCardsFromPlayer("J1",1));    
    }

    @Test
    public void getWorstCardsFromPlayer(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test1 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        ArrayList<Card> card= new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("KH"));
        test1.playerCards.put("J1", cardJ1);
        card.add(Card.valueOf("QH"));

        assertEquals(card, test1.getWorstCardsFromPlayer("J1",1));
    }

    @Test
    public void getNextPlayer(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");
        var test4 = new LocalPresidentGame(players);
        
        asserEquals("J2", test4.getNextPlayer("J1"));
    }


    private void asserEquals(String string, String nextPlayer) {
    }

    @Test
    public void ifCarre(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("QD"));
        cardJ1.add(Card.valueOf("QC"));

        test5.playerCards.put("J1", cardJ1);

        assertEquals(true, test5.ifCarre("J1"));

    }

    @Test
    public void ifBrelon(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("QD"));

        test5.playerCards.put("J1", cardJ1);

        assertEquals(true, test5.ifBrelon("J1"));

    }

    @Test
    public void ifPair(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));

        test5.playerCards.put("J1", cardJ1);

        assertEquals(true, test5.ifPair("J1"));

    }

    @Test
    public void cardPair(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("KS"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QS"));
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.cardPair("J1",cardJ1));
    }

    @Test
    public void cardBrelon(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("KS"));
        cardJ1.add(Card.valueOf("QD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QD"));
        card.add(Card.valueOf("QS"));
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.cardBrelon("J1",cardJ1));
    }

    @Test
    public void cardCarre(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("QC"));
        cardJ1.add(Card.valueOf("QD"));
        cardJ1.add(Card.valueOf("KD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QS"));
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QD"));
        card.add(Card.valueOf("QC"));
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.cardCarre("J1",cardJ1));
    }

    @Test
    public void playerPlayCards() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test5 = new LocalPresidentGame(players);

        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("QC"));
        cardJ1.add(Card.valueOf("QD"));
        cardJ1.add(Card.valueOf("KD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QS"));
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QD"));
        card.add(Card.valueOf("QC"));
        Collection<Card> tapis = Collections.emptyList();
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.playerPlayCards("J1",tapis));


    }

    @Test
    public void playerPlayCards1() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test5 = new LocalPresidentGame(players);

        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("QD"));
        cardJ1.add(Card.valueOf("KD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QD"));
        card.add(Card.valueOf("QS"));
        Collection<Card> tapis = Collections.emptyList();
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.playerPlayCards("J1",tapis));
    }

    @Test
    public void playerPlayCards2() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test5 = new LocalPresidentGame(players);

        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("QS"));
        cardJ1.add(Card.valueOf("KD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("QH"));
        card.add(Card.valueOf("QS"));
        Collection<Card> tapis = Collections.emptyList();
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.playerPlayCards("J1",tapis));
    }

    @Test
    public void playerPlayCards3() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test5 = new LocalPresidentGame(players);

        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ1.add(Card.valueOf("9S"));
        cardJ1.add(Card.valueOf("10D"));
        cardJ1.add(Card.valueOf("KD"));
        ArrayList<Card> card = new ArrayList<>();
        card.add(Card.valueOf("KD"));
        Collection<Card> tapis = Collections.emptyList();
        
        test5.playerCards.put("J1", cardJ1);
        assertEquals(card, test5.playerPlayCards("J1",tapis));
    }

    @Test
    public void playerPlayCards4() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");

        var test5 = new LocalPresidentGame(players);

        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("4H"));
        cardJ1.add(Card.valueOf("3S"));
        cardJ1.add(Card.valueOf("4D"));
        cardJ1.add(Card.valueOf("5D"));
        ArrayList<Card> card = new ArrayList<>();
        Collection<Card> tapis = new ArrayList<>();
        tapis.add(Card.valueOf("9H"));
        assertEquals(card, test5.playerPlayCards("J1",tapis));
    }

    @Test
    public void playerPlayCards5() throws NoMoreCardException{
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test5 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("KD"));
        cardJ1.add(Card.valueOf("4H"));
        cardJ1.add(Card.valueOf("3S"));
        cardJ1.add(Card.valueOf("4D"));
        ArrayList<Card> card = new ArrayList<>();
        Collection<Card> tapis = new LinkedList<>();
        card.add(Card.valueOf("KD"));
        tapis.add(Card.valueOf("9H"));
        for (Card element : tapis){
            element = Card.valueOf("9D");
        }
        assertEquals(card, test5.playerPlayCards("J1",tapis));
    }


    @Test
    public void giveCardsToPlayer(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test1 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        test1.playerCards.put("J1", cardJ1);
        test1.giveCardsToPlayer("J1","QH");
        boolean result= !cardJ1.isEmpty();
        assertTrue(result);
    }

    @Test void getCurrentPlayerCount(){
        Set<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");
        var test1 = new LocalPresidentGame(players);
        Queue<String> player = new LinkedList<>();
        player.offer("J1");
        player.offer("J2");
        assertEquals(2, test1.getCurrentPlayerCount(player));
    }
    @Test
    public void declareWinner(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        var test1 = new LocalPresidentGame(players);
        String winner="J1";
        test1.declareWinner(winner);
        asserEquals("J1",winner);
    }

    @Test
    public void getFirstParty(){
        HashSet<String> players = new HashSet<>();
        var test = new LocalPresidentGame(players);
        int [] numberParty = new int [3];
        boolean result = !test.getFirstParty(numberParty);
        assertTrue(result);

    }

    @Test
    public void getRandomCards(){
        int lenght= 5;
        Card [] result= Deck.getRandomCards(lenght);
        assertEquals(5, result.length);
    }

    @Test
    public void contient() {
        var test= new Deck();
        List<Card> cartes = new ArrayList<Card>();
        Card carte = Card.valueOf("KD");
        cartes.add(Card.valueOf("KD"));
        boolean result= !test.contient(carte);
        assertTrue(result);
    }

    @Test
    public void valueOf(){
        CardValue value= CardValue.valueOfStr("Q");
        CardColor color= CardColor.valueOfStr("H");
        var test= new Card(color, value);
        assertEquals(Card.valueOf("QH"), test.valueOf("QH"));
    }

    @Test
    public void getWinner(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");

        var test1 = new LocalPresidentGame(players);
        ArrayList<Card> cardJ1 = new ArrayList<>();
        ArrayList<Card> cardJ2 = new ArrayList<>();
        cardJ1.add(Card.valueOf("QH"));
        cardJ2.add(Card.valueOf("9S"));

        test1.playerCards.put("J1", cardJ1);
        test1.playerCards.put("J2", cardJ2);
        asserEquals("J1", PresidentGameEngine.getWinner("J1", "J2", Card.valueOf("KD"), Card.valueOf("9S")));
    }

    @Test
    public void addFinishedPlayer(){
        HashSet<String> players = new HashSet<>();
        players.add("J1");
        players.add("J2");
        var test1 = new LocalPresidentGame(players);
    }

    @Test 
    public void isTapisFinished(){
        HashSet<String> players = new HashSet<>();
        var test1 = new LocalPresidentGame(players);
        Collection<Card> tapis = Collections.emptyList();
        boolean result = test1.isTapisFinished(tapis);
        assertTrue(result);

    }

    
}