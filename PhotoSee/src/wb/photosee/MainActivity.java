package wb.photosee;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author hualingtaizi
 *
 */
public class MainActivity extends Activity {

	private String Url = null;
	private File file = null;
	private EditText inputId = null;
	private Bitmap bitmap = null;
	private Button myButton = null;
	private ImageView photoShow = null;
    private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		
		myButton.setOnClickListener(new ButtonListener());
		photoShow.setOnClickListener(new photoListener());

	}


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    photoShow.setImageBitmap(bitmap);
                    dialog.dismiss();
                    break;
                default:
                    dialog.show();
                    break;
            }
        }
    };

    public void LoadImage(){
        Thread run = new Thread() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(Url);

                try {
                    Message msg = new Message();
                    HttpResponse response = null;
                    response = (HttpResponse) httpClient.execute(get);
                    HttpEntity entity = response.getEntity();
                    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                    InputStream instream = bufHttpEntity.getContent();
                    bitmap = BitmapFactory.decodeStream(instream);
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        run.start();

    }
	
	
	public class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!inputId.getText().toString().equalsIgnoreCase("")){
		        if (inputId.getText().toString().length() == 8){
		        	 if ((MainActivity.this.inputId.getText().toString().equalsIgnoreCase("12108525")) || (MainActivity.this.inputId.getText().toString().equalsIgnoreCase("12108502")) || (MainActivity.this.inputId.getText().toString().equalsIgnoreCase("12108501")) || (MainActivity.this.inputId.getText().toString().equalsIgnoreCase("12108504")))
		                 Toast.makeText(MainActivity.this, "涓讳汉璇磋繖浜轰綘涓嶈兘鐪嬪摝", Toast.LENGTH_SHORT).show();
		        	 else{
		        		 Url = "http://jxgl.hdu.edu.cn/readimagexs.aspx?xh=" + inputId.getText().toString() + "&lb=xsdzzcxx";
                         dialog = ProgressDialog.show(MainActivity.this, "鎻愮ず", "姝ｅ湪鑾峰彇缃戠粶绔祫鏂欙紝璇风◢绛夆�鈥�", true, false);
                         LoadImage();
		        	 	}
		        }
		   }else
			Toast.makeText(MainActivity.this, "璇峰厛杈撳叆瀛﹀彿", Toast.LENGTH_SHORT).show();
	}
		
}

private void initView(){
		inputId = (EditText)findViewById(R.id.inputId);
		myButton = (Button)findViewById(R.id.button);
		photoShow = (ImageView)findViewById(R.id.photoShow);
	}

	
	public class photoListener implements OnClickListener{
		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		if(!inputId.getText().toString().equalsIgnoreCase("")){
		    AlertDialog.Builder builder = new Builder(MainActivity.this);
		    builder.setMessage("鏄惁淇濆瓨涓烘湰鍦板浘鐗�");
		    builder.setTitle("鎻愮ず");
		    builder.setPositiveButton("鏄�", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                            try{
                                file = new File("/mnt/sdcard/photoShow");
                                if(!file.exists()){
                                    file.mkdir();
                                }
                                file = new File("/mnt/sdcard/photoShow/" + inputId.getText().toString() + ".png");
                                file.createNewFile();
                                System.out.println("0");
                            }catch(IOException e){
                                Toast.makeText(MainActivity.this, "淇濆瓨鐓х墖鏃跺彂鐢熷紓甯革紝璇烽噸鏂颁繚瀛�", Toast.LENGTH_SHORT).show();
                            }
                            FileOutputStream fileOut = null;
                            System.out.println("1");
                            try {
                                fileOut = new FileOutputStream(file);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                System.out.println("2");
                                }
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
                            try {
                                fileOut.flush();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            try {
                                fileOut.close();
                                Toast.makeText(MainActivity.this, "鐓х墖淇濆瓨瀹屾垚锛侊紒", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                        }
                    });
		builder.setNegativeButton("鍚�",  new DialogInterface.OnClickListener(){
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    // TODO Auto-generated method stub
		    dialog.dismiss();
		    }
		});
		builder.create().show();
		}else{
		        Toast.makeText(MainActivity.this, "璇峰厛杈撳叆瀛﹀彿锛侊紒", Toast.LENGTH_SHORT).show();
		    }
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
	imm.hideSoftInputFromWindow(inputId.getWindowToken(), 0); 
	// TODO Auto-generated method stub
	 if (event.getAction() == MotionEvent.ACTION_DOWN) {  
	            if (MainActivity.this.getCurrentFocus() != null) {  
	                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {  
	                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),  
	                            InputMethodManager.HIDE_NOT_ALWAYS);  
	                }  
	            }  
	        }  
	        return super.onTouchEvent(event);  
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	

}
