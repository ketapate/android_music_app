package com.keta2_app;

public class RowItem {
    private String imageId;
    private String years;
    private String genres;
    private String name;
    private String link;
 
    private String sample;
    private String title;
    private String performer;
    private String composers;
    
    private String artists;
    private String genre;
    private String year;
        
    public RowItem(int i,String a, String b, String c, String d, String e) {
    	if(i==1){
	        this.imageId = a;
	        this.years = b;
	        this.genres = c;
	        this.name = d;
	        this.link = e;
    	}
    	else if (i==2){
    		this.sample = a;
    		this.title = b;
    		this.performer = c;
    		this.composers = d;
    		this.link = e;
    	}
    }
    
    public RowItem(String imageId,String title,String artists,String genre,String year,String link){
    	this.imageId = imageId;
    	this.title = title;
    	this.artists = artists;
    	this.genre = genre;
    	this.year = year;
    	this.link = link;
    }

    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getYears() {
        return years;
    }
    public void setYears(String years) {
        this.years = years;
    }
    public String getGenres() {
        return genres;
    }
    public void setGenres(String genres) {
        this.genres = genres;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getSample() {
        return sample;
    }
    public void setSample(String sample) {
        this.sample = sample;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }    
    public String getPerformer() {
        return performer;
    }
    public void setPerformer(String performer) {
        this.performer = performer;
    }
    public String getComposers() {
        return composers;
    }
    public void setComposers(String composers) {
        this.composers = composers;
    }
    public String getArtists() {
        return artists;
    }
    public void setArtists(String artists) {
        this.artists = artists;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

}