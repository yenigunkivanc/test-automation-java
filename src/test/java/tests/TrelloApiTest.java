package tests;

import api.clients.BoardApi;
import api.clients.CardApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class TrelloApiTest {

    @Test
    void trelloScenarioTest() {

        BoardApi boardApi = new BoardApi();
        CardApi cardApi = new CardApi();

        // 1) Board oluştur
        String boardId = boardApi.createBoard("Automation Board - Kivanc");

        // 2) Üyeleri davet et (ödevde mail verilmiş)
        boardApi.inviteMemberByEmail(boardId, "batuhan.zafer@testinium.com");
        boardApi.inviteMemberByEmail(boardId, "tam@testinium.com");

        // 3) Board listelerini çek → ilk list'i kullan
        List<String> listIds = boardApi.getListIdsOnBoard(boardId);
        Assertions.assertFalse(listIds.isEmpty(), "Board listesi boş geldi!");
        String listId = listIds.get(0);
        System.out.println("Kartlar bu listeye eklenecek: " + listId);

        // 4) 2 kart oluştur
        String card1 = cardApi.createCard(listId, "Card 1 - Automation");
        String card2 = cardApi.createCard(listId, "Card 2 - Automation");

        // 5) Random 1 kart seç (log bas)
        String selected = new Random().nextBoolean() ? card1 : card2;
        System.out.println("Random seçilen kart id: " + selected);

        // 6) Kartları sil
        cardApi.deleteCard(card1);
        cardApi.deleteCard(card2);

        // 7) Board sil
        boardApi.deleteBoard(boardId);

        System.out.println("Trello API senaryosu tamamlandı (cleanup ok).");
    }
}
