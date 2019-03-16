package com.example.tp1chenilrescue.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.tp1chenilrescue.R;

/**
 * @author Kevin Teasdale-Dubé
 */
public class BarDiagram extends View {

    private int[] mValues;
    private int canvasHeight;
    private float GraphTop;
    private int numberOfDog;
    private float distBetweenLines;

    public BarDiagram(Context context) {
        super( context );
    }

    public BarDiagram(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.barchart,
                0, 0);

        //Resources res = getResources();
        //mValues = res.getIntArray(R.array.barchart_values);
        a.recycle();
    }

    public void setValeurs(int[] chiens) {
        mValues = chiens;
        invalidate();
        requestLayout();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        numberOfDog = mValues[5];
        canvasHeight = getHeight();
        distBetweenLines = (float) canvasHeight / 15;
        final Paint paint = new Paint();
        drawHorizontalLinesInGraph( canvas );
        drawTextOnGraph(canvas);
        drawSideLinesOnGraph(canvas, paint);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        for(int i = 0; i<mValues.length - 1; i++) {
            canvas.drawRect(
                    new Rect((i*200)+160,
                            canvasHeight- (int)(getPercentage( mValues[i] ) * (distBetweenLines * 10)) -50,
                            (i*200) + 100,canvasHeight -50), paint
            );
        }
    }

    /**
     * Méthode qui calcul le pourcentage de chien possédant un age spécifique.
     *
     * @param nbrOfDogInThisAge Qte de chien ayant un age spécifique.
     * @return le pourcentage en float.
     */
    private float getPercentage(int nbrOfDogInThisAge){
        return ((float) nbrOfDogInThisAge / (float) numberOfDog);
    }

    /**
     * Méthode qui dessine les lignes verticales (de chaque cotés) du diagramme.
     *
     * @param canvas Canvas servant de toile.
     * @param paint Ce qui sert à dessiner.
     */
    private void drawSideLinesOnGraph(Canvas canvas, Paint paint) {
        // ligne verticale de gauche
        canvas.drawLine( 60f, canvasHeight - 20 ,60f, GraphTop , paint);

        // ligne verticale de droite
        canvas.drawLine( canvas.getWidth()- 1, canvasHeight - 20, canvas.getWidth() -1, GraphTop, paint );
    }

    /**
     * Méthode qui dessine tout le texte qui est requis dans le diagramme (appel d'autres méthodes private)
     *
     * @param canvas Canvas passer par la Méthode onDraw().
     */
    private void drawTextOnGraph(Canvas canvas) {
        Paint paint = new Paint(  );
        paint.setTextSize( 35 );

        drawSideText(canvas, paint);
        drawBottomText(canvas, paint);
        drawTitleToGraph(canvas, paint);
    }

    /**
     * Méthode qui écrit le titre au centre du diagramme.
     *
     * @param canvas Canvas passer par la Méthode drawTextOnGraph().
     * @param paint ce qui servira à écrire sur le canvas.
     */
    private void drawTitleToGraph(Canvas canvas, Paint paint) {
        int posX = (int)((canvas.getWidth() / 2) - ((paint.descent() + paint.ascent()) / 2));
        paint.setTextSize( 50 );
        paint.setTextAlign( Paint.Align.CENTER );
        canvas.drawText( "Pourcentage de chiens par tranches d'âge",posX,
                canvasHeight - (12 * distBetweenLines),paint );
    }

    /**
     *Méthode qui écrit la gradation du % à gauche du diagramme.
     *
     * @param canvas canvas qui sera "dessiné" dessus.
     * @param paint ce qui servira à écrire sur le canvas.
     */
    private void drawSideText(Canvas canvas, Paint paint) {
        paint.setTextSize( 30 );
        for (int x = 0; x < 11; x++){
            canvas.drawText( String.valueOf( x * 10 ) ,0, (canvasHeight - (x * distBetweenLines) - 55),paint );
        }
    }

    /**
     * Méthode qui écrit les tranches d'âge sur l'axe des X.
     * @param canvas canvas qui sera "dessiné" dessus.
     * @param paint ce qui servira à écrire sur le canvas.
     */
    private void drawBottomText(Canvas canvas, Paint paint) {
        String[] bottomLineText = {"0 ans", "1-2 ans", "2-4 ans", "4-8 ans", "8 ans et +"};
        for (int x = 0; x < bottomLineText.length;x++){
            canvas.drawText( bottomLineText[x] ,(x *200)+80, (canvasHeight -5),paint );
        }
    }

    /**
     * Méthode qui trace les lignes de gradation dans le diagramme.
     * @param canvas canvas qui sera "dessiné" dessus.
     */
    private void drawHorizontalLinesInGraph(Canvas canvas){
        Paint paint = new Paint(  );
        paint.setColor( Color.GRAY );


        for (int x = 0; x < 11; x++){
            canvas.drawLine( 25f, canvasHeight - (x *distBetweenLines) -50,canvas.getWidth(),
                    canvasHeight - (x *distBetweenLines) - 50, paint);
            if (x == 10){
                GraphTop = canvasHeight - (x *distBetweenLines) - 50;
            }
        }
    }

}
