package my.chess.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import my.chess.api.ChessBoard;
import my.chess.logic.ChessUIController;

public class ChessStage implements Screen {

	
	Stage stage;
	Skin skin;
	
	Texture chessboard;
	Image cb;
	Texture background;
	Image bg;
	
	Label turnText;
	
	Texture pawnTex_w,	 pawnTex_b;
	Texture rookTex_w,	 rookTex_b;
	Texture queenTex_w,	 queenTex_b;
	Texture kingTex_w,	 kingTex_b;
	Texture bishopTex_w, bishopTex_b;
	Texture knightTex_w, knightTex_b;
	
	private float boardPadX = 272; // 32 + 240
	private float boardPadY = 32;
	private float boardEdgeSize = 32;
	private float chessPieceSize = 92;
	
	private ChessUIController controller;
	
	
	public ChessStage(ChessUIController controller)
	{
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();

		chessboard = new Texture("chessboard.png");
		background = new Texture("game_bg.png");
		bg = new Image(background);
		bg.setZIndex(0);
		bg.setSize(1280, 960);
		stage.addActor(bg);
		
		turnText = new Label("", skin);
		turnText.setPosition(640, 910);
		turnText.setFontScale(3.0f);
		turnText.setColor(1,1,1,1);
		turnText.setAlignment(Align.center);
		turnText.setVisible(true);
		turnText.setZIndex(1);
		stage.addActor(turnText);
		
		cb = new Image(chessboard);
		cb.setSize(800, 800);
		cb.setPosition(240, 0);
		// 1280 - 800 = 480 (240*2)
		
		cb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				chessboardClick(x, y);
			}
		});
		stage.addActor(cb);
		
		this.controller = controller;
		
		loadChessPieces();
		
		Gdx.gl.glViewport(0, 0, 1280, 960);
	}
	

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		drawChessPieces();
		
		controller.Update();
		
		drawLegalMoves();
	}
	
	ShapeRenderer r = new ShapeRenderer();
	
	private void drawLegalMoves()
	{
		ArrayList<int[]> moves = controller.GetLegalMovesForSelection();
		
		if(moves == null || moves.size() == 0) return;
		
		r.setAutoShapeType(true);
		r.begin(ShapeType.Filled);
		r.setColor(.2f, .8f, .2f, 1f);
		
		for(int[] pos : moves)
		{
			r.ellipse(pos[0] * chessPieceSize + boardPadX + 0.5f * chessPieceSize - 12, pos[1] * chessPieceSize + boardPadY + 0.5f * chessPieceSize - 12, 32, 32);
		}
		
		r.end();
	}

	private void drawChessPieces()
	{
		ChessBoard cb = controller.GetChessBoard();
		
		stage.getBatch().begin();
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				// Check that we've got a chesspiece here
				if(cb.GetChessPieceAt(x, y) == null) continue;
				
				Texture tmp = null;
				
				// Find out which one
				switch(cb.GetChessPieceAt(x, y).GetPieceType())
				{
				case Bishop:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = bishopTex_w;
					else
						tmp = bishopTex_b;
					break;
				case King:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = kingTex_w;
					else
						tmp = kingTex_b;
					break;
				case Knight:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = knightTex_w;
					else
						tmp = knightTex_b;
					break;
				case Pawn:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = pawnTex_w;
					else
						tmp = pawnTex_b;
					break;
				case Queen:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = queenTex_w;
					else
						tmp = queenTex_b;
					break;
				case Rook:
					if(cb.GetChessPieceAt(x, y).isPieceWhite())
						tmp = rookTex_w;
					else
						tmp = rookTex_b;
					break;
				default:
					break;
				}
				
				// Render it at it's grid position
				
				float tmpX = x * chessPieceSize + boardPadX;
				float tmpY = y * chessPieceSize + boardPadY;
				
				stage.getBatch().draw(tmp, tmpX, tmpY, chessPieceSize, chessPieceSize);
			}
		}
		stage.getBatch().end();
	}
	
	
	private void chessboardClick(float x, float y)
	{
		// Convert to board coordinates(indices)
		
		x -= boardEdgeSize;
		y -= boardEdgeSize;
		
		// Divide position with the size of the playing field
		float tmp = chessPieceSize * 7.0f;
		x /= tmp;
		y /= tmp;
		
		// x and y are 0-1 floats in the playing field, convert to index 0-7
		int boardX = (int)Math.floor(x * 7.0f);
		int boardY = (int)Math.floor(y * 7.0f);
		
		//System.out.println("Final calculated board position is "+boardX+","+boardY);

		if(boardX >= 0 && boardX < 8 && boardY >= 0 && boardY < 8)
			controller.ChessboardClick(boardX, boardY);
		else
			controller.AbortSelection();
	
	}
	

	/**
	 * Loads textures, creates actors and adds them to the stage
	 * <br> We hide them since their position is not set yet
	 */
	private void loadChessPieces()
	{
		
		pawnTex_w = new Texture("white_pawn.png");
		rookTex_w = new Texture("white_rook.png");
		queenTex_w = new Texture("white_queen.png");
		kingTex_w = new Texture("white_king.png");
		bishopTex_w = new Texture("white_bishop.png");
		knightTex_w = new Texture("white_knight.png");
	
		pawnTex_b = new Texture("black_pawn.png");
		rookTex_b = new Texture("black_rook.png");
		queenTex_b = new Texture("black_queen.png");
		kingTex_b = new Texture("black_king.png");
		bishopTex_b = new Texture("black_bishop.png");
		knightTex_b = new Texture("black_knight.png");
		
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
	
	}
	
	public void SetTurnText(String text)
	{
		turnText.setText(text);
	}
	
	@Override public void show() { Gdx.input.setInputProcessor(stage); }
	
	@Override
	public void dispose() {
		chessboard.dispose();
		
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
		
		stage.dispose();
	}
	
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}
	

}
