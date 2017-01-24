package in.skonda.qrcodesacannerexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.R.attr.width;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CodeBuilderActivity extends AppCompatActivity {
    public final static int WIDTH = 500;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    String type;
    ImageView iv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_builder);
        Intent i = getIntent();
        type = i.getStringExtra("tag");
        et = (EditText) findViewById(R.id.et);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void gen(View view) {
        Bitmap bitmap = encodeAsBitmap(et.getText().toString());
        iv.setImageBitmap(bitmap);
    }

    private Bitmap encodeAsBitmap(String str) {
        BitMatrix result = null;
        try {
            switch (type) {
                case "qr":
                    result = new MultiFormatWriter().encode(str,
                            BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
                    break;
                case "br":
                    result = new MultiFormatWriter().encode(str,
                            BarcodeFormat.CODE_128, WIDTH, 200, null);
                    break;
            }
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
}
