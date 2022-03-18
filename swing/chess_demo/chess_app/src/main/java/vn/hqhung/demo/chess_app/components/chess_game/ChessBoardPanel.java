package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.chess_app.models.Castling;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.common.models.GameSetting;
import vn.hqhung.demo.chess_app.models.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @System: demo
 * @Title: Chess Board
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class ChessBoardPanel extends JPanel {
    public static final int BOARD_SIZE = 510;
    public static final int TOP_BOARD = 0;
    public static final int BOTTOM_BOARD = 7;
    public static final int CELL_SIZE = 60;

    private static final Image IMAGE_BACKGROUND_IMAGE = ResourceHelper.loadImage("ChessBoard.png").getScaledInstance(CELL_SIZE * 8, CELL_SIZE * 8, 0);
    private static final Image SELECT_CELL_IMAGE = ResourceHelper.loadImage("Possible.png").getScaledInstance(CELL_SIZE, CELL_SIZE, 0);
    private static final Image ABLE_MOVE_CELL_IMAGE = ResourceHelper.loadImage("Last.png").getScaledInstance(CELL_SIZE, CELL_SIZE, 0);

    public final ChessCell[][] cells = new ChessCell[8][8];

    public ChessCell activeCell;

    public GameSetting gameSetting;

    public MoveHistoryTable moveHistory;

    private int activeXCell;
    private int activeYCell;

    public King kingWhite;
    public King kingBlack;
    public Pawn pawnMovedTwoCell = null;
    public Pawn twoCellMovedPawn2 = null;


    private Image upDownLabel = null;
    private Image leftRightLabel = null;
    private final Point topLeft = new Point(0, 0);
    private final ChessGamePanel chessGamePanel;

    public ChessBoardPanel(ChessGamePanel chessGamePanel, GameSetting gameSetting, MoveHistoryTable moveHistory) {
        this.chessGamePanel = chessGamePanel;
        this.gameSetting = gameSetting;
        this.moveHistory = moveHistory;

        activeCell = null;
        activeXCell = 0;
        activeYCell = 0;

        // init cell
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                cells[i][y] = new ChessCell(i, y, null);
            }
        }

        setDoubleBuffered(true); // use to draw
        setLayout(null);
        drawLabels();
    }

    public void removeAllPieces() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                cells[x][y].setChessPiece(null);
            }
        }
    }

    public void setChessPieces(String places, GamePlayer plWhite, GamePlayer plBlack) {
        if (places.equals("")) {
            setPiecesForNewGame(gameSetting.upSideDown, plWhite, plBlack);
        }
    }

    private void setPiecesForNewGame(boolean upSideDown, GamePlayer plWhite, GamePlayer plBlack) {
        GamePlayer p1 = plBlack;
        GamePlayer p2 = plWhite;

        if (upSideDown) {
            p1 = plWhite;
            p2 = plBlack;
        }

        setFiguresForNewGame(0, p1, upSideDown);
        setPawnsForNewGame(1, p1);
        setFiguresForNewGame(7, p2, upSideDown);
        setPawnsForNewGame(6, p2);
    }

    private void setFiguresForNewGame(int i, GamePlayer gamePlayer, boolean upsideDown) {
        if (i != 0 && i != 7) {
            return;
        } else if (i == 0) {
            gamePlayer.setGoDown(true);
        }

        cells[0][i].setChessPiece(new Rook(this, gamePlayer));
        cells[7][i].setChessPiece(new Rook(this, gamePlayer));
        cells[1][i].setChessPiece(new Knight(this, gamePlayer));
        cells[6][i].setChessPiece(new Knight(this, gamePlayer));
        cells[2][i].setChessPiece(new Bishop(this, gamePlayer));
        cells[5][i].setChessPiece(new Bishop(this, gamePlayer));

        if (upsideDown) {
            cells[4][i].setChessPiece(new Queen(this, gamePlayer));
            if (gamePlayer.isWhite()) {
                cells[3][i].setChessPiece(kingWhite = new King(this, gamePlayer));
            } else {
                cells[3][i].setChessPiece(kingBlack = new King(this, gamePlayer));
            }
        } else {
            cells[3][i].setChessPiece(new Queen(this, gamePlayer));
            if (gamePlayer.isWhite()) {
                cells[4][i].setChessPiece(kingWhite = new King(this, gamePlayer));
            } else {
                cells[4][i].setChessPiece(kingBlack = new King(this, gamePlayer));
            }
        }
    }

    private void setPawnsForNewGame(int i, GamePlayer gamePlayer) {
        if (i != 1 && i != 6) {
            return;
        }

        for (int x = 0; x < 8; x++) {
            cells[x][i].setChessPiece(new Pawn(this, gamePlayer));
        }
    }

    public ChessCell getCell(int x, int y) {
        if ((x > get_height()) || (y > get_width())) {
            return null;
        }

        x -= upDownLabel.getHeight(null);
        y -= upDownLabel.getHeight(null);

        double cellX = 1.0 * x / CELL_SIZE;
        double cellY = 1.0 * y / CELL_SIZE;

        if (cellX > (int) cellX) {
            cellX = (int) cellX + 1;
        }
        if (cellY > (int) cellY) {
            cellY = (int) cellY + 1;
        }

        if (cellX > 8) {
            cellX = 8;
        }
        if (cellY > 8) {
            cellY = 8;
        }

        return cells[(int) cellX - 1][(int) cellY - 1];
    }

    public void select(ChessCell cell) {
        activeCell = cell;
        activeXCell = cell.posX + 1;
        activeYCell = cell.posY + 1;

        repaint();
    }

    public void unselect() {
        activeCell = null;
        activeXCell = 0;
        activeYCell = 0;

        repaint();
    }

    public int get_width() {
        return getHeight();
    }

    public int get_height() {
        return IMAGE_BACKGROUND_IMAGE.getHeight(null) + upDownLabel.getHeight(null);
    }

    public void move(ChessCell begin, ChessCell end, ChessPiece piece) {
        move(begin, end, true, piece);
    }

    public void move(ChessCell begin, ChessCell end, boolean moveForward, ChessPiece piece) {
        Castling wasCastling = Castling.NONE;
        ChessPiece promotedPiece = piece;
        boolean wasEnPassant = false;
        if (end.getChessPiece() != null) {
            end.getChessPiece().chessCell = null;
        }

        ChessCell cellBegin = new ChessCell(begin);
        ChessCell cellEnd = new ChessCell(end);

        twoCellMovedPawn2 = pawnMovedTwoCell;

        begin.getChessPiece().chessCell = end;
        end.setChessPiece(begin.getChessPiece());
        begin.setChessPiece(null);
        ChessPiece tempPiece;
        ChessPiece chessPiece = end.getChessPiece();

        switch (chessPiece.getChessPieceType()) {
            case KING:
                if (!((King) chessPiece).wasMotion) {
                    ((King) chessPiece).wasMotion = true;
                }

                // Castling
                if (begin.posX + 2 == end.posX) {
                    move(cells[7][begin.posY], cells[end.posX - 1][begin.posY], false, null);
                    wasCastling = Castling.SHORT;
                } else if (begin.posX - 2 == end.posX) {
                    move(cells[0][begin.posY], cells[end.posX + 1][begin.posY], false, null);
                    wasCastling = Castling.LONG;
                }
                break;
            case ROOK:
                if (!((Rook) chessPiece).wasMotion) {
                    ((Rook) chessPiece).wasMotion = true;
                }
                break;
            case PAWN:
                // en passant
                if (pawnMovedTwoCell != null && cells[end.posX][begin.posY] == pawnMovedTwoCell.chessCell) {
                    tempPiece = cells[end.posX][begin.posY].getChessPiece();
                    cellEnd.setChessPiece(tempPiece);

                    cells[end.posX][begin.posY].setChessPiece(null);
                    wasEnPassant = true;
                }

                // if pawn move two cell
                if (begin.posY - end.posY == 2 || end.posY - begin.posY == 2) {
                    pawnMovedTwoCell = (Pawn) chessPiece;
                } else {
                    // remove last move of en passant
                    pawnMovedTwoCell = null;
                }

                // pawn promotion
                if ((chessPiece.chessCell.posY == 0 || chessPiece.chessCell.posY == 7)) {
                    if (!chessGamePanel.isBlockedChessboard) {
                        if (moveForward) {
                            // show dialog choose promotion
                            String newPiece = showPawnPromotionBox(chessPiece.owner.isWhite());

                            switch (newPiece) {
                                case "Queen":
                                    Queen queen = new Queen(this, chessPiece.owner);
                                    queen.chessBoard = chessPiece.chessBoard;
                                    queen.owner = chessPiece.owner;
                                    queen.chessCell = chessPiece.chessCell;
                                    end.setChessPiece(queen);
                                    break;
                                case "Rook":
                                    Rook rook = new Rook(this, chessPiece.owner);
                                    rook.chessBoard = chessPiece.chessBoard;
                                    rook.owner = chessPiece.owner;
                                    rook.chessCell = chessPiece.chessCell;
                                    end.setChessPiece(rook);
                                    break;
                                case "Bishop":
                                    Bishop bishop = new Bishop(this, chessPiece.owner);
                                    bishop.chessBoard = chessPiece.chessBoard;
                                    bishop.owner = chessPiece.owner;
                                    bishop.chessCell = chessPiece.chessCell;
                                    end.setChessPiece(bishop);
                                    break;
                                default:
                                    Knight knight = new Knight(this, chessPiece.owner);
                                    knight.chessBoard = chessPiece.chessBoard;
                                    knight.owner = chessPiece.owner;
                                    knight.chessCell = chessPiece.chessCell;
                                    end.setChessPiece(knight);
                                    break;
                            }

                            promotedPiece = chessPiece;
                        }
                    } else {
                        promotedPiece.chessBoard = chessPiece.chessBoard;
                        promotedPiece.owner = chessPiece.owner;
                        promotedPiece.chessCell = chessPiece.chessCell;
                        end.setChessPiece(promotedPiece);
                    }
                }
                break;
            default:
                pawnMovedTwoCell = null;
                break;
        }

        unselect();

        repaint();

        moveHistory.addMove(cellBegin, cellEnd, moveForward, wasCastling, wasEnPassant, promotedPiece);
    }

    public void undoMove() {
        Move last = moveHistory.undo();

        if (last != null && last.getFromChessCell() != null) {
            ChessCell begin = last.getFromChessCell();
            ChessCell end = last.getToChessCell();
            try {
                ChessPiece moved = last.getMovedChessPiece();
                cells[begin.posX][begin.posY].setChessPiece(moved);

                moved.chessCell = cells[begin.posX][begin.posY];

                ChessPiece taken = last.getTakenChessPiece();
                ChessPiece rook;
                switch (last.getCastlingMove()) {
                    case SHORT:
                        rook = cells[end.posX - 1][end.posY].getChessPiece();
                        cells[7][begin.posY].setChessPiece(rook);
                        rook.chessCell = cells[7][begin.posY];
                        cells[end.posX - 1][end.posY].setChessPiece(null);

                        ((King) moved).wasMotion = false;
                        ((Rook) rook).wasMotion = false;
                        break;
                    case LONG:
                        rook = cells[end.posX + 1][end.posY].getChessPiece();
                        cells[0][begin.posY].setChessPiece(rook);
                        rook.chessCell = cells[0][begin.posY];
                        cells[end.posX + 1][end.posY].setChessPiece(null);

                        ((King) moved).wasMotion = false;
                        ((Rook) rook).wasMotion = false;
                        break;
                    default:
                        switch (moved.getChessPieceType()) {
                            case ROOK:
                                ((Rook) moved).wasMotion = false;
                                break;
                            case PAWN:
                                if (last.isWasEnPassant()) {
                                    Pawn pawn = (Pawn) last.getTakenChessPiece();
                                    cells[end.posX][begin.posY].setChessPiece(pawn);
                                    pawn.chessCell = cells[end.posX][begin.posY];
                                } else if (last.getPromotedChessPiece() != null) {
                                    ChessPiece promoted = cells[end.posX][end.posY].getChessPiece();
                                    promoted.chessCell = null;
                                    cells[end.posX][end.posY].setChessPiece(null);
                                }
                            default:
                                break;
                        }
                        break;
                }
                // check en passant
                Move oneMoveEarlier = moveHistory.getLastMoveFromHistory();
                if (oneMoveEarlier != null && oneMoveEarlier.isWasMovePawnTwoCells()) {
                    ChessPiece canBeTakenEnPassant = cells[oneMoveEarlier.getToChessCell().posX][oneMoveEarlier.getToChessCell().posY].getChessPiece();
                    if (canBeTakenEnPassant.getChessPieceType() == ChessPieceType.PAWN) {
                        pawnMovedTwoCell = (Pawn) canBeTakenEnPassant;
                    }
                }

                if (taken != null && !last.isWasEnPassant()) {
                    cells[end.posX][end.posY].setChessPiece(taken);
                    taken.chessCell = cells[end.posX][end.posY];
                } else {
                    cells[end.posX][end.posY].setChessPiece(null);
                }

                unselect();
                repaint();
            } catch (ArrayIndexOutOfBoundsException | NullPointerException exc) {
                log.error(exc.getMessage(), exc);
            }

        }
    }

    PawnPromotionDialog promotionBox;

    public String showPawnPromotionBox(boolean isWhite) {
        JFrame frame = (JFrame) getRootPane().getParent();
        String color = isWhite ? "White" : "Black";
        if (promotionBox == null) {
            promotionBox = new PawnPromotionDialog(frame, color);
            promotionBox.setLocationRelativeTo(frame);
            promotionBox.setModal(true);
        } else {
            promotionBox.setColor(color);
        }
        promotionBox.setVisible(true);

        return promotionBox.result;
    }

    public void draw() {
        getGraphics().drawImage(IMAGE_BACKGROUND_IMAGE, getTopLeftPoint().x, getTopLeftPoint().y, this);
        drawLabels();
        repaint();
    }

    public boolean isChecked;

    @Override
    public void update(Graphics g) {
        repaint();
    }

    public Point getTopLeftPoint() {
        return new Point(topLeft.x + upDownLabel.getHeight(null), topLeft.y + upDownLabel.getHeight(null));
    }


    private List<ChessCell> cellCanMove;

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point topLeftPoint = getTopLeftPoint();


        if (topLeftPoint.x <= 0 && topLeftPoint.y <= 0) {
            drawLabels();
        }

        g2D.drawImage(upDownLabel, 0, 0, null);
        g2D.drawImage(upDownLabel, 0, IMAGE_BACKGROUND_IMAGE.getHeight(null) + topLeftPoint.y, null);

        g2D.drawImage(leftRightLabel, 0, 0, null);
        g2D.drawImage(leftRightLabel, IMAGE_BACKGROUND_IMAGE.getHeight(null) + topLeftPoint.x, 0, null);


        g2D.drawImage(IMAGE_BACKGROUND_IMAGE, topLeftPoint.x, topLeftPoint.y, this);

        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if (cells[i][y].getChessPiece() != null) {
                    cells[i][y].getChessPiece().draw(g);
                }
            }
        }

        // check if board have selected cell
        if ((activeXCell != 0) && (activeYCell != 0)) {
            //highlight selected cell
            g2D.drawImage(SELECT_CELL_IMAGE, ((activeXCell - 1) * CELL_SIZE) + topLeftPoint.x, ((activeYCell - 1) * CELL_SIZE) + topLeftPoint.y, null);
            ChessCell tmpCell = cells[activeXCell - 1][activeYCell - 1];
            if (tmpCell.getChessPiece() != null) {
                cellCanMove = cells[activeXCell - 1][activeYCell - 1].getChessPiece().getAllCellsCanMove();
            }

            if (cellCanMove != null) {
                for (ChessCell cell : cellCanMove) {
                    g2D.drawImage(ABLE_MOVE_CELL_IMAGE, (cell.posX * CELL_SIZE) + topLeftPoint.x, (cell.posY * CELL_SIZE) + topLeftPoint.y, null);
                }
            }
        }

        if (isChecked) {
            g2D.setColor(Color.RED);
            g2D.setFont(new Font("Arial", Font.BOLD, 40));
            g2D.drawString("Check", BOARD_SIZE / 2 - 50, BOARD_SIZE / 2 - 10);
        }

        g2D.dispose();
    }

    protected final void drawLabels() {
        int labelHeight = (int) Math.ceil(CELL_SIZE / 4.0);
        int labelWidth = (int) Math.ceil(CELL_SIZE * 8 + (2 * labelHeight));

        BufferedImage uDL = new BufferedImage(labelWidth + 15, labelHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D uDL2D = uDL.createGraphics();

        uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        uDL2D.drawImage(ResourceHelper.loadImage("EdgeVer.png"), 0, 0, labelWidth + 15, labelHeight, null);

        uDL2D.setColor(Color.BLACK);
        uDL2D.setFont(new Font("Arial", Font.BOLD, 16));

        int addX = CELL_SIZE / 2 + labelHeight / 2;

        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};

        if (!gameSetting.upSideDown) {
            for (int i = 1; i <= letters.length; i++) {
                uDL2D.drawString(letters[i - 1], (CELL_SIZE * (i - 1)) + addX, 10 + (labelHeight / 3));
            }
        } else {
            int j = 1;
            for (int i = letters.length; i > 0; i--, j++) {
                uDL2D.drawString(letters[i - 1], (CELL_SIZE * (j - 1)) + addX, 10 + (labelHeight / 3));
            }
        }
        uDL2D.dispose();

        upDownLabel = uDL;

        uDL = new BufferedImage(labelHeight, labelWidth + 15, BufferedImage.TYPE_4BYTE_ABGR);
        uDL2D = uDL.createGraphics();

        uDL2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        uDL2D.drawImage(ResourceHelper.loadImage("EdgeHor.png"), 0, 0, labelHeight, labelWidth + 15, null);

        uDL2D.setColor(Color.BLACK);
        uDL2D.setFont(new Font("Arial", Font.BOLD, 16));

        addX = (CELL_SIZE / 2) + labelHeight;
        if (gameSetting.upSideDown) {
            for (int i = 1; i <= 8; i++) {
                uDL2D.drawString(Integer.toString(i), (labelHeight / 3) - 1, (CELL_SIZE * (i - 1)) + addX + 5);
            }
        } else {
            int j = 1;
            for (int i = 8; i > 0; i--, j++) {
                uDL2D.drawString(Integer.toString(i), (labelHeight / 3) - 1, (CELL_SIZE * (j - 1)) + addX + 5);
            }
        }

        uDL2D.dispose();
        leftRightLabel = uDL;
    }
}
