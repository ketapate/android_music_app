package com.keta2_app;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MyAdapter extends ArrayAdapter<RowItem> {
 
    Context context;

    public MyAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        //ImageView imageView;
    	
        ImageView image;
        TextView artist_name;
        TextView artist_genre;
        TextView artist_year;
        
        
        TextView album_title;
        TextView album_artists;
        TextView album_genre;
        TextView album_year;
        
        ImageView sample;
        TextView song_title;
        TextView song_performer;
        TextView song_composer;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
 
        
 
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	if(MainActivity.myType.equals("artists")){
        		convertView = mInflater.inflate(R.layout.row_artist, null);
        	}        	
        	else if(MainActivity.myType.equals("albums")){
        		convertView = mInflater.inflate(R.layout.row_album, null);
        	}
        	else if(MainActivity.myType.equals("songs")){
        		convertView = mInflater.inflate(R.layout.row_song, null);
        	}
            holder = new ViewHolder();
            
            
            if(MainActivity.myType.equals("artists")){
	            holder.image = (ImageView) convertView.findViewById(R.id.image);
	            holder.artist_name = (TextView) convertView.findViewById(R.id.artist_name);
	            holder.artist_genre = (TextView) convertView.findViewById(R.id.artist_genre);
	            holder.artist_year = (TextView) convertView.findViewById(R.id.artist_year);
            }
            if(MainActivity.myType.equals("albums")){
            	holder.image = (ImageView) convertView.findViewById(R.id.image);
	            holder.album_title = (TextView) convertView.findViewById(R.id.album_title);            	
	            holder.album_artists = (TextView) convertView.findViewById(R.id.album_artists);
	            holder.album_genre = (TextView) convertView.findViewById(R.id.album_genre);            
	            holder.album_year = (TextView) convertView.findViewById(R.id.album_year);
            }
            if(MainActivity.myType.equals("songs")){
            	holder.sample = (ImageView) convertView.findViewById(R.id.sample);
            	holder.song_title = (TextView) convertView.findViewById(R.id.song_title);
	            holder.song_performer = (TextView) convertView.findViewById(R.id.song_performer);
	            holder.song_composer = (TextView) convertView.findViewById(R.id.song_composer);	            
            }
            	
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
          
        ImageLoader imgLoader = new ImageLoader(getContext());
        int loader_album = R.drawable.album;
        int loader_artist = R.drawable.artist;

        
        if(MainActivity.myType.equals("artists")){
        	if(!(rowItem.getImageId().equals("N/A"))){
        		// call DisplayImage function
                // url - image url to load
                // loader - loader image, will be displayed before getting image
                // image - ImageView 		
                imgLoader.DisplayImage(rowItem.getImageId(), loader_artist, holder.image );     		
			
        	}else{holder.image.setImageResource(R.drawable.artist);}

	        holder.artist_name.setText("Name: "+rowItem.getName());
	        holder.artist_genre.setText("Genre: "+rowItem.getGenres());
	        holder.artist_year.setText("Year: "+rowItem.getYears());	             
        }
        else if(MainActivity.myType.equals("albums")){
        	if(!(rowItem.getImageId().equals("N/A"))){ 		 		
                imgLoader.DisplayImage(rowItem.getImageId(), loader_album, holder.image );		
        	}
        	else{		
        		holder.image.setImageResource(R.drawable.album);
        	}
		    holder.album_title.setText("Title: "+rowItem.getTitle());       
		    holder.album_artists.setText("Artist: "+rowItem.getArtists());
		    holder.album_genre.setText("Genre: "+rowItem.getGenre());
		    holder.album_year.setText("Year: "+rowItem.getYear());	        
        	
        }
        else if(MainActivity.myType.equals("songs")){	        
	        	holder.sample.setImageResource(R.drawable.song);        	
		        holder.song_title.setText("Title: "+rowItem.getTitle());
		        holder.song_performer.setText("Performer: "+rowItem.getPerformer());
		        holder.song_composer.setText("Composer: "+rowItem.getComposers());	        	        
	    }

        return convertView;
    }



}