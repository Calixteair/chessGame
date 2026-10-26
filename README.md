# chessGame

Moteur de règles du jeu d'échecs, utilisé par le serveur.

- `ChessGame` : point d'entrée. `play(Move)` refuse tout coup illégal ; l'état expose les coups légaux, l'échec, le statut (`GameStatus`) et le vainqueur.
- Règles complètes : coups légaux (aucun coup ne laisse son roi en échec), roque (droits, cases traversées non attaquées), prise en passant, promotion au choix, échec et mat, pat, nulles par 50 coups, triple répétition et matériel insuffisant, abandon.
- `Board` : position complète (équivalent FEN) ; `Fen`, `Notation` (SAN) et `Move` (UCI) pour les échanges.

Repère : `x` = colonne (0 = a), `y` = ligne affichée (0 = rangée 8, 7 = rangée 1).

```bash
mvn test      # 36 tests, dont perft de référence (chessprogramming.org)
mvn install   # requis avant de construire chessServer
```
