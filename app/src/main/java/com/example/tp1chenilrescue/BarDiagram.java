package com.example.tp1chenilrescue;

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

import com.example.tp1chenilrescue.models.Chien;

import java.util.ArrayList;

/**
 * @author Kevin Teasdale-Dubé
 */
public class BarDiagram extends View {

    private int[] mValues;
    private int canvasHeight;
    private float GraphTop;

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
        canvasHeight = canvas.getHeight();
        final Paint paint = new Paint();
        drawHorizontalLinesInGraph( canvas );
        drawTextOnGraph(canvas);
        drawSideLinesOnGraph(canvas, paint);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        for(int i = 0; i<mValues.length; i++) {
            canvas.drawRect(
                    new Rect((i*200)+160,
                            canvasHeight-(mValues[i] * (canvasHeight/20)) -50,
                            (i*200) + 100,canvasHeight -50), paint
            );
        }
    }

    private void drawSideLinesOnGraph(Canvas canvas, Paint paint) {
        // ligne verticale de gauche
        canvas.drawLine( 50f, canvasHeight - 20 ,50f, GraphTop , paint);

        // ligne verticale de droite
        canvas.drawLine( canvas.getWidth()- 1, canvasHeight - 20, canvas.getWidth() -1, GraphTop, paint );
    }

    private void drawTextOnGraph(Canvas canvas) {
        float distBetweenLines = (float) canvasHeight / 20;
        Paint paint = new Paint(  );
        paint.setTextSize( 35 );

        drawSideText(canvas,distBetweenLines, paint);
        drawBottomText(canvas, paint);
        drawTitleToGraph(canvas, paint, distBetweenLines);
    }

    private void drawTitleToGraph(Canvas canvas, Paint paint, float distanceBetweenLine) {
        int posX = (int)((canvas.getWidth() / 2) - ((paint.descent() + paint.ascent()) / 2));
        paint.setTextSize( 50 );
        paint.setTextAlign( Paint.Align.CENTER );
        canvas.drawText( "Nombre de chiens par tranches d'âge",posX,
                canvasHeight - (13 * distanceBetweenLine),paint );
    }

    private void drawSideText(Canvas canvas, float distBetweenLines, Paint paint) {
        for (int x = 0; x < 12; x++){
            canvas.drawText( String.valueOf( x ) ,0, (canvasHeight - (x * distBetweenLines) - 55),paint );
        }
    }

    private void drawBottomText(Canvas canvas, Paint paint) {
        String[] bottomLineText = {"0 ans", "1-2 ans", "2-4 ans", "4-8 ans", "8 ans et +"};
        for (int x = 0; x < bottomLineText.length;x++){
            canvas.drawText( bottomLineText[x] ,(x *200)+70, (canvasHeight -5),paint );
        }
    }

    private void drawHorizontalLinesInGraph(Canvas canvas){
        Paint paint = new Paint(  );
        paint.setColor( Color.GRAY );
        int canvasHeight = canvas.getHeight();
        float distBetweenLines = (float) canvasHeight / 20;

        for (int x = 0; x < 12; x++){
            canvas.drawLine( 25f, canvasHeight - (x *distBetweenLines) -50,canvas.getWidth(),
                    canvasHeight - (x *distBetweenLines) - 50, paint);
            if (x == 11){
                GraphTop = canvasHeight - (x *distBetweenLines) - 50;
            }
        }
    }

}
