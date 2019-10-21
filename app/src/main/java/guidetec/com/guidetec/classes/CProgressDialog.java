package guidetec.com.guidetec.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import guidetec.com.guidetec.R;

public class CProgressDialog extends ProgressDialog {
  private AnimationDrawable animation;

  public CProgressDialog(Context context) {
    super(context);
  }

  public static ProgressDialog ctor(Context context) {
    CProgressDialog dialog = new CProgressDialog(context);
    dialog.setIndeterminate(true);
    dialog.setCancelable(false);
    return dialog;
  }


  public CProgressDialog(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.progress_dialog);

    //ImageView la = (ImageView) findViewById(R.id.iv_frame_loading);
   // la.setBackgroundResource(R.drawable.loaging_ring);
    //animation = (AnimationDrawable) la.getBackground();
  }

  @Override
  public void show() {
    super.show();
    //animation.start();
  }

  @Override
  public void dismiss() {
    super.dismiss();
    //animation.stop();
  }
}
