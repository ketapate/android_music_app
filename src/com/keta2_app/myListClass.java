package com.keta2_app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class myListClass extends Activity implements OnItemClickListener {
	
	
ListView listView;
List<RowItem> rowItems;
MediaPlayer mediaPlayer;
Dialog dialog = null;
int flag=0;
static int item_id=-1;
static String[][] myData;
static String post_type;
String[] post_data;

String[] options1 = {"facebook"};
String[] options2 = {"facebook","sample"};

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.new_list);



rowItems = new ArrayList<RowItem>();


myData=MainActivity.data;
String artist_image = null;
String artist_years = null;
String artist_genres = null;
String artist_name = null;
String artist_link = null;

String album_image = null;
String album_title = null;
String album_artists = null;
String album_genre = null;
String album_year = null;
String album_link = null;

String song_sample = null;
String song_title = null;
String song_performer = null;
String song_composers = null;
String song_link = null;


if(MainActivity.myType.equals("artists")){
	post_type="artists";
	for(int i = 0; i < 5; i++) {
		if(myData[i][0]!="-1"){
			  artist_image = myData[i][0];
			  artist_years = myData[i][1];
			  artist_genres = myData[i][2];
			  artist_name = myData[i][3];
			  artist_link = myData[i][4];
			  RowItem item = new RowItem(1,artist_image,artist_years,artist_genres,artist_name,artist_link);
			  rowItems.add(item);
		}
	}
}
	
if(MainActivity.myType.equals("albums")){
	post_type="albums";
	for(int i = 0; i < 5; i++) {
		if(myData[i][0]!="-1"){
		  album_image = myData[i][0];
		  album_title = myData[i][1];
		  album_artists = myData[i][2];
		  album_genre = myData[i][3];
		  album_year = myData[i][4];
		  album_link = myData[i][5];
		  RowItem item = new RowItem(album_image,album_title,album_artists,album_genre,album_year,album_link);
		  rowItems.add(item);
		}
		}
}

if(MainActivity.myType.equals("songs")){
	post_type="songs";
	for(int i = 0; i < 5; i++) {		
		if(myData[i][0]!="-1"){
		  song_sample = myData[i][0];		  
		  song_title = myData[i][1];	  
		  song_performer = myData[i][2];
		  song_composers = myData[i][3];
		  song_link = myData[i][4];
		  RowItem item = new RowItem(2,song_sample,song_title,song_performer,song_composers,song_link);
		  rowItems.add(item);
		}
		}
}



listView = (ListView) findViewById(R.id.listView1);
MyAdapter adapter = new MyAdapter(this,R.layout.row, rowItems);
listView.setAdapter(adapter);
listView.setOnItemClickListener(this);


}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.showDialog(position);
		/*
		if(flag==1){
			this.MyMedia(position);
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
			}
			else{mediaPlayer.stop();}
		}
		*/
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		item_id=id;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(MainActivity.myType.equals("songs")){
			
			
			builder.setTitle("Post To Facebook").setItems(options2, new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface arg0, int which) {
					
					
					if(which==0){	//facebook option selected
						
						Intent next=new Intent(myListClass.this,myFacebook.class);
				    	startActivity(next);					
											
					}
					else{			//sample to be played						
						//flag=1;
						//redirect to the mp3 url
						if(myData[item_id][0].equals("N/A")){
							Toast toast = Toast.makeText(myListClass.this,"No Sample found", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();							
						}
						else{
							Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(myData[item_id][0]));
							startActivity(viewIntent);
						}
					}
				}
			});
		}
		else{
			
			builder.setTitle("Post To Facebook").setItems(options1, new DialogInterface.OnClickListener() {			
				@Override
				public void onClick(DialogInterface arg0, int which) {
					
					//Intent next=new Intent(myListClass.this,myFacebook.class);
		    		//startActivity(next);

		    		facebookLogin();
				}
			});
		}
		
		dialog=builder.create();
		return dialog;
	}
	
	protected void MyMedia(int id){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.reset();
		try {
			String url = rowItems.get(id).getSample();
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare(); // might take long! (for buffering, etc)

		}catch(Exception e){}
	}
	
	
	
	
	///////////////////////////////////////////////////
	public void facebookLogin() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {   
		@Override
		public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {
		        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
		                @Override
		                public void onCompleted(GraphUser user, Response response) {
		                        if (user != null) {                        	
		                        	facebookFeedDialog(myListClass.item_id,myListClass.post_type,myListClass.myData);		                        	
		                        }
		                }
		        });
		        }
		}
		});
		}
	
	
	public void facebookFeedDialog(int id,String type, String[][] data) {
		
		Bundle params = new Bundle();
		JSONObject attachment = new JSONObject();
		if(type.equals("artists")){			
			String years=data[id][1];
			String genres=data[id][2];
			
			JSONObject properties = new JSONObject();
			JSONObject prop1 = new JSONObject();
			
			try{
				if(data[id][0].equals("N/A")) {
					params.putString("picture","http://www-scf.usc.edu/~ketapate/artist.png");
				}
				else{params.putString("picture", data[id][0]);}	
				params.putString("link", data[id][4]);
				params.putString("name", data[id][3]);
				params.putString("description", "I like "+data[id][3]+" who is active since year "+years+" <center></center>Genre of Music is: "+genres);
				
				prop1.put("text", "here");
				prop1.put("href", data[id][4]);
	            properties.put("Look at details", prop1);
				
	            attachment.put("properties",properties);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                        
			params.putString("properties",properties.toString());
              
					
		}
		else if(type.equals("albums")){		
			String artist=data[id][2];
			String genre=data[id][3];
			String year=data[id][4];
			
			JSONObject properties = new JSONObject();
			JSONObject prop1 = new JSONObject();
			
			try{
				if(data[id][0].equals("N/A")) {
					params.putString("picture","http://www-scf.usc.edu/~ketapate/album.png");
				}
				else{params.putString("picture", data[id][0]);}	
				params.putString("link", data[id][5]);
				params.putString("name", data[id][1]);
				params.putString("description", "I like "+data[id][1]+" released in year "+year+" <center></center>Artist: "+artist+"<center></center>Genre: "+genre);
				
				prop1.put("text", "here");
				prop1.put("href", data[id][5]);
	            properties.put("Look at details", prop1);
				
	            attachment.put("properties",properties);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                        
			params.putString("properties",properties.toString());
              
					
		}
		if(type.equals("songs")){			
			String performer=data[id][2];
			String composer=data[id][3];
			
			JSONObject properties = new JSONObject();
			JSONObject prop1 = new JSONObject();
			
			try{
				
				params.putString("picture","http://www-scf.usc.edu/~ketapate/song.png");
					
				params.putString("link", data[id][4]);
				params.putString("name", data[id][1]);
				params.putString("description", "I like "+data[id][1]+" composed by "+composer+" <center></center>Performer: "+performer);
				
				prop1.put("text", "here");
				prop1.put("href", data[id][4]);
	            properties.put("Look at details", prop1);
				
	            attachment.put("properties",properties);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                        
			params.putString("properties",properties.toString());
              
					
		}
		
		
		 
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params)).setOnCompleteListener(new WebDialog.OnCompleteListener() {
		       // @Override
		        public void onComplete(Bundle values, FacebookException error) {
		                if (error == null) {
		                        final String postId = values.getString("post_id");
		                        if (postId != null) {		                        
		                        	Toast.makeText(getApplicationContext(),"Feed Posted",Toast.LENGTH_SHORT).show();
		                        } else {		                        
		                        	Toast.makeText(getApplicationContext(),"Feed Not Posted",Toast.LENGTH_SHORT).show();
		                        }
		                } else if (error instanceof FacebookOperationCanceledException) {		                        
		                	Toast.makeText(getApplicationContext(),"Post Cancelled",Toast.LENGTH_SHORT).show();
		                } else {
		                   	Toast.makeText(getApplicationContext(),"Error Posting",Toast.LENGTH_SHORT).show();		                        
		                }
		        }
		 
		}).build();
		feedDialog.show();
		}
	
		
	
}
