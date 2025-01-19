package Entity.UI.Other;

import Entity.UI.Deck;
import Main.Game;

public class Shop {
    Deck deck;
    Game game;
    public Shop(Game game) {
        this.game = game;
        deck = game.deck;
    }
}
