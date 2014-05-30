package com.keta2_app;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;

public class myFacebook extends Activity {

	String[] data;	
	String type;
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
            super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	
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
	
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.facebook);
	    setContentView(R.layout.new_list);
	    facebookLogin();

		}
}
