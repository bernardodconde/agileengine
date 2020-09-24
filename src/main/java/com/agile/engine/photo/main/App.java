package com.agile.engine.photo.main;

import com.agile.engine.photo.model.ImageResponse;
import com.agile.engine.photo.model.ImageWsClient;
import com.agile.engine.photo.model.Picture;
import com.agile.engine.photo.model.TokenBody;
import com.agile.engine.photo.model.TokenResponse;
import com.agile.engine.photo.model.TokenWsClient;
import com.agile.engine.photo.utils.RestClientFactory;

/**
 * AgileEngine's interview
 *
 */
public class App {
	private static String apiUrl = "http://interview.agileengine.com/";

	public static void main(String[] args) {
		System.out.println("Welcome to Berni's code");
		
		String page = "2";
		int imageNumber = 0;
		String token = null;
		ImageResponse images = null;
		Picture image = null;

		try {
			
			token = getToken();
			images = getImages(token,page);
			image = getImage(token,images.pictures.get(imageNumber).id);

		} catch (Exception e) {
			
			System.out.println("something went wrong = " + e.getCause());
			System.out.println("retry");
			
			token = getToken();
			images = getImages(token,page);
			image = getImage(token,images.pictures.get(imageNumber).id);			

		}

		System.out.println("image info");
		System.out.println("image id = " + image.id);
		System.out.println("image autohr = " + image.author);
		System.out.println("image caera = " + image.camera);
		System.out.println("image full picture link = " + image.full_picture);
		System.out.println("image cropped picture link = " + image.cropped_picture);
		System.out.println("image tags = " + image.tags);
	}
	
	public static String getToken(){
		
		String token = null;
		
		RestClientFactory factory = new RestClientFactory();

		try {
			TokenWsClient client = factory.getRestClient(apiUrl, TokenWsClient.class);
			TokenResponse response = client.sendTokenWsClient(new TokenBody());
			token = response.token;
		} catch (Exception e) {
			
			System.out.println("something went wrong = " + e.getCause());

		}
		
		return token;
		
	}
	
	public static ImageResponse getImages(String token, String page){
		
		ImageResponse images = null;
		
		RestClientFactory factory = new RestClientFactory();

		try {
			ImageWsClient secureClient = factory.getSecureRestClient(apiUrl, ImageWsClient.class, token);
			images = secureClient.getImages(page);

		} catch (Exception e) {
			
			System.out.println("something went wrong = " + e.getCause());

		}

		return images;
	}
	
	
	public static Picture getImage(String token, String imageNumber){
		
		Picture image = null;
		
		RestClientFactory factory = new RestClientFactory();
		

		try {
			
			ImageWsClient secureClient = factory.getSecureRestClient(apiUrl, ImageWsClient.class, token);
			image = secureClient.getImage(imageNumber);

			System.out.println("image info");
			System.out.println("image id = " + image.id);
			System.out.println("image autohr = " + image.author);
			System.out.println("image caera = " + image.camera);
			System.out.println("image full picture link = " + image.full_picture);
			System.out.println("image cropped picture link = " + image.cropped_picture);
			System.out.println("image tags = " + image.tags);

		} catch (Exception e) {
			
			System.out.println("something went wrong = " + e.getCause());

		}

		return image;
	}
}
