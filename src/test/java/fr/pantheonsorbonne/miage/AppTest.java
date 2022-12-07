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

    
}