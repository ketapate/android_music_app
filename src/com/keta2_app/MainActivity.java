package com.keta2_app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
	public static String[][] data = new String[5][6]; 
	public static String myType = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 
		
		 	
		 Button click=(Button)findViewById(R.id.button1);
         click.setOnClickListener(new View.OnClickListener() {	
        	 @Override
        	 public void onClick(View v) {       		
        		
				Spinner c= (Spinner)findViewById(R.id.spinner1);
				EditText a = (EditText)findViewById(R.id.editText1);			
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(a.getWindowToken(),0);
		
				String mySearch=a.getText().toString();
				try {
					mySearch=URLEncoder.encode(mySearch,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				String myType=c.getSelectedItem().toString();		
				String url="http://cs-server.usc.edu:19441/k/MyServlet1?search="+mySearch+"&type="+myType;
	
				new LongOperation().execute(url);
				
				
        	 }
        	 });
         }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	private class LongOperation extends AsyncTask<String, Void, String> {
		  private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		  private static final int WAIT_TIMEOUT = 30 * 1000;
		  private final HttpClient httpclient = new DefaultHttpClient();
		  final HttpParams params = httpclient.getParams();
		  HttpResponse response;
		  private String content =  "no data";
		  private boolean error = false;
		  private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		 
		  
		  protected void onPreExecute() {			  
		   dialog.setMessage("Getting your data... Please wait...");
		   dialog.show();			   
		  }
		
		
		@Override
        protected String doInBackground(String... urls) {
			String URL = null;		    
			   try {			 
			   URL = urls[0];
			   HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
			   HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
			   ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
			 
			   HttpGet httpGet = new HttpGet(URL);
			   response = httpclient.execute(httpGet);
	
			   StatusLine statusLine = response.getStatusLine();
			   content="status code="+statusLine.getStatusCode();
	           if(statusLine.getStatusCode() == HttpStatus.SC_OK){	 
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                content = out.toString();	  
	            }	 
	            else{
	                //Closes the connection.
	                Log.w("HTTP1:",statusLine.getReasonPhrase());
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			   }catch (ClientProtocolException e) {
		            Log.w("HTTP2:",e );
		        } catch (IOException e) {
		            Log.w("HTTP3:",e );
		        }catch (Exception e) {
		            Log.w("HTTP4:",e );
		        }
			   
 
			return content;
		}	
		
		 protected void onCancelled() {		
			   dialog.dismiss();
			   Toast toast = Toast.makeText(MainActivity.this, 
			     "Error connecting to Server", Toast.LENGTH_LONG);
			   toast.setGravity(Gravity.TOP, 25, 400);
			   toast.show();
			  }
			 
		 protected void onPostExecute(String content) {
			 dialog.dismiss();
			 Toast toast;
	
			 
			 if (error) {
				 toast = Toast.makeText(MainActivity.this, 
			     content, Toast.LENGTH_LONG);
				 toast.setGravity(Gravity.TOP, 25, 400);
				 toast.show();
			 } else {	
				 
	
										
	 
				 
				JSONObject jObj1=null;
				JSONObject jObj2=null;
				JSONArray jArr=null;
				try {
					jObj1=new JSONObject(content);
					jObj2=jObj1.getJSONObject("results");
					jArr=jObj2.getJSONArray("result");
					
				} catch (JSONException e1) {					
					e1.printStackTrace();
				}
				
				Spinner c= (Spinner)findViewById(R.id.spinner1);
				myType=c.getSelectedItem().toString();
				
				initData();
				
			    try {
			        // looping through All Contacts
			    	if(myType.equals("artists")){
			            for(int i = 0; i < jArr.length(); i++){
	            	
			            	JSONObject result = jArr.getJSONObject(i);           
	
			                String image = result.getString("image");
			                String years = result.getString("years");
			                String genres = result.getString("genres");
			                String name = result.getString("name");
			                String link = result.getString("link");
			                
			                image=URLDecoder.decode(image, "UTF-8");		                
			                years=URLDecoder.decode(years, "UTF-8");
			                genres=URLDecoder.decode(genres, "UTF-8");
			                name=URLDecoder.decode(name, "UTF-8");
			                link=URLDecoder.decode(link, "UTF-8");		
			                
			                
							data[i][0]=image;
							data[i][1]=years;
							data[i][2]=genres;
							data[i][3]=name;
							data[i][4]=link;
			            }
		            }
			    	else if(myType.equals("albums")){
			            for(int i = 0; i < jArr.length(); i++){
			            	JSONObject result = jArr.getJSONObject(i);           
	
			                String image = result.getString("image");
			                String title = result.getString("title");
			                String artists = result.getString("artists");
			                String genre = result.getString("genre");
			                String year = result.getString("year");
			                String link = result.getString("link");
			                
			                image=URLDecoder.decode(image, "UTF-8");		                
			                title=URLDecoder.decode(title, "UTF-8");
			                artists=URLDecoder.decode(artists, "UTF-8");
			                genre=URLDecoder.decode(genre, "UTF-8");
			                year=URLDecoder.decode(year, "UTF-8");
			                link=URLDecoder.decode(link, "UTF-8");
			                
			                data[i][0]=image;
							data[i][1]=title;
							data[i][2]=artists;
							data[i][3]=genre;
							data[i][4]=year;
							data[i][5]=link;
			            }
		            }
			    	else if(myType.equals("songs")){
			            for(int i = 0; i < jArr.length(); i++){
			            	JSONObject result = jArr.getJSONObject(i);           
	
			                String sample = result.getString("sample");
			                String title = result.getString("title");
			                String performer = result.getString("performer");
			                String composers = result.getString("composers");
			                String link = result.getString("link");
			                
			                sample=URLDecoder.decode(sample, "UTF-8");		                
			                title=URLDecoder.decode(title, "UTF-8");
			                performer=URLDecoder.decode(performer, "UTF-8");
			                composers=URLDecoder.decode(composers, "UTF-8");
			                link=URLDecoder.decode(link, "UTF-8");
			                
			                data[i][0]=sample;
							data[i][1]=title;
							data[i][2]=performer;
							data[i][3]=composers;
							data[i][4]=link;
							
			            }		            
		            }
			    	
			    	if(data[0][0].equals("N/A")&&data[0][1].equals("N/A")&&data[0][2].equals("N/A")&&data[0][3].equals("N/A")&&data[0][4].equals("N/A")){
			    		System.out.println("KETA :: type="+myType+" 0:"+data[0][0]+" 1:"+data[0][1]+" 2:"+data[0][2]+" 3:"+data[0][3]+" 4:"+data[0][4]);
			    		
			    		Toast toast1 = Toast.makeText(MainActivity.this,"No Discography Found for your search", Toast.LENGTH_LONG);
						toast1.setGravity(Gravity.TOP, 25, 400);
						toast1.show();
									    		
			    	}
			    	else{
				    	Intent next=new Intent(MainActivity.this,myListClass.class);
		        		startActivity(next);
			    	}
			    	
		        }catch(Exception e)
			   {
			   }			 

			 }

		 }

		 
		 void initData(){
			 			 
			 for(int i=0;i<5;i++){
				 for(int j=0;j<6;j++){
					 data[i][j]="-1";				 
				 }				 
			 }		 
		 }
		 
//        @Override
        protected void onProgressUpdate(Void... values) {
        }
	}
}
