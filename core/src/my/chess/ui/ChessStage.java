package my.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import my.chess.api.ChessBoard;
import my.chess.api.ChessBoardChangeListener;
import my.chess.api.ChessPiece.ChessPieceType;
import my.chess.logic.ChessUIController;

public class ChessStage implements Screen, ChessBoardChangeListener {

	
	Stage stage;
	Skin skin;
	
	Texture background;
	
	Texture pawnTex_w,	 pawnTex_b;
	Texture rookTex_w,	 rookTex_b;
	Texture queenTex_w,	 queenTex_b;
	Texture kingTex_w,	 kingTex_b;
	Texture bishopTex_w, bishopTex_b;
	Texture knightTex_w, knightTex_b;
	
	Image pawn_w,	 pawn_b;
	Image rook_w,	 rook_b;
	Image queen_w,	 queen_b;
	Image king_w,	 king_b;
	Image bishop_w, bishop_b;
	Image knight_w, knight_b;
	
	private float boardPadX = 100;
	private float boardPadY = 100;
	private float boardSize = 960;
	private float chessPieceSize = 100;
	
	SpriteBatch batch;
	
	private ChessUIController controller;
	
	public ChessStage(ChessUIController controller)
	{
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		batch = new SpriteBatch();
		background = new Texture("chessboard.png");
		this.controller = controller;
		if(controller != null)
			controller.GetChessBoard().AddChangeListener(this);
		
		loadChessPieces();
		
		Gdx.gl.glViewport(0, 0, 1280, 960);
	}
	

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0, 800, 800);
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void updateChessPieces()
	{
		ChessBoard cb = controller.GetChessBoard();
		
		hideAllPieces();
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				// Check that we've got a chesspiece here
				if(cb.GetChessPieceAt(x, y) == null) continue;
				
				Image tmp = null;
				
				// Find out which one
				switch(cb.GetChessPieceAt(x, y).GetPieceType())
				{
				case Bishop:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = bishop_w;
					else
						tmp = bishop_b;
					break;
				case King:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = king_w;
					else
						tmp = king_b;
					break;
				case Knight:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = knight_w;
					else
						tmp = knight_b;
					break;
				case Pawn:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = pawn_w;
					else
						tmp = pawn_b;
					break;
				case Queen:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = queen_w;
					else
						tmp = queen_b;
					break;
				case Rook:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = rook_w;
					else
						tmp = rook_b;
					break;
				default:
					break;
				}
				
				tmp.setPosition(x * chessPieceSize + boardPadX, y * chessPieceSize + boardPadY);
				tmp.setVisible(true);
			}
		}
	}

	private void hideAllPieces()
	{
		//if(false) {
		pawn_w.setVisible(false);
		rook_w.setVisible(false);
		queen_w.setVisible(false);
		king_w.setVisible(false);
		bishop_w.setVisible(false);
		knight_w.setVisible(false);
		//}

		pawn_b.setVisible(false);
		rook_b.setVisible(false);
		queen_b.setVisible(false);
		king_b.setVisible(false);
		bishop_b.setVisible(false);
		knight_b.setVisible(false);
		
	}
	
	/**
	 * Loads textures, creates actors and adds them to the stage
	 * <br> We hide them since their position is not set yet
	 */
	private void loadChessPieces()
	{
		
		pawnTex_w = new Texture("white_pawn.png");//"chess_piece_2_white_pawn.png");
		rookTex_w = new Texture("white_rook.png");//"chess_piece_2_white_rook.png");
		queenTex_w = new Texture("white_queen.png");//"chess_piece_2_white_queen.png");
		kingTex_w = new Texture("white_king.png");//"chess_piece_2_white_king.png");
		bishopTex_w = new Texture("white_bishop.png");//"chess_piece_2_white_bishop.png");
		knightTex_w = new Texture("white_knight.png");//"chess_piece_2_white_knight.png");
	
		pawnTex_b = new Texture("black_pawn.png");//"chess_piece_2_black_pawn.png");
		rookTex_b = new Texture("black_rook.png");//"chess_piece_2_black_rook.png");
		queenTex_b = new Texture("black_queen.png");//"chess_piece_2_black_queen.png");
		kingTex_b = new Texture("black_king.png");//"chess_piece_2_black_king.png");
		bishopTex_b = new Texture("black_bishop.png");//"chess_piece_2_black_bishop.png");
		knightTex_b = new Texture("black_knight.png");//"chess_piece_2_black_knight.png");
		
		pawnTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rookTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		queenTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		kingTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bishopTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		knightTex_w.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		pawnTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rookTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		queenTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		kingTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bishopTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		knightTex_b.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		pawn_w = new Image(pawnTex_w);	pawn_w.setSize(chessPieceSize, chessPieceSize);
		rook_w = new Image(rookTex_w);	rook_w.setSize(chessPieceSize, chessPieceSize);
		queen_w = new Image(queenTex_w);queen_w.setSize(chessPieceSize, chessPieceSize);
		king_w = new Image(kingTex_w);	king_w.setSize(chessPieceSize, chessPieceSize);
		bishop_w = new Image(bishopTex_w);bishop_w.setSize(chessPieceSize, chessPieceSize);
		knight_w = new Image(knightTex_w);knight_w.setSize(chessPieceSize, chessPieceSize);
	
		pawn_b = new Image(pawnTex_b);
		rook_b = new Image(rookTex_b);
		queen_b = new Image(queenTex_b);
		king_b = new Image(kingTex_b);
		bishop_b = new Image(bishopTex_b);
		knight_b = new Image(knightTex_b);
		
		stage.addActor(pawn_w);  
		stage.addActor(rook_w);  
		stage.addActor(queen_w); 
		stage.addActor(king_w);  
		stage.addActor(bishop_w); 
		stage.addActor(knight_w); 
		
		stage.addActor(pawn_b);  
		stage.addActor(rook_b);  
		stage.addActor(queen_b); 
		stage.addActor(king_b);  
		stage.addActor(bishop_b); 
		stage.addActor(knight_b); 
		
		hideAllPieces();
	}
	
	@Override public void show() { Gdx.input.setInputProcessor(stage); }
	@Override public void OnChessBoardChange() { updateChessPieces(); }
	
	@Override
	public void dispose() {
		background.dispose();
		batch.dispose();
		
		// Dispose of all the chesspiece textures when not needed anymore
		pawnTex_w.dispose();
		rookTex_w.dispose();
		queenTex_w.dispose();
		kingTex_w.dispose();
		bishopTex_w.dispose();
		knightTex_w.dispose();
		
		pawnTex_b.dispose();
		rookTex_b.dispose();
		queenTex_b.dispose();
		kingTex_b.dispose();
		bishopTex_b.dispose();
		knightTex_b.dispose();
		
		if(controller != null)
			controller.GetChessBoard().RemoveChangeListener(this);
		
		stage.dispose();
	}
	
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}
	

}
