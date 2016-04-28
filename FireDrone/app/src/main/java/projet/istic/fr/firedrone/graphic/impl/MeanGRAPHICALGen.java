package projet.istic.fr.firedrone.graphic.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

import projet.istic.fr.firedrone.graphic.BITMAPGen;
import projet.istic.fr.firedrone.model.MeansItem;


/**
 * @author Group A
 * Generates BITMAP reprezenting MEAN
 */
public class MeanGRAPHICALGen implements BITMAPGen {


    //    Static Attributes

    private static final int PADDING = 42;



    //    Attributes

    private Rect border;

    private String name;

    private String type;

    private String fullNAME;

    private String color;


    //    Default Constructor
    public MeanGRAPHICALGen() {
    }

    //    Parameter Attributes
    public MeanGRAPHICALGen(String type, String name, String colorHexa) {
        this.border = new Rect();
        this.name = name;
        this.type = type;
        this.color = colorHexa;
        this.fullNAME = name;
    }

    //    Getter & Setter
    /** ...  TODO: DEFINES GETTER AND SETTER IF NEEDED **/

    @Override
    public Bitmap getBitMap(MeansItem mean) {

        Canvas canvas = new Canvas();
        /**   Draw Text  **/
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(color == null){
            color = "#000000";
        }
        textPaint.setColor(Color.parseColor(color));

        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);

        float[] text = new float[fullNAME.length()];

        Rect myBorder = new Rect();

        textPaint.getTextBounds(fullNAME, 0, fullNAME.length(), myBorder);

        int borderLeft = 0;
        int borderTop = 0;
        int borderRight = myBorder.right;
        int borderBottom = myBorder.bottom+ Math.abs(myBorder.top) ;

        /* */
        Bitmap bitmap = Bitmap.createBitmap(borderRight + PADDING, borderBottom + PADDING, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        myBorder.set(borderLeft, borderTop, borderRight + PADDING, borderBottom + PADDING);

        canvas.drawText(fullNAME, 0, fullNAME.length(), borderRight/2+PADDING/2, borderBottom+PADDING/2, textPaint);

        /**   Draw Border  **/
        Paint dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashPaint.setColor(Color.parseColor(color));

        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(8);
        if(mean.getMsMeanHEngaged() == null || mean.isRedeployement()) {
            dashPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        }

        canvas.drawRect(myBorder, dashPaint);

        canvas.save();

        return bitmap;
    }




}
